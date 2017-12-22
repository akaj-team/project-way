package vn.asiantech.way.ui.share

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.util.Log.d
import android.view.View
import android.view.animation.Animation
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.hypertrack.lib.HyperTrack
import com.hypertrack.lib.HyperTrackUtils
import com.hypertrack.lib.internal.consumer.view.MarkerAnimation
import com.hypertrack.lib.models.HyperTrackLocation
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.setContentView
import vn.asiantech.way.R
import vn.asiantech.way.data.model.LocationRoad
import vn.asiantech.way.data.model.Row
import vn.asiantech.way.data.model.TrackingInformation
import vn.asiantech.way.data.model.WayLocation
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.custom.ArrivedDialog
import vn.asiantech.way.ui.custom.BottomButtonCard
import vn.asiantech.way.ui.custom.RadiusAnimation
import vn.asiantech.way.ui.search.SearchActivity
import vn.asiantech.way.utils.AppConstants
import vn.asiantech.way.utils.DateTimeUtil
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 27/11/2017.
 */
class ShareActivity : BaseActivity(), GoogleMap.OnCameraIdleListener, LocationListener, GoogleApiClient.ConnectionCallbacks {
    companion object {
        private const val TYPE_PROGRESS_MAX = 100
        private const val INTERVAL = 500L
        private const val ZOOM_SIZE = 16f
        private const val SECOND_VALUE = 1000L
        private const val STEP_ETA = 3
        private const val REQUEST_CODE = 200
        private const val Z_INDEX_VALUE = 8f
        private const val POLYLINE_WIDTH = 20f

        const val ACTION_SEND_WAY_LOCATION = "action_send_way_location"
        const val KEY_LOCATION = "location"
        const val ACTION_CHOOSE_ON_MAP = "action_choose_on_map"
        const val ACTION_CURRENT_LOCATION = "action_current_location"
        const val STOP_STATUS = "STOP"
        const val MOVING_STATUS = "MOVING"
        const val DRIVER_STATUS = "DRIVER"
    }

    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var handleTrackingProgress: Disposable
    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var shareViewModel: ShareViewModel
    private lateinit var destinationLatLng: LatLng
    private lateinit var googleMap: GoogleMap
    private val ui = ShareActivityUI()
    private var locations = mutableListOf<TrackingInformation>()
    private var locationUpdates = mutableListOf<LatLng>()
    private var currentMarker: Marker? = null
    private var marker: Marker? = null
    private var locationLatLng: LatLng? = null
    private var currentLatLng: LatLng? = null
    private var currentLocation: Location? = null
    private var isStartTracking = false
    private var isStopTracking = false
    private var isReTracking = false
    private var isArrived = false
    private var isConfirm = false
    private var isSetETA = false
    private var action: String? = null
    private var currentStatus = STOP_STATUS
    private var destinationName = ""
    private var etaDistance = ""
    private var timeArrived = ""
    private var timeStart = ""
    private var etaTime = ""
    private var time = ""
    private var groundOverlay: GroundOverlay? = null
    private var myLocation: WayLocation? = null
    private var lineSmall: Polyline? = null
    private var lineLarge: Polyline? = null
    private var distanceTravel = 0f
    private var averageSpeed = 0f
    private var etaMaximum = 0.0f
    private var etaUpdate = 0.0f
    private var distance = 0.0f
    private var endStatus = 0L
    private var angle = 0.0f
    private var startPosition = 0
    private var countTimer = SECOND_VALUE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui.setContentView(this)
        shareViewModel = ShareViewModel(WayRepository())
        shareViewModel.registerBatteryReceiver(this)
        supportMapFragment = SupportMapFragment()
        if (intent.extras != null) {
            myLocation = intent.getParcelableExtra(AppConstants.KEY_LOCATION)
        }
        action = intent.action
        initMap()
        configMap()
        initLocationRequest()
        initGoogleApiClient()
        initBottomButtonCard(true, action)
        setEnableRlSearchLocation()
    }

    override fun onBindViewModel() {
        addDisposables(shareViewModel.batteryCapacity
                .observeOnUiThread()
                .subscribe(this::handleShowBatteryCapacity),
                shareViewModel.getCalendarDateTime()
                        .observeOnUiThread()
                        .subscribe(this::handleGetCurrentDateTime))
    }

    override fun onLocationChanged(location: Location) {
        if (!isStartTracking) {
            if (marker != null && groundOverlay != null) {
                val latLng = LatLng(location.latitude, location.longitude)
                marker?.position = latLng
                groundOverlay?.position = latLng
            }
        }
    }

    override fun onCameraIdle() {
        locationLatLng = googleMap.cameraPosition?.target
        if (!isConfirm) {
            setLocationNameWhenCameraMoving()
        }
    }

    override fun onConnected(p0: Bundle?) {
        if (!HyperTrack.checkLocationPermission(this)) {
            HyperTrack.requestPermissions(this)
        }

        if (!HyperTrack.checkLocationServices(this)) {
            HyperTrack.requestLocationServices(this)
        }

        if (HyperTrackUtils.isInternetConnected(this)) {
            if (HyperTrackUtils.isLocationEnabled(this)) {
                LocationServices.FusedLocationApi
                        .requestLocationUpdates(googleApiClient, locationRequest, this)
            }
        }
    }

    override fun onConnectionSuspended(p0: Int) = Unit

    override fun onResume() {
        super.onResume()
        googleApiClient.connect()
    }

    override fun onPause() {
        super.onPause()
        googleApiClient.disconnect()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, SearchActivity::class.java))
        this.finish()
    }

    internal fun eventCopyLinkToClipboard() {
        try {
            (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip =
                    ClipData.newPlainText("tracking_url", ui.bottomCard.tvURL.text)
        } catch (e: IOException) {
            Log.d("zxc", "ErrorResponse: " + e.message)
        }
    }

    internal fun eventActionButtonClicked() {
        when (action) {
            AppConstants.ACTION_CHOOSE_ON_MAP -> {
                action = AppConstants.ACTION_SEND_WAY_LOCATION
                initBottomButtonCard(true, action)
                locationLatLng?.let { destinationLatLng = it }
                addDestinationMarker(destinationLatLng)
                isConfirm = true
            }
        // Click sharing
            AppConstants.ACTION_SEND_WAY_LOCATION, AppConstants.ACTION_CURRENT_LOCATION -> {
                action = null
                ui.bottomCard.startProgress()
                addDisposables(shareViewModel.getTrackingURL()
                        .observeOnUiThread()
                        .subscribe(this::handleTrackingUrlComplete))
                isStartTracking = true
                handlerProgressTracking()
            }
            else -> shareLocation()
        }
        setEnableRlSearchLocation()
    }

    internal fun eventRlSearchLocationClicked() {
        startActivity(Intent(this, SearchActivity::class.java))
    }

    internal fun eventButtonBackClicked() {
        onBackPressed()
    }

    internal fun eventConfirmStopDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.share_textview_text_dialog_title))
        builder.setMessage(resources.getString(R.string.share_textview_text_dialog_message))
        builder.setCancelable(true)
        builder.setNegativeButton("No") { dialog, _ -> dialog?.cancel() }
        builder.setPositiveButton("Stop") { _, _ ->
            isStopTracking = true
            handleTrackingProgress.dispose()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    internal fun eventShowDialog() {
        val dialog = ArrivedDialog.newInstance(formatInterval(countTimer), etaDistance)
        val fragmentManager = supportFragmentManager
        dialog.show(fragmentManager, resources.getString(R.string.arrived_dialog_tag))
    }

    private fun setEnableRlSearchLocation() {
        if (action != AppConstants.ACTION_CHOOSE_ON_MAP) {
            ui.rlSearchLocation.isEnabled = false
        }
    }

    // Get ETA Distance and duration
    private fun handleDistanceETA(row: List<Row>) {
        d("zxc", "aa $row")
        ui.trackingInfo.tvTime.text = resources.getString(R.string.eta, row[0].elements[0]
                .duration.text)
        ui.trackingInfo.tvDistance.text = resources.getString(R.string.open_parentheses,
                row[0].elements[0].distance.text)
        etaDistance = row[0].elements[0].distance.text
    }

    // Get list lat lng direction
    private fun handleListLocationWhenReOpen(list: List<LocationRoad>) {
    }

    // Show dialog when arrived
    private fun setArrived() {
        val handleStartAddress: (String) -> Unit = { ui.trackingInfo.tvStartAddress.text = it }
        val handleEndAddress: (String) -> Unit = { ui.trackingInfo.tvEndAddress.text = it }
        val handleStartTime: (String) -> Unit = { ui.trackingInfo.tvTime.text = it }
        val handleEndTime: (String) -> Unit = {
            ui.trackingInfo.tvEndTime.text = it
            timeArrived = it
        }
        ui.bottomCard.hideBottomCardLayout()
        ui.trackingInfo.showTrackingProgress()
        ui.trackingInfo.hideTrackingInfor()
        ui.trackingInfo.showArrivedInfor()
        ui.trackingInfo.circleProgressBar.progress = TYPE_PROGRESS_MAX
        ui.trackingInfo.circleProgressBar.circleProgressColor = ContextCompat.getColor(this, R.color.violet)
        ui.trackingInfo.tvStartTime.text = timeStart
        locationLatLng?.let {
            addDisposables(shareViewModel.getLocationName(this, it)
                    .observeOnUiThread()
                    .subscribe(handleStartAddress),
                    shareViewModel.getLocationName(this, destinationLatLng)
                            .observeOnUiThread()
                            .subscribe(handleEndAddress),
                    shareViewModel.getCalendarDateTime()
                            .observeOnUiThread()
                            .subscribe(handleEndTime),
                    shareViewModel.getTimeDuringStatus(countTimer / AppConstants.TIME_ZOOM_CAMERA)
                            .observeOnUiThread()
                            .subscribe(handleStartTime))
        }
        ui.trackingInfo.tvDistance.text = etaDistance
        ui.trackingInfo.imgBtnCall.imageResource = R.drawable.ic_reached
        eventShowDialog()
    }

    private fun shareLocation() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        val message = "My Location is ${ui.bottomCard.tvURL.text}"
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message)
        startActivityForResult(Intent.createChooser(sharingIntent, "Share via"), REQUEST_CODE)
    }

    private fun addDestinationMarker(latLng: LatLng?) {
        if (latLng != null) {
            googleMap.addMarker(MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_expected_place_marker))
                    .title(destinationName)
                    .anchor(AppConstants.KEY_DEFAULT_ANCHOR, AppConstants.KEY_DEFAULT_ANCHOR))
                    ?.showInfoWindow()
            ui.imgPickLocation.visibility = View.INVISIBLE
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                    .target(latLng)
                    .zoom(ZOOM_SIZE)
                    .build()), AppConstants.TIME_ZOOM_CAMERA, object : GoogleMap.CancelableCallback {
                override fun onFinish() = Unit

                override fun onCancel() = Unit
            })
        }
    }

    private fun drawCurrentMaker(location: Location) {
        val currentLocation = LatLng(location.latitude, location.longitude)
        if (!isStartTracking) {
            googleMap.clear()
            marker = googleMap.addMarker(MarkerOptions()
                    .position(currentLocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_point))
                    .title(getString(R.string.current_location))
                    .anchor(AppConstants.KEY_DEFAULT_ANCHOR, AppConstants.KEY_DEFAULT_ANCHOR))
            addPulseRing(currentLocation)
            if (action == AppConstants.ACTION_CHOOSE_ON_MAP)
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                        .target(currentLocation)
                        .zoom(ZOOM_SIZE)
                        .build()), AppConstants.TIME_ZOOM_CAMERA, object : GoogleMap.CancelableCallback {
                    override fun onFinish() = Unit

                    override fun onCancel() = Unit
                })
        } else {
            marker?.remove()
        }
    }

    private fun addPulseRing(latLng: LatLng) {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.OVAL
        drawable.setSize(AppConstants.KEY_DRAWABLE_SIZE, AppConstants.KEY_DRAWABLE_SIZE)
        drawable.setColor(ContextCompat.getColor(this, R.color.pulse_color))

        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        groundOverlay = googleMap.addGroundOverlay(GroundOverlayOptions()
                .position(latLng, AppConstants.KEY_GROUND_OVERLAY_POSITION)
                .image(BitmapDescriptorFactory.fromBitmap(bitmap)))
        val groundAnimation = RadiusAnimation(groundOverlay)
        groundAnimation.repeatCount = Animation.INFINITE
        groundAnimation.duration = AppConstants.KEY_GR_ANIMATION_DUR
        supportMapFragment.view?.startAnimation(groundAnimation)
    }

    private fun getLocationName(locationName: String) {
        ui.tvLocation.text = locationName
    }

    private fun initLocationRequest() {
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = ShareActivity.INTERVAL
        locationRequest.fastestInterval = ShareActivity.INTERVAL
    }

    private fun initGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build()
    }

    private fun requestLocation() {
        addDisposables(shareViewModel.getCurrentHyperTrackLocation()
                .observeOnUiThread()
                .subscribe(this::handleGetCurrentLocation))
    }

    private fun updateCurrentTimeView() {
        ui.trackingInfo.tvSpeed.text = String.format("%.2f", averageSpeed).plus(" km/h")
        ui.trackingInfo.circleProgressBar.progress = AppConstants.PROGRESS_BAR_MAX
    }

    private fun updateUI(hyperTrackLocation: HyperTrackLocation) {
        val latLng = hyperTrackLocation.geoJSONLocation.latLng
        currentLatLng = latLng
        // Get location distance estimate
        d("zxc", "kdkdk " + "${latLng.latitude},${latLng.longitude}" +
                "${destinationLatLng.latitude},${destinationLatLng.longitude}")
        addDisposables(shareViewModel
                .getLocationDistance("${latLng.latitude},${latLng.longitude}",
                        "${destinationLatLng.latitude},${destinationLatLng.longitude}")
                .observeOnUiThread()
                .subscribe(this::handleDistanceETA),
                shareViewModel.compareLocation(latLng,
                        destinationLatLng)
                        .observeOnUiThread()
                        .subscribe(this::handleCompareLocation),
                shareViewModel.getCalendarDateTime()
                        .observeOnUiThread()
                        .subscribe(this::handleGetCurrentDateTime))

        if (latLng != null) {
            if (isReTracking && locationLatLng != latLng) {
                isReTracking = false
            } else {
                locationUpdates.add(latLng)
            }
        }

        if (locationUpdates.size > 1) {
            getListLocation(latLng, locationUpdates.size - 1)
        }
        updateView()
        updateMapView(latLng)
    }

    private fun updateView() {
        if (!isStopTracking) {
            ui.trackingInfo.tvActionStatus.text = resources.getString(R.string.leaving)
        }
        ui.trackingInfo.tvSpeed.text = String.format("%.2f", averageSpeed).plus(" km/h")
        ui.trackingInfo.tvElapsedTime.text = formatInterval(countTimer)
        ui.trackingInfo.tvDistanceTravelled.text = String.format("%.2f", distanceTravel).plus(" km")
        if (etaMaximum - etaUpdate > STEP_ETA) {
            ui.trackingInfo.circleProgressBar.progress = (etaMaximum - (etaUpdate /
                    etaMaximum) * AppConstants.PROGRESS_BAR_MAX).toInt()
        }
        drawLine()
    }

    private fun formatInterval(millis: Long): String = String.format("%02d:%02d:%02d"
            , TimeUnit.MILLISECONDS.toHours(millis)
            , TimeUnit.MILLISECONDS.toMinutes(millis)
            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))
            , TimeUnit.MILLISECONDS.toSeconds(millis)
            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))

    private fun drawLine() {
        if (locationUpdates.size > 1) {
            val option = PolylineOptions()
                    .width(POLYLINE_WIDTH)
                    .geodesic(true)
                    .color(Color.parseColor("#1976D2"))
                    .zIndex(Z_INDEX_VALUE)
                    .startCap(RoundCap())
                    .endCap(RoundCap())
                    .jointType(JointType.BEVEL)
                    .addAll(locationUpdates)
            val option1 = PolylineOptions()
                    .width(ZOOM_SIZE)
                    .geodesic(true)
                    .color(Color.parseColor("#2196F3"))
                    .zIndex(Z_INDEX_VALUE)
                    .startCap(RoundCap())
                    .endCap(RoundCap())
                    .jointType(JointType.BEVEL)
                    .addAll(locationUpdates)
            lineSmall?.remove()
            lineSmall = googleMap.addPolyline(option)
            lineSmall?.pattern = null
            lineLarge?.remove()
            lineLarge = googleMap.addPolyline(option1)
            lineLarge?.pattern = null
        }
    }

    private fun updateMapView(latLng: LatLng) {
        if (locationUpdates.size > 1) {
            addDisposables(shareViewModel.getAngleMarker(locationUpdates[locationUpdates.size - 2],
                    locationUpdates[locationUpdates.size - 1])
                    .observeOnUiThread()
                    .subscribe(this::handleAngleMarker))
        }
        if (currentMarker == null) {
            currentMarker = googleMap.addMarker(MarkerOptions().
                    position(latLng).
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_hero_marker))
                    .anchor(AppConstants.KEY_DEFAULT_ANCHOR, AppConstants.KEY_DEFAULT_ANCHOR)
                    .flat(true))
        } else {
            currentMarker?.position = latLng
            currentMarker?.rotation = angle
        }
        MarkerAnimation.animateMarker(currentMarker, latLng)
    }

    private fun checkStatus(speed: Float) = if (speed <= AppConstants.MIN_SPEED) {
        STOP_STATUS
    } else if (speed > AppConstants.MIN_SPEED && speed <= AppConstants.MAX_SPEED) {
        MOVING_STATUS
    } else DRIVER_STATUS

    private fun getListLocation(latLng: LatLng, position: Int) {
        val status = checkStatus(averageSpeed)
        var description = ""
        val handleDescription: (string: String) -> Unit = { description = it }
        if (currentStatus != status) {
            if (status == STOP_STATUS) {
                addDisposables(shareViewModel.getLocationName(this, latLng)
                        .observeOnUiThread()
                        .subscribe(handleDescription))
            } else {
                // Time during status
                addDisposables(shareViewModel.getTimeDuringStatus((countTimer - endStatus) /
                        AppConstants.TIME_ZOOM_CAMERA)
                        .observeOnUiThread()
                        .subscribe(this::handleTimeDuringStatus))
                for (i in 0..(position - 1)) {
                    addDisposables(shareViewModel
                            .getDistancePerSecond(locationUpdates[i], locationUpdates[i + 1])
                            .observeOnUiThread()
                            .subscribe(this::handleDistancePerSecond))
                }
                endStatus = countTimer
                description = time.plus(" | ").plus(distance.toString()).plus("km")
            }
        }
        val location = TrackingInformation(DateTimeUtil().getTimeChangeStatus(),
                status, description, latLng)
        locations.add(location)
        currentStatus = status
        startPosition = position
    }

    private fun deleteListTrackingLatLng() {
        locationUpdates.clear()
    }

    private fun initMap() {
        supportFragmentManager.beginTransaction().replace(R.id.share_activity_map, supportMapFragment).commit()
    }

    private fun configMap() {
        supportMapFragment.getMapAsync {
            it.setOnCameraIdleListener(this)
            this.googleMap = it
            mapEvent()
        }
    }

    private fun mapEvent() {
        handleDrawCurrentMarker()
    }

    private fun drawMarkerWhenClickItemSearch() {
        val lat = myLocation?.geometry?.location?.lat
        val lng = myLocation?.geometry?.location?.lng
        if (lat != null && lng != null) {
            locationLatLng = LatLng(lat, lng)
            addDestinationMarker(locationLatLng)
        }
    }

    private fun initBottomButtonCard(show: Boolean, action: String?) {
        when (action) {
            AppConstants.ACTION_CHOOSE_ON_MAP ->
                setViewBottomCardWhenChooseLocation(ui.bottomCard)

            AppConstants.ACTION_CURRENT_LOCATION, AppConstants.ACTION_SEND_WAY_LOCATION ->
                setViewBottomCardWhenConfirmedLocation(ui.bottomCard)

            else -> setViewBottomCardWhenShareLink(ui.bottomCard)
        }
        if (show && !isArrived) {
            ui.bottomCard.showBottomCardLayout()
            ui.trackingInfo.hideTrackingProgress()
        }
    }

    private fun setViewBottomCardWhenChooseLocation(bottomCard: BottomButtonCard) {
        with(bottomCard) {
            hideCloseButton()
            hideTvTitle()
            setDescriptionText(getString(R.string.confirm_move_map))
            setShareButtonText(getString(R.string.confirm_location))
            showActionButton()
        }
    }

    private fun setViewBottomCardWhenConfirmedLocation(bottomCard: BottomButtonCard) {
        with(bottomCard) {
            hideCloseButton()
            hideTvDescription()
            setTitleText(getString(R.string.share_textview_text_look_good))
            setShareButtonText(getString(R.string.share_textview_text_start_sharing))
            showActionButton()
        }
    }

    private fun setViewBottomCardWhenShareLink(bottomCard: BottomButtonCard) {
        with(bottomCard) {
            ui.imgCurrentLocation.visibility = View.INVISIBLE
            showCloseButton()
            showTrackingURLLayout()
            setTitleText(getString(R.string.bottom_button_card_title_text))
            setDescriptionText(getString(R.string.bottom_button_card_description_text))
            setShareButtonText(getString(R.string.share_textview_text_share_link))
            showActionButton()
            showTitle()
        }
    }

    private fun getCurrentLocation(location: Location) {
        currentLocation = location
        currentLatLng = currentLocation?.longitude?.
                let {
                    currentLocation?.latitude?.
                            let { it1 -> LatLng(it1, it) }
                }
        currentLocation?.let { drawCurrentMaker(it) }

        drawMarkerWhenClickItemSearch()

        if (action == AppConstants.ACTION_CURRENT_LOCATION) {
            locationLatLng = currentLocation?.latitude?.
                    let {
                        currentLocation?.longitude?.
                                let { it1 -> LatLng(it, it1) }
                    }
            addDestinationMarker(locationLatLng)
            currentLatLng?.let {
                addDisposables(shareViewModel.getLocationName(this, it)
                        .observeOnUiThread()
                        .subscribe(this::getLocationName))
            }
            isConfirm = true
        }

        if (locationLatLng != null) {
            locationLatLng?.let { destinationLatLng = it }
        }
    }

    private fun setLocationNameWhenCameraMoving() {
        if (myLocation?.geometry?.location == null || action == AppConstants.ACTION_CHOOSE_ON_MAP) {
            locationLatLng?.let {
                addDisposables(shareViewModel.getLocationName(this, it)
                        .observeOnUiThread()
                        .subscribe(this::getLocationName))
            }
        } else {
            val lat = myLocation?.geometry?.location?.lat
            val lng = myLocation?.geometry?.location?.lng
            if (lat != null && lng != null) {
                locationLatLng = LatLng(lat, lng)
                locationLatLng?.let {
                    addDisposables(shareViewModel.getLocationName(this, it)
                            .observeOnUiThread()
                            .subscribe(this::getLocationName))
                }
            }
            isConfirm = true
        }
    }

    private fun handleShowBatteryCapacity(batteryCapacity: Int) {
        ui.trackingInfo.tvBattery.text = batteryCapacity.toString()
    }

    private fun handleGetCurrentDateTime(dateTime: String) {
        timeStart = dateTime
    }

    private fun handleTrackingUrlComplete(link: String) {
        ui.bottomCard.tvURL.text = link
        ui.bottomCard.hideProgress()
        initBottomButtonCard(true, action)
    }

    private fun handlerProgressTracking() {
        handleTrackingProgress = Observable.interval(SECOND_VALUE, TimeUnit.MILLISECONDS)
                .observeOnUiThread()
                .subscribe({
                    if (isArrived) {
                        etaUpdate = 0f
                        averageSpeed = 0f
                        currentMarker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_location))
                        currentMarker?.setAnchor(AppConstants.KEY_DEFAULT_ANCHOR, AppConstants.KEY_DEFAULT_ANCHOR)
                        updateCurrentTimeView()
                        setArrived()
                        deleteListTrackingLatLng()
                        handleTrackingProgress.dispose()
                    }
                    countTimer += SECOND_VALUE
                    requestLocation()
                })
    }

    private fun handleGetCurrentLocation(hyperTrackLocation: HyperTrackLocation) {
        updateUI(hyperTrackLocation)
    }

    private fun handleDistancePerSecond(list: List<Float>) {
        averageSpeed += list[0]
        distance += list[1]
    }

    private fun handleDrawCurrentMarker() {
        addDisposables(shareViewModel.getCurrentLocation(this)
                .observeOnUiThread()
                .subscribe(this::getCurrentLocation))
    }

    private fun handleTimeDuringStatus(stringTime: String) {
        time = stringTime
    }

    private fun handleCompareLocation(isArrived: Boolean) {
        this.isArrived = isArrived
    }

    private fun handleAngleMarker(float: Float) {
        angle = float
    }
}

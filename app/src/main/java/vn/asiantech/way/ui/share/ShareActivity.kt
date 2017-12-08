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
import vn.asiantech.way.data.model.Rows
import vn.asiantech.way.data.model.TrackingInformation
import vn.asiantech.way.data.model.WayLocation
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.custom.ArrivedDialog
import vn.asiantech.way.ui.custom.BottomButtonCard
import vn.asiantech.way.ui.custom.RadiusAnimation
import vn.asiantech.way.ui.custom.TrackingProgressInfo
import vn.asiantech.way.ui.search.SearchActivity
import vn.asiantech.way.utils.AppConstants
import vn.asiantech.way.utils.DateTimeUtil
import vn.asiantech.way.utils.LocationUtil
import java.util.*
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
        private const val ZINDEX_VALUE = 8f
        private const val POLYLINE_WIDTH = 20f
    }

    private lateinit var shareViewModel: ShareViewModel
    private lateinit var ui: ShareActivityUI
    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var countTimer: Disposable

    private var mCurrentMarker: Marker? = null
    private var mIsStopTracking = false
    private var mLocationUpdates: MutableList<LatLng>? = mutableListOf()
    private var mLocations: MutableList<TrackingInformation>? = mutableListOf()
    private var mCurrentLocation: Location? = null
    private var mCountTimer = SECOND_VALUE
    private lateinit var mDestinationLatLng: LatLng
    private var mCurrentLatLng: LatLng? = null
    private var mDistanceTravel = 0f
    private var mAverageSpeed = 0f
    private var mEtaUpdate = 0.0f
    private var mEtaMaximum = 0.0f
    private var mAngle = 0.0f
    private var distance = 0.0f
    private var mCurrentStatus = "STOP"
    private var mEndStatus = 0L
    private var mIsArrived: Boolean = false
    private var mIsSetETA: Boolean = false
    private var mEtaTime: String? = null
    private var mEtaDistance: String? = null
    private var mTimeStart: String? = null

    private var mTimeArrived: String? = null
    private var mStartPosition = 0
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mLocationRequest: LocationRequest
    private var mDestinationName: String? = null
    private var mLatLng: LatLng? = null
    private var mMyLocation: WayLocation? = null
    private var mAction: String? = null
    private var mIsReTracking: Boolean = false
    private var mIsConfirm: Boolean = false
    private var mIsStartTracking: Boolean = false
    private var mMarker: Marker? = null
    private var mGroundOverlay: GroundOverlay? = null
    private var mLine: Polyline? = null
    private var mLine1: Polyline? = null
    private var time: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ShareActivityUI()
        ui.setContentView(this)
        shareViewModel = ShareViewModel(this)
        supportMapFragment = SupportMapFragment()
        mLocationUpdates = ArrayList()
        mLocations = mutableListOf()
        initializeUIViews()
        initMap()
        configMap()
        initLocationRequest()
        initGoogleApiClient()
        mAction = intent.action
        initBottomButtonCard(true, mAction)
        setEnableRlSearchLocation()
    }

    private fun initMap() {
        supportFragmentManager.beginTransaction().replace(R.id.share_activity_map, supportMapFragment).commit()
    }

    private fun configMap() {
        supportMapFragment.getMapAsync { googleMap ->
            googleMap.setOnCameraIdleListener(this)
            mGoogleMap = googleMap
            mapEvent()
        }
    }

    private fun mapEvent() {
        mCurrentLocation = LocationUtil(this).getCurrentLocation()
        mCurrentLatLng = mCurrentLocation?.longitude?.let { mCurrentLocation?.latitude?.let { it1 -> LatLng(it1, it) } }
        mCurrentLocation?.let { drawCurrentMaker(it) }

        mGoogleMap.setOnCameraIdleListener(this)
        val lat = mMyLocation?.geometry?.location?.lat
        val lng = mMyLocation?.geometry?.location?.lng
        if (lat != null && lng != null) {
            mLatLng = LatLng(lat, lng)
            addDestinationMarker(mLatLng)
        }
        if (mAction == AppConstants.ACTION_CURRENT_LOCATION) {
            mLatLng = mCurrentLocation?.latitude?.let { mCurrentLocation?.longitude?.let { it1 -> LatLng(it, it1) } }
            addDestinationMarker(mLatLng)
            addDisposables(shareViewModel.getLocationName(mLatLng!!)
                    .observeOnUiThread()
                    .subscribe(this::getLocationName))
            mIsConfirm = true
        }
        if (mLatLng != null) {
            mLatLng?.let { mDestinationLatLng = it }
        }
        addDisposables(shareViewModel.getLocationName(mCurrentLatLng!!)
                .observeOnUiThread()
                .subscribe(this::getLocationName))
    }

    private fun initializeUIViews() {
        ui.bottomCard.onBottomCardListener = object : BottomButtonCard.OnBottomCardListener {
            override fun onCloseButtonClick() {
                ui.bottomCard.hideBottomCardLayout()
                ui.trackingInfo.showTrackingProgress()
            }

            override fun onActionButtonClick() {
                eventActionButtonClicked()
            }

            override fun onCopyButtonClick() {
                (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip =
                        ClipData.newPlainText("tracking_url", ui.bottomCard.tvURL.text)
            }
        }

        ui.trackingInfo.onTrackingInfoListener = object : TrackingProgressInfo.OnTrackingProgressListener {
            override fun onStopButtonClick() {
                confirmStopDialog()
            }

            override fun onShareButtonClick() {
                ui.bottomCard.showBottomCardLayout()
                ui.trackingInfo.hideTrackingProgress()
            }

            override fun onCallButtonClick() {
                // No-Op
            }

            override fun onSummaryButtonClick() {
                showDialog()
            }
        }
    }

    private fun eventActionButtonClicked() {
        when (mAction) {
            AppConstants.ACTION_CHOOSE_ON_MAP -> {
                mAction = AppConstants.ACTION_SEND_WAY_LOCATION
                initBottomButtonCard(true, mAction)
                mDestinationLatLng = mLatLng!!
                addDestinationMarker(mDestinationLatLng)
                mIsConfirm = true
            }
        // Click sharing
            AppConstants.ACTION_SEND_WAY_LOCATION, AppConstants.ACTION_CURRENT_LOCATION -> {
                mAction = null
                ui.bottomCard.startProgress()
                addDisposables(shareViewModel.getTrackingURL()
                        .observeOnUiThread()
                        .subscribe(this::handleTrackingUrlComplete))
                mIsStartTracking = true
                handlerProgressTracking()
            }
            else -> shareLocation()
        }
        setEnableRlSearchLocation()
    }

    private fun initBottomButtonCard(show: Boolean, action: String?) {
        when (action) {
            AppConstants.ACTION_CHOOSE_ON_MAP -> {
                ui.bottomCard.hideCloseButton()
                ui.bottomCard.hideTvTitle()
                ui.bottomCard.setDescriptionText(getString(R.string.confirm_move_map))
                ui.bottomCard.setShareButtonText(getString(R.string.confirm_location))
                ui.bottomCard.showActionButton()
            }

            AppConstants.ACTION_CURRENT_LOCATION, AppConstants.ACTION_SEND_WAY_LOCATION -> {
                ui.bottomCard.hideCloseButton()
                ui.bottomCard.hideTvDescription()
                ui.bottomCard.setTitleText(getString(R.string.share_textview_text_look_good))
                ui.bottomCard.setShareButtonText(getString(R.string.share_textview_text_start_sharing))
                ui.bottomCard.showActionButton()
            }

            else -> {
                ui.imgCurrentLocation.visibility = View.INVISIBLE
                ui.bottomCard.showCloseButton()
                ui.bottomCard.showTrackingURLLayout()
                ui.bottomCard.setTitleText(getString(R.string.bottom_button_card_title_text))
                ui.bottomCard.setDescriptionText(getString(R.string.bottom_button_card_description_text))
                ui.bottomCard.setShareButtonText(getString(R.string.share_textview_text_share_link))
                ui.bottomCard.showActionButton()
                ui.bottomCard.showTitle()
            }
        }
        if (show && !mIsArrived) {
            ui.bottomCard.showBottomCardLayout()
            ui.trackingInfo.hideTrackingProgress()
        }
    }

    override fun onBindViewModel() {
        addDisposables(shareViewModel.batteryCapacity
                .observeOnUiThread()
                .subscribe(this::handleShowBatteryCapacity),
                shareViewModel.getCalendarDateTime()
                        .observeOnUiThread()
                        .subscribe(this::handleGetCurrentDateTime),
                shareViewModel.result
                        .observeOnUiThread()
                        .subscribe(this::handleGetCurrentLocation))
    }

    override fun onLocationChanged(location: Location) {
        if (!mIsStartTracking) {
            if (mMarker != null && mGroundOverlay != null) {
                val latLng = LatLng(location.latitude, location.longitude)
                mMarker?.position = latLng
                mGroundOverlay?.position = latLng
            }
        }
    }

    override fun onCameraIdle() {
        mLatLng = mGoogleMap.cameraPosition?.target
        if (!mIsConfirm) {
            if (mMyLocation?.geometry?.location == null || mAction == AppConstants.ACTION_CHOOSE_ON_MAP) {
                addDisposables(shareViewModel.getLocationName(mLatLng!!)
                        .observeOnUiThread()
                        .subscribe(this::getLocationName))
            } else {
                val lat = mMyLocation?.geometry?.location?.lat
                val lng = mMyLocation?.geometry?.location?.lng
                if (lat != null && lng != null) {
                    mLatLng = LatLng(lat, lng)
                    addDisposables(shareViewModel.getLocationName(mLatLng!!)
                            .observeOnUiThread()
                            .subscribe(this::getLocationName))
                }
                mIsConfirm = true
            }
        }
    }

    internal fun eventRlSearchLocationClicked() {
        startActivity(Intent(this, SearchActivity::class.java))
    }

    internal fun eventButtonBackClicked() {
        onBackPressed()
    }

    private fun setEnableRlSearchLocation() {
        if (mAction != AppConstants.ACTION_CHOOSE_ON_MAP) {
            ui.rlSearchLocation.isEnabled = false
        }
    }

    // Get ETA Distance and duration
    private fun handleDistanceETA(row: List<Rows>) {
        ui.trackingInfo.tvTime.text = resources.getString(R.string.eta, row[0].elements[0]
                .duration.text)
        ui.trackingInfo.tvDistance.text = resources.getString(R.string.open_parentheses,
                row[0].elements[0].distance.text)
    }

    // Get list lat lng direction
    private fun handleListLocationWhenReOpen(list: List<LocationRoad>) {
    }

    // Show dialog when arrived
    private fun setArrived() {
        val handleStartAddress: (string: String) -> Unit = { ui.trackingInfo.tvStartAddress.text = it }
        val handleEndAddress: (string: String) -> Unit = { ui.trackingInfo.tvEndAddress.text = it }
        val handleStartTime: (string: String) -> Unit = { ui.trackingInfo.tvTime.text = it }
        val handleEndTime: (string: String) -> Unit = {
            ui.trackingInfo.tvEndTime.text = it
            mTimeArrived = it
        }
        ui.bottomCard.hideBottomCardLayout()
        ui.trackingInfo.showTrackingProgress()
        ui.trackingInfo.hideTrackingInfor()
        ui.trackingInfo.showArrivedInfor()
        ui.trackingInfo.circleProgressBar.progress = TYPE_PROGRESS_MAX
        ui.trackingInfo.circleProgressBar.circleProgressColor = resources.getColor(R.color
                .violet)
        ui.trackingInfo.tvStartTime.text = mTimeStart
        addDisposables(shareViewModel.getLocationName(mLatLng!!)
                .observeOnUiThread()
                .subscribe(handleStartAddress),
                shareViewModel.getLocationName(mDestinationLatLng)
                        .observeOnUiThread()
                        .subscribe(handleEndAddress),
                shareViewModel.getCalendarDateTime()
                        .observeOnUiThread()
                        .subscribe(handleEndTime),
                shareViewModel.getTimeDuringStatus(mCountTimer / 1000)
                        .observeOnUiThread()
                        .subscribe(handleStartTime))
        ui.trackingInfo.tvDistance.text = mEtaDistance
        ui.trackingInfo.imgBtnCall.imageResource = R.drawable.ic_reached
        showDialog()
    }

    private fun showDialog() {
        val dialog = ArrivedDialog.newInstance(mTimeArrived, mEtaDistance)
        val fragmentManager = supportFragmentManager
        dialog.show(fragmentManager, resources.getString(R.string.arrived_dialog_tag))
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
            mGoogleMap.addMarker(MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_expected_place_marker))
                    .title(mDestinationName)
                    .anchor(AppConstants.KEY_DEFAULT_ANCHOR, AppConstants.KEY_DEFAULT_ANCHOR))
                    ?.showInfoWindow()
            ui.imgPickLocation.visibility = View.INVISIBLE
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
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
        if (!mIsStartTracking) {
            mGoogleMap.clear()
            mMarker = mGoogleMap.addMarker(MarkerOptions()
                    .position(currentLocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_point))
                    .title(getString(R.string.current_location))
                    .anchor(AppConstants.KEY_DEFAULT_ANCHOR, AppConstants.KEY_DEFAULT_ANCHOR))
            addPulseRing(currentLocation)
            if (mAction == AppConstants.ACTION_CHOOSE_ON_MAP)
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                        .target(currentLocation)
                        .zoom(ZOOM_SIZE)
                        .build()), AppConstants.TIME_ZOOM_CAMERA, object : GoogleMap.CancelableCallback {
                    override fun onFinish() = Unit

                    override fun onCancel() = Unit
                })
        } else {
            mMarker?.remove()
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
        mGroundOverlay = mGoogleMap.addGroundOverlay(GroundOverlayOptions()
                .position(latLng, AppConstants.KEY_GROUND_OVERLAY_POSITION)
                .image(BitmapDescriptorFactory.fromBitmap(bitmap)))
        val groundAnimation = RadiusAnimation(mGroundOverlay)
        groundAnimation.repeatCount = Animation.INFINITE
        groundAnimation.duration = AppConstants.KEY_GR_ANIMATION_DUR
        supportMapFragment.view?.startAnimation(groundAnimation)
    }

    private fun getLocationName(locationName: String) {
        ui.tvLocation.text = locationName
    }

    private fun initLocationRequest() {
        mLocationRequest = LocationRequest.create()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = ShareActivity.INTERVAL
        mLocationRequest.fastestInterval = ShareActivity.INTERVAL
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
                        .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
            }
        }
    }

    override fun onConnectionSuspended(p0: Int) = Unit

    private fun initGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build()
    }

    private fun confirmStopDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.share_textview_text_dialog_title))
        builder.setMessage(resources.getString(R.string.share_textview_text_dialog_message))
        builder.setCancelable(true)
        builder.setNegativeButton("No") { dialog, _ -> dialog?.cancel() }
        builder.setPositiveButton("Stop") { _, _ ->
            mIsStopTracking = true
            countTimer.dispose()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun requestLocation() {
        shareViewModel.getCurrentLocation()
    }

    private fun updateCurrentTimeView() {
        ui.trackingInfo.tvSpeed.text = String.format("%.2f", mAverageSpeed).plus(" km/h")
        ui.trackingInfo.circleProgressBar.progress = AppConstants.PROGRESS_BAR_MAX
    }

    private fun updateUI(hyperTrackLocation: HyperTrackLocation) {
        val latLng = hyperTrackLocation.geoJSONLocation.latLng
        mCurrentLatLng = latLng
        // get location distance estimate
        addDisposables(shareViewModel
                .getLocationDistance("${latLng.latitude},${latLng.longitude}",
                        "${mDestinationLatLng.latitude},${mDestinationLatLng.longitude}")
                .observeOnUiThread()
                .subscribe(this::handleDistanceETA),
                shareViewModel.compareLocation(latLng,
                        mDestinationLatLng)
                        .observeOnUiThread()
                        .subscribe(this::handleCompareLocation),
                shareViewModel.getCalendarDateTime()
                        .observeOnUiThread()
                        .subscribe(this::handleGetCurrentDateTime))

        if (mLocationUpdates != null) {
            if (latLng != null) {
                if (mIsReTracking && mLatLng != latLng) {
                    mIsReTracking = false
                } else {
                    mLocationUpdates?.add(latLng)
                }
            }

            if (mLocationUpdates!!.size > 1) {
                getListLocation(latLng, mLocationUpdates!!.size - 1)
            }
            updateView()
            updateMapView(latLng)
        }
    }

    private fun updateView() {
        if (!mIsStopTracking) {
            ui.trackingInfo.tvActionStatus.text = resources.getString(R.string.leaving)
        }
        ui.trackingInfo.tvSpeed.text = String.format("%.2f", mAverageSpeed).plus(" km/h")
        ui.trackingInfo.tvElapsedTime.text = formatInterval(mCountTimer)
        ui.trackingInfo.tvDistanceTravelled.text = String.format("%.2f", mDistanceTravel).plus(" km")
        if (mEtaMaximum - mEtaUpdate > STEP_ETA) {
            ui.trackingInfo.circleProgressBar.progress = (mEtaMaximum - (mEtaUpdate /
                    mEtaMaximum) * AppConstants.PROGRESS_BAR_MAX).toInt()
        }
        drawLine()
    }

    private fun formatInterval(millis: Long): String = String.format("%02d:%02d:%02d"
            , TimeUnit.MILLISECONDS.toHours(millis)
            , TimeUnit.MILLISECONDS.toMinutes(millis)
            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))
            , TimeUnit.MILLISECONDS.toSeconds(millis)
            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))


    private fun handleShowBatteryCapacity(batteryCapacity: Int) {
        ui.trackingInfo.tvBattery.text = batteryCapacity.toString()
    }

    private fun handleGetCurrentDateTime(dateTime: String) {
        mTimeStart = dateTime
    }

    private fun handleTrackingUrlComplete(link: String) {
        ui.bottomCard.tvURL.text = link
        ui.bottomCard.hideProgress()
        initBottomButtonCard(true, mAction)
    }

    private fun handlerProgressTracking() {
        countTimer = Observable.interval(SECOND_VALUE, TimeUnit.MILLISECONDS)
                .observeOnUiThread()
                .subscribe({
                    if (mLocationUpdates != null) {
                        if (mIsArrived) {
                            mEtaUpdate = 0f
                            mAverageSpeed = 0f
                            mCurrentMarker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_location))
                            mCurrentMarker?.setAnchor(AppConstants.KEY_DEFAULT_ANCHOR, AppConstants.KEY_DEFAULT_ANCHOR)
                            updateCurrentTimeView()
                            setArrived()
                            deleteListTrackingLatLng()
                            countTimer.dispose()
                        }
                        mCountTimer += SECOND_VALUE
                        requestLocation()
                    }
                })
    }

    private fun handleGetCurrentLocation(hyperTrackLocation: HyperTrackLocation) {
        updateUI(hyperTrackLocation)
    }

    private fun handleDistancePerSecond(list: List<Float>) {
        mAverageSpeed += list[0]
        distance += list[1]
    }

    private fun drawLine() {
        if (mLocationUpdates != null) {
            if (mLocationUpdates!!.size > 1) {
                val option = PolylineOptions()
                        .width(POLYLINE_WIDTH)
                        .geodesic(true)
                        .color(Color.parseColor("#1976D2"))
                        .zIndex(ZINDEX_VALUE)
                        .startCap(RoundCap())
                        .endCap(RoundCap())
                        .jointType(JointType.BEVEL)
                        .addAll(mLocationUpdates)
                val option1 = PolylineOptions()
                        .width(ZOOM_SIZE)
                        .geodesic(true)
                        .color(Color.parseColor("#2196F3"))
                        .zIndex(ZINDEX_VALUE)
                        .startCap(RoundCap())
                        .endCap(RoundCap())
                        .jointType(JointType.BEVEL)
                        .addAll(mLocationUpdates)
                mLine?.remove()
                mLine = mGoogleMap.addPolyline(option)
                mLine?.pattern = null
                mLine1?.remove()
                mLine1 = mGoogleMap.addPolyline(option1)
                mLine1?.pattern = null
            }
        }
    }

    private fun updateMapView(latLng: LatLng) {
        if (mLocationUpdates!!.size > 1) {
            addDisposables(shareViewModel.getAngleMarker(mLocationUpdates!![mLocationUpdates!!.size - 2], mLocationUpdates!![mLocationUpdates!!.size - 1])
                    .observeOnUiThread()
                    .subscribe(this::handleAngleMarker))
        }
        if (mCurrentMarker == null) {
            mCurrentMarker = mGoogleMap.addMarker(MarkerOptions().
                    position(latLng).
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_hero_marker))
                    .anchor(AppConstants.KEY_DEFAULT_ANCHOR, AppConstants.KEY_DEFAULT_ANCHOR)
                    .flat(true))
        } else {
            mCurrentMarker!!.position = latLng
            mCurrentMarker!!.rotation = mAngle
        }
        MarkerAnimation.animateMarker(mCurrentMarker, latLng)
    }

    private fun checkStatus(speed: Float): String {
        return if (speed <= AppConstants.MIN_SPEED) {
            "STOP"
        } else if (speed > AppConstants.MIN_SPEED && speed <= AppConstants.MAX_SPEED) {
            "MOVING"
        } else "DRIVER"
    }

    private fun getListLocation(latLng: LatLng, position: Int) {
        val status = checkStatus(mAverageSpeed)
        var description: String? = null
        val handleDescription: (string: String) -> Unit = { description = it }
        if (mCurrentStatus != status) {
            if (status == "STOP") {
                addDisposables(shareViewModel.getLocationName(latLng)
                        .observeOnUiThread()
                        .subscribe(handleDescription))
            } else {
                // Time during status
                addDisposables(shareViewModel.getTimeDuringStatus((mCountTimer - mEndStatus) /
                        AppConstants.TIME_ZOOM_CAMERA)
                        .observeOnUiThread()
                        .subscribe(this::handleTimeDuringStatus))
                for (i in 0..(position - 1)) {
                    addDisposables(shareViewModel
                            .getDistancePerSecond(mLocationUpdates!![i], mLocationUpdates!![i + 1])
                            .observeOnUiThread()
                            .subscribe(this::handleDistancePerSecond))
                }
                mEndStatus = mCountTimer
                description = time.plus(" | ").plus(distance.toString()).plus("km")
            }
        }
        val location = TrackingInformation(DateTimeUtil().getTimeChangeStatus(),
                status, description, latLng)
        mLocations?.add(location)
        mCurrentStatus = status
        mStartPosition = position
    }

    private fun deleteListTrackingLatLng() {
        mLocationUpdates?.clear()
    }

    private fun handleTimeDuringStatus(stringTime: String) {
        time = stringTime
    }

    private fun handleCompareLocation(isArrived: Boolean) {
        mIsArrived = isArrived
    }

    private fun handleAngleMarker(float: Float) {
        mAngle = float
    }

    override fun onResume() {
        super.onResume()
        mGoogleApiClient.connect()
    }

    override fun onPause() {
        super.onPause()
        mGoogleApiClient.disconnect()
    }
}

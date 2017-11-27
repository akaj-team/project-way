package vn.asiantech.way.ui.share

import android.annotation.SuppressLint
import android.content.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.view.animation.Animation
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.hypertrack.lib.HyperTrack
import com.hypertrack.lib.HyperTrackUtils
import com.hypertrack.lib.callbacks.HyperTrackCallback
import com.hypertrack.lib.internal.consumer.view.MarkerAnimation
import com.hypertrack.lib.models.*
import kotlinx.android.synthetic.main.activity_share_location.*
import kotlinx.android.synthetic.main.bottom_button_card_view.view.*
import kotlinx.android.synthetic.main.tracking_progress_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.asiantech.way.R
import vn.asiantech.way.data.model.TrackingInformation
import vn.asiantech.way.data.model.search.MyLocation
import vn.asiantech.way.data.model.share.ResultDistance
import vn.asiantech.way.data.model.share.ResultRoad
import vn.asiantech.way.data.remote.APIUtil
import vn.asiantech.way.data.remote.RoadClient
import vn.asiantech.way.ui.arrived.DialogArrived
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.confirm.LocationNameAsyncTask
import vn.asiantech.way.ui.custom.BottomButtonCard
import vn.asiantech.way.ui.custom.RadiusAnimation
import vn.asiantech.way.ui.custom.TrackingProgressInfo
import vn.asiantech.way.ui.search.SearchLocationActivity
import vn.asiantech.way.ui.update.UpdateMap
import vn.asiantech.way.utils.AppConstants
import vn.asiantech.way.utils.DateTimeUtil
import vn.asiantech.way.utils.LocationUtil
import vn.asiantech.way.utils.Preference
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 27/09/2017.
 */
class ShareLocationActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener, LocationListener, GoogleApiClient.ConnectionCallbacks {

    companion object {
        private const val TYPE_PROGRESS_MAX = 100
        private const val INTERVAL = 500L
        private const val ZOOM_SIZE = 16f
        private const val TIME_CONVERT = 3.6
        private const val SECOND_VALUE = 1000L
        private const val RADIUS = 360.0
        private const val STEP_ETA = 3
    }

    private var mCurrentMarker: Marker? = null
    private var mIsStopTracking = false
    private var mLocationUpdates: MutableList<LatLng>? = mutableListOf()
    private var mLocations: MutableList<TrackingInformation>? = mutableListOf()
    private var mCurrentLocation: Location? = null
    private lateinit var mHandlerTracking: Handler
    private var mRunnable: Runnable? = null
    private var mCountTimer = SECOND_VALUE
    private lateinit var mDestinationLatLng: LatLng
    private var mCurrentLatLng: LatLng? = null
    private var mDistanceTravel = 0f
    private var mAverageSpeed = 0f
    private var mEtaUpdate = 0.0f
    private var mEtaMaximum = 0.0f
    private var mAngle = 0.0f
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
    private lateinit var mMapFragment: SupportMapFragment
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mLocationRequest: LocationRequest
    private var mDestinationName: String? = null
    private var mLatLng: LatLng? = null
    private var mMyLocation: MyLocation? = null
    private var mAction: String? = null
    private var mIsReTracking: Boolean = false
    private var mIsConfirm: Boolean = false
    private var mIsStartTracking: Boolean = false
    private var mMarker: Marker? = null
    private var mGroundOverlay: GroundOverlay? = null
    private var mLine: Polyline? = null
    private var mLine1: Polyline? = null
    private lateinit var mLocationAsyncTask: AsyncTask<LatLng, Void, String>

    private val mCurrentBatteryReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(p0: Context?, p1: Intent?) {
            val level = p1?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            tvBattery.text = "${level.toString()}%"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_location)
        if (intent.extras != null) {
            mMyLocation = intent.getParcelableExtra(AppConstants.KEY_LOCATION)
        }
        Preference.init(this)
        mAction = Preference().getActionType()
        registerReceiver(mCurrentBatteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        mLocationUpdates = ArrayList()
        mLocations = mutableListOf()
        initMap()
        initializeUIViews()
        initGoogleApiClient()
        initLocationRequest()
        onClickButtonSearchLocation()
        initArrivedDialog()
        initBottomButtonCard(true, mAction)
    }

    private fun initMap() {
        mMapFragment = supportFragmentManager.findFragmentById(R.id.fgMap) as SupportMapFragment
        mMapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        if (map != null) {
            mGoogleMap = map
            mGoogleMap.setOnCameraIdleListener(this)
        }

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
        if (mAction == AppConstants.KEY_CURRENT_LOCATION) {
            mLatLng = mCurrentLocation?.latitude?.let { LatLng(it, mCurrentLocation!!.longitude) }
            addDestinationMarker(mLatLng)
            getLocationName(mLatLng)
            mIsConfirm = true
        }
        if (mLatLng != null) {
            mDestinationLatLng = mLatLng!!
            Preference().setDestinationLatLng(mDestinationLatLng)
        }

        mTimeStart = getCalendarDateTime()
        // Continue tracking
        if (mAction == AppConstants.KEY_START_TRACKING) {
            mLatLng = Preference().getCurrentLatLng()
            mLocationUpdates = Preference().getListLocationLatLng()
            mDestinationLatLng = Preference().getDestinationLatLng()
            addDestinationMarker(mDestinationLatLng)
            handlerProgressTracking()
            getLocationName(mDestinationLatLng)
            Preference().getCountTimer()?.let { mCountTimer = it }
            mIsConfirm = true
            mIsReTracking = true
            mIsStartTracking = true
        }
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
            if (mMyLocation?.geometry?.location == null || mAction == AppConstants.KEY_CONFIRM_LOCATION) {
                mLocationAsyncTask = LocationNameAsyncTask(WeakReference(this)).execute(mLatLng)
            } else {
                val lat = mMyLocation?.geometry?.location?.lat
                val lng = mMyLocation?.geometry?.location?.lng
                if (lat != null && lng != null) {
                    mLatLng = LatLng(lat, lng)
                    mLocationAsyncTask = LocationNameAsyncTask(WeakReference(this)).execute(mLatLng)
                }
                mIsConfirm = true
            }
        }
    }

    private fun initializeUIViews() {
        bottomButtonCard?.onBottomCardListener = object : BottomButtonCard.OnBottomCardListener {
            override fun onCloseButtonClick() {
                bottomButtonCard?.hideBottomCardLayout()
                trackingProgressIno?.showTrackingProgress()
            }

            override fun onActionButtonClick() {
                when (mAction) {
                    AppConstants.KEY_CONFIRM_LOCATION -> {
                        Preference().setActionType(AppConstants.KEY_CHOOSE_LOCATION)
                        mAction = Preference().getActionType()
                        initBottomButtonCard(true, mAction)
                        mDestinationLatLng = mLatLng!!
                        addDestinationMarker(mDestinationLatLng)
                        mIsConfirm = true
                        Preference().setDestinationLatLng(mDestinationLatLng)
                    }
                // Click sharing
                    AppConstants.KEY_CHOOSE_LOCATION, AppConstants.KEY_CURRENT_LOCATION -> {
                        Preference().setActionType(AppConstants.KEY_START_TRACKING)
                        mAction = Preference().getActionType()
                        bottomButtonCard.startProgress()
                        getTrackingURL()
                        mIsStartTracking = true
                        drawCurrentMaker(mCurrentLocation!!)
                        handlerProgressTracking()
                    }
                    else -> shareLocation()
                }
                onClickButtonSearchLocation()
            }

            override fun onCopyButtonClick() {
                (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip =
                        ClipData.newPlainText("tracking_url", bottomButtonCard.tvURL.text)
            }
        }
        trackingProgressIno?.trackingProgressClick = object : TrackingProgressInfo.TrackingProgressClick {
            override fun onStopButtonClick() {
                if (rippleTrackingToggle.tag == "stop") {
                    confirmStopDialog()
                }
            }

            override fun onShareButtonClick() {
                bottomButtonCard?.showBottomCardLayout()
                trackingProgressIno?.hideTrackingProgress()
            }

            override fun onCallButtonClick() {
                // No-op
            }
        }
    }

    private fun confirmStopDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.share_textview_text_dialog_title))
        builder.setMessage(resources.getString(R.string.share_textview_text_dialog_message))
        builder.setCancelable(true)
        builder.setNegativeButton("No") { dialog, _ -> dialog?.cancel() }
        builder.setPositiveButton("Stop") { _, _ ->
            mIsStopTracking = true
            mHandlerTracking.removeCallbacks(mRunnable)
            Preference().setActionType(AppConstants.KEY_FINISH_TRACKING)
            deleteListTrackingLatLng()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun getTrackingURL() {
        val builder = ActionParamsBuilder()
        HyperTrack.createAndAssignAction(builder.build(), object : HyperTrackCallback() {
            override fun onSuccess(response: SuccessResponse) {
                if (response.responseObject != null) {
                    val action = response.responseObject as? Action
                    HyperTrack.clearServiceNotificationParams()
                    bottomButtonCard.tvURL.text = action?.trackingURL
                    bottomButtonCard.hideProgress()
                    initBottomButtonCard(true, mAction)
                }
            }

            override fun onError(errorResponse: ErrorResponse) {
                AlertDialog.Builder(this@ShareLocationActivity)
                        .setTitle(getString(R.string.dialog_title_error))
                        .setMessage(errorResponse.errorMessage)
                        .setPositiveButton(getString(R.string.dialog_button_ok)) { dialogInterface, _ ->
                            bottomButtonCard.hideProgress()
                            bottomButtonCard?.setShareButtonText(getString(R.string
                                    .share_textview_text_start_sharing))
                            dialogInterface.dismiss()
                        }
            }
        })
    }

    private fun initBottomButtonCard(show: Boolean, action: String?) {
        when (action) {
            AppConstants.KEY_CONFIRM_LOCATION -> {
                bottomButtonCard?.hideCloseButton()
                bottomButtonCard?.hideTvTitle()
                bottomButtonCard?.setDescriptionText(getString(R.string.confirm_move_map))
                bottomButtonCard?.setShareButtonText(getString(R.string.confirm_location))
                bottomButtonCard?.showActionButton()
            }
            AppConstants.KEY_CHOOSE_LOCATION, AppConstants.KEY_CURRENT_LOCATION -> {
                bottomButtonCard?.hideCloseButton()
                bottomButtonCard?.hideTvDescription()
                bottomButtonCard?.setTitleText(getString(R.string.share_textview_text_look_good))
                bottomButtonCard?.setShareButtonText(getString(R.string.share_textview_text_start_sharing))
                bottomButtonCard?.showActionButton()
            }
            else -> {
                imgPickLocation.visibility = View.INVISIBLE
                bottomButtonCard?.showCloseButton()
                bottomButtonCard?.actionType = BottomButtonCard.ActionType.SHARE_TRACKING_URL
                bottomButtonCard?.showTrackingURLLayout()
                bottomButtonCard?.setTitleText(getString(R.string.bottom_button_card_title_text))
                bottomButtonCard?.setDescriptionText(getString(R.string.bottom_button_card_description_text))
                bottomButtonCard?.setShareButtonText(getString(R.string.share_textview_text_share_link))
                bottomButtonCard?.showActionButton()
                bottomButtonCard?.showTitle()
            }
        }
        if (show && !mIsArrived) {
            bottomButtonCard?.showBottomCardLayout()
            trackingProgressIno?.hideTrackingProgress()
        }
    }

    private fun shareLocation() {
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        val message = "My Location is ${bottomButtonCard.tvURL.text}"
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message)
        startActivityForResult(Intent.createChooser(sharingIntent, "Share via"), 200)
    }

    /**
     * Set destination name
     */
    fun setDestinationName(name: String) {
        mDestinationName = name
    }

    private fun onClickButtonSearchLocation() {
        if (mAction != AppConstants.KEY_CONFIRM_LOCATION) {
            rlSearchLocation.isEnabled = false
        }
        rlSearchLocation.setOnClickListener {
            startActivity(Intent(this, SearchLocationActivity::class.java))
        }
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun addDestinationMarker(latLng: LatLng?) {
        if (latLng != null) {
            mGoogleMap.addMarker(MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_expected_place_marker))
                    .title(mDestinationName)
                    .anchor(AppConstants.KEY_DEFAULT_ANCHOR, AppConstants.KEY_DEFAULT_ANCHOR))
                    ?.showInfoWindow()
            imgPickLocation.visibility = View.INVISIBLE
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                    .target(latLng)
                    .zoom(ZOOM_SIZE)
                    .build()),1000, object : GoogleMap.CancelableCallback{
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
                    .anchor(0.5f, 0.5f))
            addPulseRing(currentLocation)
            if (mAction == AppConstants.KEY_CONFIRM_LOCATION)
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                        .target(currentLocation)
                        .zoom(ZOOM_SIZE)
                        .build()),1000, object : GoogleMap.CancelableCallback{
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
        mMapFragment.view?.startAnimation(groundAnimation)
    }

    private fun getLocationName(latLng: LatLng?) {
        if (latLng != null) {
            val geoCoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address> = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                tvLocation.text = address.getAddressLine(0)
                mDestinationName = if (!address.subThoroughfare.isNullOrEmpty()) {
                    address.subThoroughfare.plus(" ").plus(address.thoroughfare)
                } else {
                    address.thoroughfare
                }
            } else {
                tvLocation.text = null
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mGoogleApiClient.connect()
    }

    override fun onPause() {
        super.onPause()
        mGoogleApiClient.disconnect()
        if (mAction == AppConstants.KEY_CONFIRM_LOCATION && !mLocationAsyncTask.isCancelled) {
            mLocationAsyncTask.cancel(true)
        }
    }

    override fun onStop() {
        super.onStop()
        if (mAction == AppConstants.KEY_CONFIRM_LOCATION && !mLocationAsyncTask.isCancelled) {
            mLocationAsyncTask.cancel(true)
        }
    }

    private fun initGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build()
    }

    private fun initLocationRequest() {
        mLocationRequest = LocationRequest.create()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = ShareLocationActivity.INTERVAL
        mLocationRequest.fastestInterval = ShareLocationActivity.INTERVAL
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

    override fun onConnectionSuspended(p0: Int) {
        //No op
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (mAction != AppConstants.KEY_START_TRACKING) {
            mAction = AppConstants.KEY_FINISH_TRACKING
        }
        startActivity(Intent(this, UpdateMap::class.java))
        this.finish()
    }

    private fun handlerProgressTracking() {
        mHandlerTracking = Handler()
        mRunnable = Runnable {
            if (mLocationUpdates != null) {
                if (mIsArrived) {
                    Preference().setActionType(AppConstants.KEY_FINISH_TRACKING)
                    mEtaUpdate = 0f
                    mAverageSpeed = 0f
                    mCurrentMarker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_location))
                    mCurrentMarker?.setAnchor(0.5f, 0.5f)
                    updateCurrentTimeView()
                    setArrived()
                    deleteListTrackingLatLng()
                    return@Runnable
                }
                mCountTimer += SECOND_VALUE
                Preference().saveCountTimer(mCountTimer)
                requestLocation()
                handlerProgressTracking()
            }
        }
        mHandlerTracking.postDelayed(mRunnable, SECOND_VALUE)
    }

    private fun requestLocation() {
        HyperTrack.getCurrentLocation(object : HyperTrackCallback() {
            override fun onSuccess(p0: SuccessResponse) {
                val hyperTrackLocation = HyperTrackLocation((p0.responseObject) as Location?)
                updateUI(hyperTrackLocation)
            }

            override fun onError(p0: ErrorResponse) {
                Log.d("at-dinhvo", "ErrorResponse: " + p0.errorMessage)
            }
        })
    }

    private fun updateCurrentTimeView() {
        tvSpeed.text = String.format("%.2f", mAverageSpeed).plus(" km/h")
        circleProgressBar.progress = 100
    }

    private fun updateUI(hyperTrackLocation: HyperTrackLocation) {
        val latLng = hyperTrackLocation.geoJSONLocation.latLng
        latLng?.let { Preference().setCurrentLatLng(it) }
        mCurrentLatLng = latLng
        getLocationDistanceEstimates(latLng, mDestinationLatLng)
        mIsArrived = compareLocation(latLng, mDestinationLatLng)
        mTimeArrived = getCalendarDateTime()
        if (mLocationUpdates != null) {
            if (latLng != null) {
                if (mIsReTracking && mLatLng != latLng) {
                    mLatLng?.let { getLocationWhenAppNotWork(it, latLng) }
                    mIsReTracking = false
                } else {
                    mLocationUpdates?.add(latLng)
                }
                mLocationUpdates?.let { Preference().saveListLocationLatLng(it) }
            }
            if (mLocationUpdates!!.size > 1) {
                getListLocation(latLng, mLocationUpdates!!.size - 1)
                mDistanceTravel += getDistancePerSecond(mLocationUpdates!![mLocationUpdates!!.size - 2]
                        , mLocationUpdates!![mLocationUpdates!!.size - 1])
            }
            updateView()
            updateMapView(latLng)
        }
    }

    private fun updateView() {
        if (!mIsStopTracking) {
            tvActionStatus.text = resources.getString(R.string.leaving)
        }
        tvSpeed.text = String.format("%.2f", mAverageSpeed).plus(" km/h")
        tvElapsedTime.text = formatInterval(mCountTimer)
        tvDistanceTravelled.text = String.format("%.2f", mDistanceTravel).plus(" km")
        if (mEtaMaximum - mEtaUpdate > STEP_ETA) {
            circleProgressBar.progress = (mEtaMaximum - (mEtaUpdate / mEtaMaximum) * 100).toInt()
        }
        drawLine()
    }

    private fun formatInterval(millis: Long): String = String.format("%02d:%02d:%02d"
            , TimeUnit.MILLISECONDS.toHours(millis)
            , TimeUnit.MILLISECONDS.toMinutes(millis)
            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))
            , TimeUnit.MILLISECONDS.toSeconds(millis)
            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))

    private fun radians(n: Double) = n * (Math.PI / (RADIUS / 2))

    private fun degrees(n: Double) = n * ((RADIUS / 2) / Math.PI)

    private fun getAngleMarker(startLatLong: LatLng, endLatLong: LatLng): Double {
        val startLat = radians(startLatLong.latitude)
        val startLong = radians(startLatLong.longitude)
        val endLat = radians(endLatLong.latitude)
        val endLong = radians(endLatLong.longitude)
        var deltaLong = endLong - startLong
        val deltaPhi = Math.log(Math.tan(endLat / 2.0 + Math.PI / 4.0) / Math.tan(startLat / 2.0 + Math.PI / 4.0))
        if (Math.abs(deltaLong) > Math.PI) {
            deltaLong = if (deltaLong > 0.0) {
                -(2.0 * Math.PI - deltaLong)
            } else {
                (2.0 * Math.PI + deltaLong)
            }
        }
        return (degrees(Math.atan2(deltaLong, deltaPhi)) + RADIUS) % RADIUS
    }

    private fun getDistancePerSecond(source: LatLng, destination: LatLng): Float {
        val des = Location("Point")
        des.latitude = source.latitude
        des.longitude = source.longitude
        val src = Location("Point")
        src.latitude = destination.latitude
        src.longitude = destination.longitude
        mAverageSpeed = (src.distanceTo(des) * TIME_CONVERT).toFloat()
        return src.distanceTo(des) / SECOND_VALUE
    }

    //todo will update polyline later
    private fun drawLine() {
        if (mLocationUpdates != null) {
            if (mLocationUpdates!!.size > 1) {
                val option = PolylineOptions()
                        .width(20f)
                        .geodesic(true)
                        .color(Color.parseColor("#1976D2"))
                        .zIndex(8f)
                        .startCap(RoundCap())
                        .endCap(RoundCap())
                        .jointType(JointType.BEVEL)
                        .addAll(mLocationUpdates)
                val option1 = PolylineOptions()
                        .width(16f)
                        .geodesic(true)
                        .color(Color.parseColor("#2196F3"))
                        .zIndex(8f)
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
            mAngle = getAngleMarker(mLocationUpdates!![mLocationUpdates!!.size - 2], mLocationUpdates!![mLocationUpdates!!.size - 1]).toFloat()
        }
        if (mCurrentMarker == null) {
            mCurrentMarker = mGoogleMap.addMarker(MarkerOptions().
                    position(latLng).
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_hero_marker))
                    .anchor(0.5f, 0.5f)
                    .flat(true))
        } else {
            mCurrentMarker!!.position = latLng
            mCurrentMarker!!.rotation = mAngle
        }
        MarkerAnimation.animateMarker(mCurrentMarker, latLng)
    }

    private fun checkStatus(speed: Float): String {
        return if (speed <= 0.2) {
            "STOP"
        } else if (speed > 0.2 && speed <= 15.5) {
            "MOVING"
        } else "DRIVER"
    }

    private fun getTimeDuringStatus(time: Long): String {
        return when {
            time in 60..3599 -> (time / 60).toString().plus("min")
            time < 60 -> time.toString().plus("second")
            else -> (time / 3600).toString().plus("hour")
        }
    }

    private fun getListLocation(latLng: LatLng, position: Int) {
        val status = checkStatus(mAverageSpeed)
        if (mCurrentStatus != status) {
            var description: String? = null
            if (status == "STOP") {
                val geoCoder = Geocoder(this, Locale.getDefault())
                val addresses: List<Address> = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (addresses.isNotEmpty()) {
                    val address: Address = addresses[0]
                    description = address.getAddressLine(0)
                }
            } else {
                val time = getTimeDuringStatus((mCountTimer - mEndStatus) / 1000)
                val distance = (0..(position - 1))
                        .map {
                            getDistancePerSecond(mLocationUpdates!![it]
                                    , mLocationUpdates!![it + 1])
                        }
                        .sum()
                mEndStatus = mCountTimer
                description = time.plus(" | ").plus(distance.toString()).plus("km")
            }
            val location = TrackingInformation(DateTimeUtil().getTimeChangeStatus(),
                    status, description, latLng)
            with(Preference()) {
                saveTrackingHistory(location)
                mLocations = getTrackingHistory()
            }
            mCurrentStatus = status
            mStartPosition = position
        }
    }

    private fun getLocationDesciption(latLng: LatLng): String? {
        val geoCoder = Geocoder(this, Locale.getDefault())
        var description: String? = null
        val addresses: List<Address> = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (addresses.isNotEmpty()) {
            val address: Address = addresses[0]
            description = address.getAddressLine(0)
        }
        return description
    }

    private fun getLocationDistanceEstimates(currentLatLng: LatLng, destinationLatLng: LatLng) {
        val apiService = APIUtil.getService()
        apiService?.getLocationDistance("metric", convertLatLngToString(currentLatLng), convertLatLngToString(destinationLatLng))
                ?.enqueue(object : Callback<ResultDistance> {
                    override fun onResponse(call: Call<ResultDistance>?, response: Response<ResultDistance>?) {
                        val result = response?.body()
                        if (result != null) {
                            Log.d("zxc", "result" + result)
                            val element = result.rows[0].elements[0]
                            tvTime.text = resources.getString(R.string.eta, element.duration.text)
                            tvDistance.text = resources.getString(R.string.open_parentheses, element.distance.text)
                            if (!mIsSetETA) {
                                mEtaTime = element.duration.text
                                mEtaDistance = element.distance.text
//                                tvTimeTotalArrived.text = mEtaTime
//                                tvDistanceArrived.text = mEtaDistance
                                mIsSetETA = true
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResultDistance>?, t: Throwable?) {
                        Log.d("zxc", "fail " + t)
                    }

                })
    }

    private fun getLocationWhenAppNotWork(currentLatLng: LatLng, destinationLatLng: LatLng) {
        val path = "${currentLatLng.latitude},${currentLatLng.longitude}|${destinationLatLng.latitude},${destinationLatLng.longitude}"
        val apiService = RoadClient.getService()
        apiService?.getListLocation(path, "AIzaSyCZc4PAEpeVC18QnS5fPBt5hk3EFMbFjj8")
                ?.enqueue(object : Callback<ResultRoad> {
                    override fun onResponse(call: Call<ResultRoad>?, response: Response<ResultRoad>?) {
                        val result = response?.body()
                        Log.d("zxc", "aa " + result)
                        if (result != null) {
                            Log.d("zxc", "aa " + result)
                            result.snappedPoints.forEach {
                                mLocationUpdates?.add(LatLng(it.location.latitude, it.location.longitude))
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResultRoad>?, t: Throwable?) {
                        Log.d("zxc", "fail " + t)
                    }

                })
    }

    private fun deleteListTrackingLatLng() {
        mLocationUpdates?.clear()
        mLocationUpdates?.let { Preference().saveListLocationLatLng(it) }
    }

    private fun convertLatLngToString(latLng: LatLng?): String {
        return "${latLng?.latitude.toString()},${latLng?.longitude.toString()}"
    }

    private fun compareLocation(currentLatLng: LatLng, destinationLatLng: LatLng): Boolean {
        val currentLat = String.format("%.3f", currentLatLng.latitude)
        val currentLng = String.format("%.3f", currentLatLng.longitude)
        val destinationLat = String.format("%.3f", destinationLatLng.latitude)
        val destinationLng = String.format("%.3f", destinationLatLng.longitude)
        return currentLat == destinationLat && currentLng == destinationLng
    }

    private fun initArrivedDialog() {
        Preference().getTrackingHistory()?.let { mLocations?.addAll(it) }
        btnShowSummary.setOnClickListener {
            showDialog()
        }

        imgArrowRightStartItem.setOnClickListener {
            setArrowRightStartItemClick()
        }

        imgArrowRightEndItem.setOnClickListener {
            setArrowRightEndItemClick()
        }

        imgArrowDropDownStartItem.setOnClickListener {
            setArrowDropDownStartItemClick()
        }

        imgArrowDropDownEndItem.setOnClickListener {
            setArrowDropDownEndItemClick()
        }

        //todo check button even
        imgBtnResetPosition.setOnClickListener {
            mCurrentLatLng?.let {
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, ZOOM_SIZE))
            }
        }
    }

    // Show dialog when arrived
    private fun setArrived() {
        bottomButtonCard?.hideBottomCardLayout()
        trackingProgressIno?.showTrackingProgress()
        trackingProgressIno?.hideTrackingInfor()
        trackingProgressIno?.showArrivedInfor()
        circleProgressBar.progress = TYPE_PROGRESS_MAX
        circleProgressBar.circleProgressColor = resources.getColor(R.color.violet)
        tvStartTime.text = mTimeStart
        tvStartAddress.text = mLatLng?.let { getLocationDesciption(it) }
        tvEndTime.text = mTimeArrived
        tvEndAddress.text = getLocationDesciption(mDestinationLatLng)
        tvTime.text = getTimeDuringStatus(mCountTimer / 1000)
        tvDistance.text = mEtaDistance
        imgBtnCall.setImageDrawable(resources.getDrawable(R.drawable.ic_reached))
        showDialog()
    }

    private fun showDialog() {
        val dialog = DialogArrived.newInstance(getTimeDuringStatus(mCountTimer / 1000), mEtaDistance)
        val fragmentManager = supportFragmentManager
        dialog.show(fragmentManager, resources.getString(R.string.arrived_dialog_tag))
    }

    private fun getCalendarDateTime(): String {
        val calendar = Calendar.getInstance().time
        val simple = SimpleDateFormat("KK:mm a', Th'MM dd", Locale.ENGLISH)
        return simple.format(calendar)
    }

    private fun setArrowRightStartItemClick() {
        imgArrowRightStartItem.visibility = View.GONE
        imgArrowDropDownStartItem.visibility = View.VISIBLE
        tvStartAddress.visibility = View.VISIBLE
    }

    private fun setArrowDropDownEndItemClick() {
        imgArrowDropDownEndItem.visibility = View.GONE
        imgArrowRightEndItem.visibility = View.VISIBLE
        tvEndAddress.visibility = View.GONE
    }

    private fun setArrowDropDownStartItemClick() {
        imgArrowDropDownStartItem.visibility = View.GONE
        imgArrowRightStartItem.visibility = View.VISIBLE
        tvStartAddress.visibility = View.GONE
    }

    private fun setArrowRightEndItemClick() {
        imgArrowRightEndItem.visibility = View.GONE
        imgArrowDropDownEndItem.visibility = View.VISIBLE
        tvEndAddress.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mCurrentBatteryReceiver)
    }
}

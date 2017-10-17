package vn.asiantech.way.ui.in_progress

import android.Manifest
import android.animation.Animator
import android.animation.IntEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.location.LocationManager
import android.os.BatteryManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.hypertrack.lib.HyperTrack
import com.hypertrack.lib.HyperTrack.stopTracking
import com.hypertrack.lib.HyperTrackUtils
import com.hypertrack.lib.callbacks.HyperTrackCallback
import com.hypertrack.lib.callbacks.HyperTrackEventCallback
import com.hypertrack.lib.internal.common.models.VehicleType
import com.hypertrack.lib.internal.consumer.view.MarkerAnimation
import com.hypertrack.lib.internal.transmitter.models.HyperTrackEvent
import com.hypertrack.lib.models.*
import kotlinx.android.synthetic.main.fragment_progress_location.*
import vn.asiantech.way.R
import vn.asiantech.way.ui.splash.SplashActivity


/**
 * A simple [Fragment] subclass.
 */
class ProgressLocationFragment : Fragment(), OnMapReadyCallback {

    private var mGoogleMap: GoogleMap? = null

    private var mCurrentLocationMarker: Marker? = null
    private var mCircle: GroundOverlay? = null
    private var mValueAnimator: ValueAnimator? = null
    private var mCircleRadius: Int = 160
    private var mIsMapLoaded = false
    private var mIsVehicleTypeTabLayoutVisible = false
    private var mExpectedPlace: Place? = null
    private var mIsRestoreLocationSharing = false
    private var mIsHandleTrackingUrlDeeplink = false

    private var mDestinationPlace: Place? = null
    private var mUserPositions: MutableList<Location> ?= null

    private val mCurrentBatteryReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(p0: Context?, p1: Intent?) {
            val level = p1?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            tvBattery.text = "${level.toString()}%"
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_progress_location, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkGPS()
        initView()
        getCurrentLocation()
        activity.registerReceiver(mCurrentBatteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//        setHyperTrackCallbackForActivityUpdates()
        addEvents()
    }

    private fun initView() {
        val mapFragment = activity.fragmentManager.findFragmentById(R.id.mapFragment) as MapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
//        startPulse(false)
    }

    private fun addEvents() {
        rlCollapse.setOnClickListener {
            if (rlExpandedInfo.visibility == View.GONE) {
                rlExpandedInfo.visibility = View.VISIBLE
                imgArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_18dp)
            } else {
                rlExpandedInfo.visibility = View.GONE
                imgArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_18dp)
            }
        }
        //TODO Action ripple here!
        rippleTrackingToggle.setOnRippleCompleteListener {
            if (rippleTrackingToggle.tag == "stop") {
                stopTracking()
            } else if (rippleTrackingToggle.tag == "summary") {

            }
        }
        rippleShareLink.setOnClickListener { Toast.makeText(context, "SHARE SHARE SHARE", Toast.LENGTH_SHORT).show() }
    }

    override fun onMapReady(p0: GoogleMap?) {
        MapsInitializer.initialize(context)
        mGoogleMap = p0
        /*ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                1)
        ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1)*/
        mGoogleMap?.addMarker(MarkerOptions()
                .title("A Place!")
                .position(LatLng(16.082709, 108.236512))
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_hero_marker_car))
        )
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(16.074812, 108.233132), 12f))
    }

    // check GPS
    private fun checkGPS() {
        val manager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val builder = AlertDialog.Builder(activity)
            builder.setMessage("Turn on GPS")
                    .setPositiveButton("OK") { dialogInterface, _ ->
                        //                        activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        ActivityCompat.requestPermissions(activity,
                                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                                1)
                        dialogInterface.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }
            builder.create().show()
        }
    }

    //init List of location when update location
    private fun getCurrentLocation() {
        mUserPositions = ArrayList()
//        mUserPositions.add(Location(LatLng(16.082709, 108.236512)))
        mIsMapLoaded = true
        updateCurrentLocation(null)
    }

    override fun onPause() {
        super.onPause()
//        stopPulse()
    }

    private fun setHyperTrackCallbackForActivityUpdates() {
        HyperTrack.setCallback(object : HyperTrackEventCallback() {
            override fun onEvent(p0: HyperTrackEvent) {
                when (p0.eventType) {
                    HyperTrackEvent.EventType.LOCATION_CHANGED_EVENT -> updateCurrentLocation(p0.location)
                }
            }

            override fun onError(p0: ErrorResponse) {
                Log.d("at-dinhvo", "ErrorResponse when setHyperTrackCallbackForActivityUpdates()")
            }
        })
    }

    private fun setCallbackForHyperTrackEvents() {
        HyperTrack.setCallback(object : HyperTrackEventCallback() {
            override fun onEvent(event: HyperTrackEvent) {
                when (event.eventType) {
                    HyperTrackEvent.EventType.STOP_ENDED_EVENT -> {
                        /*//Check if user has shared his tracking link
                        if (ActionManager.getSharedManager(this@Home).isActionLive()) {
                            return
                        }*/

                        activity.runOnUiThread({
                            val builder = ServiceNotificationParamsBuilder()
                            val action = ArrayList<String>()
                            action.add("Set Destination Address")
                            val notificationParams = builder
                                    .setSmallIcon(R.drawable.ic_ht_hero_marker_car)
                                    .setSmallIconBGColor(ContextCompat.getColor(context, R.color.colorAccent))
                                    .setContentTitle("setContentTitle: pewpewpewpew")
                                    .setContextText("setContextText: pewpewpewpew")
                                    .setContentIntentActivityClass(SplashActivity::class.java)
                                    .setContentIntentExtras(action)
                                    .build()
                            HyperTrack.setServiceNotificationParams(notificationParams)
                        })
                    }
                    HyperTrackEvent.EventType.TRACKING_STOPPED_EVENT, HyperTrackEvent.EventType.ACTION_ASSIGNED_EVENT, HyperTrackEvent.EventType.ACTION_COMPLETED_EVENT, HyperTrackEvent.EventType.STOP_STARTED_EVENT -> HyperTrack.clearServiceNotificationParams()
                    HyperTrackEvent.EventType.LOCATION_CHANGED_EVENT -> {
                        Log.d("at-dinhvo", "onEvent: Location Changed")
                        updateCurrentLocation(event.location)
                    }
                }
            }

            override fun onError(errorResponse: ErrorResponse) {
                // do nothing
                Log.d("at-dinhvo", "onError: ErrorResponse Location Changed")
            }
        })
    }

    fun updateCurrentLocation(location: HyperTrackLocation?) {
        if (location?.geoJSONLocation == null || location.geoJSONLocation.latLng == null) {
            HyperTrack.getCurrentLocation(object : HyperTrackCallback() {
                override fun onSuccess(p0: SuccessResponse) {
                    Log.d("at-dinhvo", "onSuccess: Current Location Recieved")
                    val hyperTrackLocation = HyperTrackLocation(p0.responseObject as? Location)
                    // check speed
                    Log.d("at-dinhvo", "speed: ${hyperTrackLocation.distanceTo(
                            HyperTrackLocation(16.082709, 108.236512))}")
//                    tvSpeed.text = hyperTrackLocation.speed.toString()
                    updateCurrentLocation(hyperTrackLocation)
                }

                override fun onError(p0: ErrorResponse) {
                    Log.d("at-dinhvo", "onError: Current Location Receiving error")
                    Log.d("at-dinhvo", "onError: " + p0.errorMessage)
                }
            })
            return
        }
        val latLng = location.geoJSONLocation.latLng
        Log.d("at-dinhvo", "Lat: " + latLng.latitude + "; Lng: " + latLng.longitude)
        if (mCurrentLocationMarker == null) {
            Log.d("at-dinhvo", "CurrentLocationMarker is null")
            /*mCurrentLocationMarker = mGoogleMap?.addMarker(MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_source_place_marker))
                    .anchor(0.5f, 0.5f))*/
            mCurrentLocationMarker = mGoogleMap?.addMarker(MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_hero_marker))
                    .anchor(0.5f, 0.5f))
//            addPulseRing(latLng)
        } else {
            mCurrentLocationMarker?.isVisible = true
            mCircle?.position = latLng
            mCircle?.isVisible = true
            MarkerAnimation.animateMarker(mCurrentLocationMarker, latLng)
        }
//        startPulse(false)
        if (!mIsRestoreLocationSharing && !mIsHandleTrackingUrlDeeplink) {
            updateMapView()
        }
    }

    private fun updateMapView() {
        Log.d("at-dinhvo", "UpdateMapView")
        if (mGoogleMap == null || !mIsMapLoaded) {
            return
        }
        if (mCurrentLocationMarker == null && mExpectedPlace == null) {
            return
        }
        val builder = LatLngBounds.Builder()
        if (mCurrentLocationMarker != null) {
            val current = mCurrentLocationMarker?.position
            builder.include(current)
        }
        if (mExpectedPlace != null) {
            val destination = mExpectedPlace?.location?.latLng
            builder.include(destination)
        }
        val bounds = builder.build()

        try {
            val cameraUpdate: CameraUpdate
            cameraUpdate = if (mExpectedPlace != null && mCurrentLocationMarker != null) {
                val width = resources.displayMetrics.widthPixels
                val height = resources.displayMetrics.heightPixels
                val padding = (width * 0.12).toInt()
                CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
            } else {
                val latLng = if (mCurrentLocationMarker != null)
                    mCurrentLocationMarker?.position
                else
                    mExpectedPlace?.location?.latLng
                CameraUpdateFactory.newLatLngZoom(latLng, 16f)
            }
            mGoogleMap?.animateCamera(cameraUpdate, 1000, null)
        } catch (e: Exception) {
            Log.d("at-dinhvo", "Exception when updateMapView()")
        }

    }

    private fun startPulse(reset: Boolean) {
        Log.d("at-dinhvo", "startPulse")
        if (!HyperTrackUtils.isInternetConnected(context)) {
            Log.d("at-dinhvo", "is InternetConnected")
            if (mCircle != null) {
                mCircle?.remove()
            }
            if (mValueAnimator != null) {
                mValueAnimator?.cancel()
            }
        }
        if (mValueAnimator == null || reset) {
            if (mValueAnimator != null)
                mValueAnimator?.end()
            val radius = intArrayOf(mCircleRadius)
            mValueAnimator = ValueAnimator()
            mValueAnimator?.repeatCount = ValueAnimator.INFINITE
            mValueAnimator?.repeatMode = ValueAnimator.RESTART
            mValueAnimator?.setIntValues(0, radius[0])
            mValueAnimator?.duration = 2000
            mValueAnimator?.setEvaluator(IntEvaluator())
            mValueAnimator?.interpolator = AccelerateDecelerateInterpolator()
            mValueAnimator?.addUpdateListener({ valueAnimator ->
                val animatedFraction = valueAnimator.animatedFraction
                mCircle?.setDimensions(animatedFraction * radius[0])
                mCircle?.transparency = animatedFraction
            })
            mValueAnimator?.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    mCircle?.transparency = 1f
                    mCircle?.isVisible = true
                    if (mCurrentLocationMarker != null)
                        mCurrentLocationMarker?.isVisible = true
                }

                override fun onAnimationEnd(animation: Animator) {

                }

                @SuppressLint("ObjectAnimatorBinding")
                override fun onAnimationCancel(animation: Animator) {
                    ObjectAnimator.ofFloat(mCircle, "transparency", 1f, 0f).setDuration(500).start()
                    mCircle?.isVisible = false
                    if (mCurrentLocationMarker != null)
                        mCurrentLocationMarker?.isVisible = false
                }

                override fun onAnimationRepeat(animation: Animator) {
                    //radius[0] = circleRadius;
                }
            })
        }
        mValueAnimator?.start()
    }

    private fun stopPulse() {
        Log.d("at-dinhvo", "stopPulse")
        if (mValueAnimator != null) {
            mValueAnimator?.cancel()
        }
    }

    private fun addPulseRing(latLng: LatLng) {
        Log.d("at-dinhvo", "addPulseRing")
        val d = GradientDrawable()
        d.shape = GradientDrawable.OVAL
        d.setSize(500, 500)
        d.setColor(ContextCompat.getColor(context, R.color.pulse_color))
        val bitmap = Bitmap.createBitmap(d.intrinsicWidth, d.intrinsicHeight, Bitmap.Config.ARGB_8888)
        // Convert the drawable to bitmap
        val canvas = Canvas(bitmap)
        d.setBounds(0, 0, canvas.width, canvas.height)
        d.draw(canvas)
        // Radius of the circle
        val radius = 100
        // Add the circle to the map
        mCircle = mGoogleMap?.addGroundOverlay(GroundOverlayOptions()
                .position(latLng, (2 * radius).toFloat())
                .image(BitmapDescriptorFactory.fromBitmap(bitmap)))
    }

    private fun track(latLng: LatLng){
        HyperTrack.getETA(latLng, VehicleType.WALK, object : HyperTrackCallback(){
            override fun onSuccess(p0: SuccessResponse) {
                Log.d("at-dinhvo", "eta: ${p0.responseObject}")
//                LatLng = p0.responseObject.
            }

            override fun onError(p0: ErrorResponse) {

            }

        })
    }
}

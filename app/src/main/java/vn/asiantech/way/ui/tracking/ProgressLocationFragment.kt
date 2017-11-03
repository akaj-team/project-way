package vn.asiantech.way.ui.tracking

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.hypertrack.lib.HyperTrack
import com.hypertrack.lib.callbacks.HyperTrackCallback
import com.hypertrack.lib.internal.common.models.VehicleType
import com.hypertrack.lib.internal.consumer.view.MarkerAnimation
import com.hypertrack.lib.models.ErrorResponse
import com.hypertrack.lib.models.HyperTrackLocation
import com.hypertrack.lib.models.SuccessResponse
import kotlinx.android.synthetic.main.fragment_progress_location.*
import vn.asiantech.way.R
import vn.asiantech.way.extension.toast
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * Copyright © AsianTech Co., Ltd
 * Created by dinhvox on 22/10/2017.
 */
class ProgressLocationFragment : Fragment(), OnMapReadyCallback {

    companion object {
        private const val ZOOM_SIZE = 16f
        private const val TIME_CONVERT = 3.6
        private const val ONE_THOUSAND = 1000L
        private const val RADIUS = 360.0
        private const val STEP_ETA = 3
    }

    private var mGoogleMap: GoogleMap? = null
    private var mMapFragment: SupportMapFragment? = null
    private var mCurrentMarker: Marker? = null
    private var mIsStopTracking = false
    private var mLocationUpdates: MutableList<LatLng>? = null
    private var mLocations: MutableList<vn.asiantech.way.data.model.Location>? = null
    private var mCurrentLocation: HyperTrackLocation? = null
    private lateinit var mHandlerTracking: Handler
    private var mRunnable: Runnable? = null
    private var mCountTimer = ONE_THOUSAND
    private lateinit var mDestinationLatLng: LatLng
    private var mDistanceTravel = 0f
    private var mAverageSpeed = 0f
    private var mEtaUpdate = 0.0f
    private var mEtaMaximum = 0.0f
    private var mEtaSpeed = 0.0f
    private var mCount = 0

    private val mCurrentBatteryReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(p0: Context?, p1: Intent?) {
            val level = p1?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            tvBattery.text = "${level.toString()}%"
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity.registerReceiver(mCurrentBatteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        return inflater!!.inflate(R.layout.fragment_progress_location, container, false)
    }

    private fun initMapsView() {
        mMapFragment = childFragmentManager.findFragmentById(R.id.mapsFragment) as SupportMapFragment?
        mMapFragment?.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap?) {
        mGoogleMap = p0
        HyperTrack.getCurrentLocation(object : HyperTrackCallback() {
            override fun onSuccess(p0: SuccessResponse) {
                mCurrentLocation = HyperTrackLocation((p0.responseObject) as Location?)
                if (mCurrentLocation != null) {
                    updateUI(mCurrentLocation!!)
                }
            }

            override fun onError(p0: ErrorResponse) {
            }
        })
        mDestinationLatLng = LatLng(16.09175, 108.23747)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMapsView()
        mGoogleMap?.clear()
        mLocationUpdates = ArrayList()
        Log.d("zxc","aaa "+mLocationUpdates)
        mLocations = ArrayList()
        getLocationName(LatLng(16.09175, 108.23747))
        handlerProgressTracking()
        addEvents()
    }

    private fun handlerProgressTracking() {
        mHandlerTracking = Handler()
        mRunnable = Runnable {
            if (mLocationUpdates != null) {
                Log.d("zxc","aaa "+mLocationUpdates)
                if (mLocationUpdates!!.size > 0
                        && (mLocationUpdates!![mLocationUpdates!!.size - 1] == mDestinationLatLng)) {
                    mEtaUpdate = 0f
                    mAverageSpeed = 0f
                    mCurrentMarker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_location))
                    mCurrentMarker?.setAnchor(0.5f, 0.5f)
                    updateCurrentTimeView()
                    Toast.makeText(context, "DONE!", Toast.LENGTH_SHORT).show()
                    return@Runnable
                }
                requestLocation()
                requestEta()
                mCountTimer += ONE_THOUSAND
                handlerProgressTracking()
            }
        }
        mHandlerTracking.postDelayed(mRunnable, ONE_THOUSAND)
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

    private fun requestEta() {
        HyperTrack.getETA(mDestinationLatLng, VehicleType.MOTORCYCLE, object : HyperTrackCallback() {
            override fun onSuccess(p0: SuccessResponse) {
                mEtaUpdate = (p0.responseObject as Double?)!!.toFloat()
                if (mCount < 1) {
                    mEtaMaximum = mEtaUpdate
                }
                mCount++
            }

            override fun onError(p0: ErrorResponse) {
                Log.d("at-dinhvo", "ETA: ErrorResponse " + p0.errorMessage)
            }
        })
    }

    private fun getLocationName(latLng: LatLng?) {
        val geoCoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geoCoder.getFromLocation(latLng!!.latitude, latLng.longitude, 1)
        if (addresses.isNotEmpty()) {
            val address: Address = addresses[0]
            tvDestination.text = address.getAddressLine(0)
        } else {
            tvDestination.text = null
        }
    }

    private fun updateCurrentTimeView() {
        tvSpeed.text = String.format("%.2f", mAverageSpeed).plus(" km/h")
        tvTime.text = resources.getString(R.string.eta).plus(getEtaTime(mEtaUpdate))
        circleProgressBar.progress = 100
    }

    private fun updateUI(hyperTrackLocation: HyperTrackLocation) {
        val latLng = hyperTrackLocation.geoJSONLocation.latLng
        if (mLocationUpdates != null) {
            if (latLng != null) {
                mLocationUpdates?.add(latLng)
            }
            if (mLocationUpdates!!.size > 1) {
                mDistanceTravel += getDistancePerSecond(mLocationUpdates!![mLocationUpdates!!.size - 2]
                        , mLocationUpdates!![mLocationUpdates!!.size - 1])
            }
            updateView(hyperTrackLocation)
            updateMapView(latLng)
        }
    }

    private fun updateView(hyperTrackLocation: HyperTrackLocation) {
        if (!mIsStopTracking) {
            tvActionStatus.text = resources.getString(R.string.leaving)
        }
        tvTime.text = resources.getString(R.string.eta).plus(getEtaTime(mEtaUpdate))
        if (hyperTrackLocation.speed != null) {
            tvDistance.text = resources.getString(R.string.open_parentheses)
                    .plus(String.format(" %.2f", (mEtaMaximum * hyperTrackLocation.speed) / ONE_THOUSAND))
                    .plus(resources.getString(R.string.close_parentheses_distance))
        }
        tvSpeed.text = String.format("%.2f", mAverageSpeed).plus(" km/h")
        tvElapsedTime.text = formatInterval(mCountTimer)
        tvDistanceTravelled.text = String.format("%.2f", mDistanceTravel).plus(" km")
        if (mEtaMaximum - mEtaUpdate > STEP_ETA) {
            circleProgressBar.progress = (mEtaMaximum - (mEtaUpdate / mEtaMaximum) * 100).toInt()
        }
    }

    private fun getEtaTime(eta: Float): String = when {
        eta >= 3600 -> String.format(" %02d", (eta / 3600).toInt()).plus(" hour")
        eta < 60 -> String.format(" %02d", eta.toInt()).plus(" sec")
        else -> String.format(" %02d", (eta / 60).toInt()).plus(" min")
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
        return src.distanceTo(des) / ONE_THOUSAND
    }

    private fun drawLine() {
        if (mLocationUpdates != null) {
            if (mLocationUpdates!!.size > 1) {
                mGoogleMap?.addPolyline(PolylineOptions()
                        .add(mLocationUpdates!![mLocationUpdates!!.size - 2], mLocationUpdates!![mLocationUpdates!!.size - 1])
                        .width(ZOOM_SIZE / 2)
                        .color(Color.BLACK))
            }
        }
    }

    private fun updateMapView(latLng: LatLng) {
        var angle = 0.0f
        if (mLocationUpdates!!.size > 1) {
            angle = getAngleMarker(mLocationUpdates!![mLocationUpdates!!.size - 2], mLocationUpdates!![mLocationUpdates!!.size - 1]).toFloat()
        }
        if (mCurrentMarker == null) {
            mCurrentMarker = mGoogleMap?.addMarker(MarkerOptions().
                    position(latLng).
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_hero_marker))
                    .anchor(0.5f, 0.5f)
                    .flat(true))
        } else {
            mCurrentMarker!!.position = latLng
            mCurrentMarker!!.rotation = angle
        }
        drawLine()
        MarkerAnimation.animateMarker(mCurrentMarker, latLng)
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
        rippleTrackingToggle.setOnRippleCompleteListener {
            if (rippleTrackingToggle.tag == "stop") {
                mIsStopTracking = true
                mHandlerTracking.removeCallbacks(mRunnable)
                activity.toast("Stop tracking")
            } else if (rippleTrackingToggle.tag == "summary") {

            }
        }
        rippleShareLink.setOnClickListener {
            activity.toast("Click rippleShareLink")
        }
        imgBtnCall.setOnClickListener {
            activity.toast("Click button call")
        }
        imgBtnBack.setOnClickListener {
            activity.toast("Click button back")
        }
    }
}

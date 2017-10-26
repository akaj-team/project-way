package vn.asiantech.way.ui.tracking

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
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
import com.google.android.gms.maps.CameraUpdateFactory
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
import java.util.concurrent.TimeUnit

/**
 * Copyright © AsianTech Co., Ltd
 * Created by dinhvox on 22/10/2017.
 */
class ProgressLocationFragment : Fragment(), OnMapReadyCallback {

    companion object {
        private const val ZOOM_SIZE = 16f
    }

    private var mGoogleMap: GoogleMap? = null
    private var mMapFragment: SupportMapFragment? = null
    private var mCurrentMarker: Marker? = null
    private var mIsStopTracking = false
    private var mLocationUpdates: MutableList<LatLng>? = null
    private var mCurrentLocation: HyperTrackLocation? = null
    private var mHandlerTracking: Handler? = null
    private var mRunnable: Runnable? = null
    private var mCountTimer = 1000L
    private lateinit var mDestinationLatLng: LatLng
    private var mDistanceTravel = 0f

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
                mCurrentLocation = HyperTrackLocation((p0.responseObject) as Location)
                if (mCurrentLocation != null) {
                    updateUI(mCurrentLocation!!)
                }
            }

            override fun onError(p0: ErrorResponse) {
            }
        })
        mDestinationLatLng = LatLng(16.07005, 108.24066)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMapsView()
        mLocationUpdates = ArrayList()
        tracking()
        addEvents()
    }

    private fun tracking() {
        mHandlerTracking = Handler()
        mRunnable = Runnable {
            if (mLocationUpdates!!.size > 0 && mLocationUpdates!![mLocationUpdates!!.size - 1] == mDestinationLatLng) {
                Toast.makeText(context, "STOP TRACKING", Toast.LENGTH_SHORT).show()
                return@Runnable
            }
            HyperTrack.getCurrentLocation(object : HyperTrackCallback() {
                override fun onSuccess(p0: SuccessResponse) {
                    val hyperTrackLocation = HyperTrackLocation((p0.responseObject) as Location)
                    updateUI(hyperTrackLocation)
                }

                override fun onError(p0: ErrorResponse) {
                }
            })
            val expectedLocation = LatLng(16.07005, 108.24066)
            HyperTrack.getETA(expectedLocation, VehicleType.MOTORCYCLE, object : HyperTrackCallback() {
                override fun onSuccess(p0: SuccessResponse) {
                    val etaResult = (p0.responseObject as Double).toDouble()
                    Log.d("at-dinhvo", "Result ETA: " + etaResult)
                }

                override fun onError(p0: ErrorResponse) {
                    Log.d("at-dinhvo", "ETA: ErrorResponse")
                }
            })
            mCountTimer += 1000
            tracking()
        }
        mHandlerTracking!!.postDelayed(mRunnable, 1000)
    }

    private fun updateUI(hyperTrackLocation: HyperTrackLocation) {
        val latLng = hyperTrackLocation.geoJSONLocation.latLng
        mLocationUpdates?.add(latLng)
        if (mLocationUpdates!!.size > 1) {
            mDistanceTravel += getDistance(mLocationUpdates!![mLocationUpdates!!.size - 2]
                    , mLocationUpdates!![mLocationUpdates!!.size - 1])
        }
        Log.d("at-dinhvo", "Location: " + latLng.latitude + " - " + latLng.longitude)
        updateView(hyperTrackLocation)
        updateMapView(latLng)
    }

    private fun updateView(hyperTrackLocation: HyperTrackLocation) {
        if (!mIsStopTracking) {
            tvActionStatus.text = resources.getString(R.string.leaving)
        }
        tvTime.text = "ETA unknown"
        tvDistance.text = "Distance Unknown"
        /*val destLocation = Location("Point")
        destLocation.latitude = 16.07005
        destLocation.longitude = 108.24066
        val sourceLocation = Location("Point")
        sourceLocation.latitude = mCurrentLocation!!.latLng.latitude
        sourceLocation.longitude = mCurrentLocation!!.latLng.longitude

        tvDistance.text = "(${String.format("%.2f", sourceLocation.distanceTo(destLocation) / 1000).plus(" km away")})"*/
        tvSpeed.text = String.format("%.2f", hyperTrackLocation.speed).plus(" km/h")
        tvElapsedTime.text = formatInterval(mCountTimer)
        tvDistanceTravelled.text = String.format("%.2f", mDistanceTravel).plus(" km")
    }

    private fun formatInterval(millis: Long): String {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))
    }

    private fun directMarker() {

    }

    private fun getDistance(source: LatLng, destination: LatLng): Float {
        val des = Location("Point")
        des.latitude = source.latitude
        des.longitude = source.longitude
        val src = Location("Point")
        src.latitude = destination.latitude
        src.longitude = destination.longitude
        return src.distanceTo(des) / 1000
    }

    private fun drawLine() {
        if (mLocationUpdates!!.size >= 2) {
            mGoogleMap?.addPolyline(PolylineOptions()
                    .add(mLocationUpdates!![mLocationUpdates!!.size - 2], mLocationUpdates!![mLocationUpdates!!.size - 1]).width(ZOOM_SIZE / 2)
                    .color(Color.BLACK))
        }
    }

    private fun updateMapView(latLng: LatLng) {
        if (mCurrentMarker == null) {
            mCurrentMarker = mGoogleMap?.addMarker(MarkerOptions().
                    position(latLng).
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_hero_marker))
                    .anchor(0.5f, 0.5f)
            )
            MarkerAnimation.animateMarker(mCurrentMarker, latLng)
        } else {
            mCurrentMarker!!.position = latLng
        }
        drawLine()
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_SIZE))
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
                mHandlerTracking?.removeCallbacks(mRunnable)
                Toast.makeText(context, "STOP TRACKING", Toast.LENGTH_SHORT).show()
            } else if (rippleTrackingToggle.tag == "summary") {

            }
        }
        rippleShareLink.setOnClickListener {

        }
    }
}

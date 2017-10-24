package vn.asiantech.way.ui.tracking

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_progress_location.*
import vn.asiantech.way.R
import vn.asiantech.way.ui.map.MapFragment
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class ProgressLocationFragment : Fragment(), LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    companion object {
        private val KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates"
        private val KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string"
        private val KEY_LOCATION = "location"
        private val ONE_MIN = (1000 * 60).toLong()
        private val INTERVAL = (1000 * 30).toLong()
        private val FASTEST_UPDATE = (1000 * 5).toLong()
        private val MIN_ACCURACY = 25.0f
        private val MIN_LAST_READ_ACCURACY = 500.0f
    }

    private var mMapFragmentCustom: MapFragment? = null
    private var mLocationRequest: LocationRequest? = null
    private var mBestReading: Location? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocations: MutableList<Location>? = null
    private var mRequestingLocationUpdates: Boolean = false
    private var mLastUpdateTime: String? = null
    private var mGoogleMap: GoogleMap? = null

    private val mCurrentBatteryReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(p0: Context?, p1: Intent?) {
            val level = p1?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            tvBattery.text = "${level.toString()}%"
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        initMapView()
        updateValuesFromBundle(savedInstanceState)
        activity.registerReceiver(mCurrentBatteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        return inflater!!.inflate(R.layout.fragment_progress_location, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createLocationRequest()
        addEvents()
    }

    override fun onResume() {
        super.onResume()
        mGoogleApiClient?.connect()
    }

    override fun onPause() {
        super.onPause()
        if (mGoogleApiClient != null && mGoogleApiClient!!.isConnected) {
            mGoogleApiClient?.disconnect()
        }
    }

    private fun createLocationRequest() {
        mLocations = ArrayList()
        mLocationRequest = LocationRequest.create()
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest?.interval = INTERVAL
        mLocationRequest?.fastestInterval = FASTEST_UPDATE
        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build()
        }
        Log.d("at-dinhvo", "createLocationRequest")
    }

    private fun initMapView() {
        mMapFragmentCustom = MapFragment()
        mMapFragmentCustom?.clearAllMarker()
        activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.frMapContainer, mMapFragmentCustom)
                .commit()
        mGoogleMap = mMapFragmentCustom!!.getGoogleMap()
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

            } else if (rippleTrackingToggle.tag == "summary") {

            }
        }
        rippleShareLink.setOnClickListener {
            Log.d("at-dinhvo", "Location: " + mBestReading?.latitude + " - " + mBestReading?.longitude
                    + " - " + "Size of list location: " + mLocations!!.size)
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState!!.putBoolean(KEY_REQUESTING_LOCATION_UPDATES,
                mRequestingLocationUpdates)
        savedInstanceState.putParcelable(KEY_LOCATION, mBestReading)
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime)
        super.onSaveInstanceState(savedInstanceState)
    }

    private fun updateValuesFromBundle(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(KEY_REQUESTING_LOCATION_UPDATES)
//                setButtonsEnabledState()
            }
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                mBestReading = savedInstanceState.getParcelable(KEY_LOCATION)
            }
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING)
            }
            updateUI()
        }
    }

    override fun onLocationChanged(p0: Location?) {
        Log.d("at-dinhvo", "onLocationChanged: " + p0!!.latitude + " - " + p0.longitude)
        if (mBestReading != p0) {
            mLocations!!.add(p0)
        }
        if (mBestReading == null || p0.accuracy < mBestReading!!.accuracy) {
//            mLocations!!.add(p0)
            mBestReading = p0
            updateUI()
            if (mBestReading!!.accuracy < MIN_ACCURACY) {
                Log.d("at-dinhvo", "onLocationChanged: removeLocationUpdates")
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
            }
        }
    }

    private fun updateUI() {
        val currentLocation = LatLng(mBestReading!!.latitude, mBestReading!!.longitude)
        val marker = mGoogleMap?.addMarker(MarkerOptions()
                .position(currentLocation)
                .draggable(true)
                .title(getString(R.string.marker_title)))
        marker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_hero_marker))
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16f))
        if (marker != null) {
            animateMarker(marker, currentLocation, true)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        if (checkPlayServices()) {
            mBestReading = getBestLastKnownLocation(MIN_LAST_READ_ACCURACY, ONE_MIN * 5)
            Log.d("at-dinhvo", "onConnected: " + mBestReading?.latitude + "-" + mBestReading?.longitude)
            if (mBestReading == null
                    || mBestReading!!.accuracy > MIN_LAST_READ_ACCURACY
                    || mBestReading!!.time < System.currentTimeMillis() - ONE_MIN * 2) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
                Executors.newScheduledThreadPool(1).schedule({
                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
                }, ONE_MIN, TimeUnit.MILLISECONDS)
            }
        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    @SuppressLint("MissingPermission")
    private fun getBestLastKnownLocation(minAccuracy: Float, minTime: Long): Location? {
        var bestResult: Location? = null
        var bestAccuracy = 5f
        var bestTime = -1000000L
        val currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
        if (currentLocation != null) {
            val accuracy = currentLocation.accuracy
            val time = currentLocation.time
            if (accuracy < bestAccuracy) {
                bestResult = currentLocation
                bestAccuracy = accuracy
                bestTime = time
            }
        }
        return if (bestAccuracy > minAccuracy || bestTime < minTime) {
            null
        } else {
            bestResult
        }
    }

    private fun checkPlayServices(): Boolean {
        val googleAPI = GoogleApiAvailability.getInstance()
        val result = googleAPI.isGooglePlayServicesAvailable(context)
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                Log.d("at-dinhvo", "Check play_service ${ConnectionResult.SUCCESS}")
                googleAPI.getErrorDialog(activity, result, 0).show()
            }
            return false
        }
        return true
    }

    /*private class UpdateMarker(val googleMap: GoogleMap) : AsyncTask<List<Marker>, LatLng, LatLng>() {

        override fun doInBackground(vararg p0: List<Marker>?): LatLng {

        }

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onProgressUpdate(vararg values: LatLng?) {
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: LatLng?) {
            super.onPostExecute(result)
        }
    }*/

    private fun animateMarker(marker: Marker, latLng: LatLng, hideMarker: Boolean) {
        Log.d("at-dinhvo", "animateMarker")
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val projection = mGoogleMap?.projection
        val startPoint = projection?.toScreenLocation(marker.position)
        val startLatLng = projection?.fromScreenLocation(startPoint)
        val interpolator = LinearInterpolator()
        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = interpolator.getInterpolation((elapsed.toFloat()) / 500L)
                val lng = t * latLng.longitude + (1 - t) * startLatLng!!.longitude
                val lat = t * latLng.latitude + (1 - t) * startLatLng.latitude
                Log.d("at-dinhvo", "animateMarker:$lat - $lng")
                marker.position = LatLng(lat, lng)
                if (t < 1.0) {
                    handler.postDelayed(this, 16)
                } else {
                    marker.isVisible = !hideMarker
                }
                mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 16f))
            }
        })
    }
}

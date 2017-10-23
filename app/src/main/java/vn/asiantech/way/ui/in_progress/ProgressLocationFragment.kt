package vn.asiantech.way.ui.in_progress

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.BatteryManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        private val ONE_MIN = (1000 * 60).toLong()
        private val INTERVAL = (1000 * 30).toLong()
        private val FASTEST_UPDATE = (1000 * 5).toLong()
        private val MIN_ACCURACY = 25.0f
        private val MIN_LAST_READ_ACCURACY = 500.0f
    }

    private var mMapFragmentCustom: MapFragment? = null
    private var mGoogleMap: GoogleMap? = null
    private var mLocationRequest: LocationRequest? = null
    private var mBestReading: Location? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocations: MutableList<Location>? = null

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
    }

    private fun initMapView() {
        mMapFragmentCustom = MapFragment()
        activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.frMapContainer, mMapFragmentCustom)
                .commit()
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
            Toast.makeText(context, "Location: " + mBestReading?.latitude + " - " + mBestReading?.longitude, Toast.LENGTH_SHORT).show()
            Toast.makeText(context, "Size of list location: " + mLocations!!.size, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onLocationChanged(p0: Location?) {
        Toast.makeText(context, "onLocationChanged: " + p0!!.latitude + " - " + p0.longitude, Toast.LENGTH_SHORT).show()
        if (mBestReading == null || p0!!.accuracy < mBestReading!!.accuracy) {
            mLocations!!.add(p0!!)
            mBestReading = p0
            updateUI()
            if (mBestReading!!.accuracy < MIN_ACCURACY) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
            }
        }
    }

    private fun updateUI() {
        mGoogleMap?.addMarker(MarkerOptions()
                .title("A Place!")
                .position(LatLng(mBestReading!!.latitude, mBestReading!!.longitude))
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_hero_marker))
        )
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mBestReading!!.latitude, mBestReading!!.longitude), 14f))
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
                googleAPI.getErrorDialog(activity, result, 0).show()
            }
            return false
        }
        return true
    }
}

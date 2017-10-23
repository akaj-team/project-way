package vn.asiantech.way.ui.in_progress

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.location.LocationManager
import android.os.BatteryManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_progress_location.*
import vn.asiantech.way.R
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class ProgressLocationFragment : Fragment(), OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    companion object {
        private val ONE_MIN = (1000 * 60).toLong()
        private val TWO_MIN = ONE_MIN * 2
        private val FIVE_MIN = ONE_MIN * 5
        private val POLLING_FREQ = (1000 * 30).toLong()
        private val FASTEST_UPDATE_FREQ = (1000 * 5).toLong()
        private val MIN_ACCURACY = 25.0f
        private val MIN_LAST_READ_ACCURACY = 500.0f
    }

    private var mGoogleMap: GoogleMap? = null
    private var mMapFragment: SupportMapFragment? = null
    private var mLocationRequest: LocationRequest? = null
    private var mBestReading: Location? = null
    private var mGoogleApiClient: GoogleApiClient? = null

    private val mCurrentBatteryReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(p0: Context?, p1: Intent?) {
            val level = p1?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            tvBattery.text = "${level.toString()}%"
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        checkGPS()
        initMapView()
        activity.registerReceiver(mCurrentBatteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        return inflater!!.inflate(R.layout.fragment_progress_location, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRequestLocation()
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

    private fun initRequestLocation() {
        mLocationRequest = LocationRequest.create()
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest?.interval = POLLING_FREQ
        mLocationRequest?.fastestInterval = FASTEST_UPDATE_FREQ
        mGoogleApiClient = GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()
    }

    private fun initMapView() {
        mMapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mMapFragment?.getMapAsync(this)
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
        rippleShareLink.setOnClickListener { Toast.makeText(context, "SHARE SHARE SHARE", Toast.LENGTH_SHORT).show() }
    }

    override fun onMapReady(p0: GoogleMap?) {
        MapsInitializer.initialize(context)
        mGoogleMap = p0
        mGoogleMap?.addMarker(MarkerOptions()
                .title("A Place!")
                .position(LatLng(16.082709, 108.236512))
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_hero_marker_car))
        )
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(16.074812, 108.233132), 14f))
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

    override fun onLocationChanged(p0: Location?) {
        if (mBestReading == null || p0!!.accuracy < mBestReading!!.accuracy) {
            mBestReading = p0
            if (mBestReading!!.accuracy < MIN_ACCURACY) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        if (servicesAvailable()) {
            mBestReading = bestLastKnownLocation(MIN_LAST_READ_ACCURACY, FIVE_MIN)
            if (mBestReading == null
                    || mBestReading!!.accuracy > MIN_LAST_READ_ACCURACY
                    || mBestReading!!.time < System.currentTimeMillis() - TWO_MIN) {
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
    private fun bestLastKnownLocation(minAccuracy: Float, minTime: Long): Location? {
        var bestResult: Location? = null
        var bestAccuracy = java.lang.Float.MAX_VALUE
        var bestTime = java.lang.Long.MIN_VALUE
        val mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
        if (mCurrentLocation != null) {
            val accuracy = mCurrentLocation.accuracy
            val time = mCurrentLocation.time
            if (accuracy < bestAccuracy) {
                bestResult = mCurrentLocation
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

    private fun servicesAvailable(): Boolean {
        val resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context)
        return if (ConnectionResult.SUCCESS == resultCode) {
            true
        } else {
            GooglePlayServicesUtil.getErrorDialog(resultCode, activity, 0).show()
            false
        }
    }
}

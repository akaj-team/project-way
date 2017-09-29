package vn.asiantech.way.ui.home

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Point
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.*
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by atHangTran on 26/09/2017.
 */
class HomeActivity : BaseActivity(), OnMapReadyCallback {

    companion object {
        const val LOCATION_UPDATE_MIN_DISTANCE = 10f
        const val LOCATION_UPDATE_MIN_TIME = 5000L
        const val TAG = "Error"
    }

    private var mHomeAdapter: HomeAdapter? = null
    private var mGoogleMap: GoogleMap? = null
    private var mLocationManager: LocationManager? = null

    private var mLocationListener = object : LocationListener {
        override fun onLocationChanged(location: android.location.Location?) {
            if (location != null) {
                drawMarker(location)
                mLocationManager!!.removeUpdates(this)
            } else {
                Log.d(TAG, "The location is null")
            }
        }

        override fun onProviderEnabled(p0: String?) {
        }

        override fun onProviderDisabled(p0: String?) {
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        initViews()
        initMap()
        setDataForRecyclerView()
    }

    override fun onMapReady(p0: GoogleMap?) {
        mGoogleMap = p0
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        mGoogleMap!!.setPadding(0, 0, 0, size.y / 3)
        getCurrentLocation()
    }

    private fun initViews() {
        val monthFormat = SimpleDateFormat("MMM", Locale.US)
        val month = monthFormat.format(Calendar.getInstance().time)
        val dateFormat = SimpleDateFormat(" dd,yyyy", Locale.getDefault())
        val date = Date()
        val time = month + dateFormat.format(date)
        tvCurrentTime.text = time
    }

    private fun initMap() {
        val supportMapFragment = supportFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
        val googlePlayStatus = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        if (googlePlayStatus != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, googlePlayStatus, -1).show()
            finish()
        } else {
            if (mGoogleMap != null) {
                mGoogleMap!!.uiSettings.isMyLocationButtonEnabled = true
                mGoogleMap!!.uiSettings.setAllGesturesEnabled(true)
            }
        }
    }

    private fun getCurrentLocation() {
        val isGPSEnabled = mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = mLocationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        var location: android.location.Location? = null

        if (isNetworkEnabled) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                mLocationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener)
                location = mLocationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
        }

        if (isGPSEnabled) {
            mLocationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener)
            location = mLocationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } else {
            Log.d(TAG, "You need enable GPS or network")
        }

        if (location != null) {
            drawMarker(location)
        }
    }

    private fun drawMarker(location: android.location.Location) {
        if (mGoogleMap != null) {
            mGoogleMap!!.clear()
            val currentLocation = LatLng(location.latitude, location.longitude)
            mGoogleMap!!.addMarker(MarkerOptions()
                    .position(currentLocation)
                    .draggable(true)
                    .title("Current location"))
                    .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_point))
            mGoogleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16f))
        } else {
            Log.d(TAG, "Google map is null")
        }
    }

    private fun setDataForRecyclerView() {
        val locations = ArrayList<Location>()
        // TODO: Get data from share function into locations

        initDummyData(locations)

        mHomeAdapter = HomeAdapter(locations) {
            locations.forEach {
                it.isChoose = false
            }
            locations[it].isChoose = true
            recycleViewLocation.smoothScrollToPosition(it + 1)
            mHomeAdapter!!.notifyDataSetChanged()
        }
        recycleViewLocation.layoutManager = LinearLayoutManager(this)
        recycleViewLocation.adapter = mHomeAdapter
    }

    fun initDummyData(locations: ArrayList<Location>) {
        locations.add(Location("12:00 AM", "Stop", "If you want to go market, you can turn left and go straight!!!!!!!"))
        locations.add(Location("12:00 AM", "Stop", "Stop here"))
        locations.add(Location("12:00 AM", "Stop", "Stop here"))
        locations.add(Location("12:00 AM", "Stop", "Stop here"))
        locations.add(Location("12:00 AM", "Stop", "Stop here"))
        locations.add(Location("12:00 AM", "Stop", "Stop here"))
    }
}

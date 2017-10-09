package vn.asiantech.way.ui.home

import android.content.Context
import android.graphics.Point
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_home.recycleViewLocation
import kotlinx.android.synthetic.main.toolbar.tvCurrentTime
import vn.asiantech.way.R
import vn.asiantech.way.extension.toast
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.util.LocationUtil
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Date
import kotlin.collections.ArrayList

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by atHangTran on 26/09/2017.
 */
class HomeActivity : BaseActivity(), OnMapReadyCallback {

    private var mPosition = -1
    private var mHomeAdapter: HomeAdapter? = null
    private var mGoogleMap: GoogleMap? = null
    private var mLocationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initMap()
        initViews()
        setDataForRecyclerView()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mGoogleMap = googleMap
        setPaddingGoogleLogo()
        val location = LocationUtil(this).getCurrentLocation()
        if (location != null) {
            drawMaker(location)
        } else {
            toast("Do not update the current location!")
        }
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
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        val supportMapFragment = supportFragmentManager.findFragmentById(R.id.fragmentMap) as? SupportMapFragment
        supportMapFragment?.getMapAsync(this)
    }

    private fun setPaddingGoogleLogo() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        mGoogleMap?.setPadding(0, 0, 0, size.y / 3)
    }

    private fun drawMaker(location: android.location.Location) {
        if (mGoogleMap != null) {
            mGoogleMap?.clear()
            val currentLocation = LatLng(location.latitude, location.longitude)
            mGoogleMap?.addMarker(MarkerOptions()
                    .position(currentLocation)
                    .draggable(true)
                    .title("Current location"))
                    ?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_point))
            mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16f))
        } else {
            toast("Google map is null")
        }
    }

    private fun setDataForRecyclerView() {
        val locations = ArrayList<Location>()
        // TODO: Get data from share function into locations

        initDummyData(locations)

        mHomeAdapter = HomeAdapter(locations) {
            if (mPosition >= 0) {
                locations[mPosition].isChoose = false
                mHomeAdapter?.notifyItemChanged(mPosition)
            }
            mPosition = it
            locations[it].isChoose = true
            mHomeAdapter?.notifyItemChanged(it)
            recycleViewLocation.scrollToPosition(it + 1)
        }
        recycleViewLocation.layoutManager = LinearLayoutManager(this)
        recycleViewLocation.adapter = mHomeAdapter
    }

    private fun initDummyData(locations: ArrayList<Location>) {
        locations.add(Location("1:00 PM", "Stop", "30 minutes| You went to market"))
        locations.add(Location("2:00 PM", "Drive", "50 minutes|You went to market"))
        locations.add(Location("3:00 PM", "Walk", "30 minutes | 1km"))
        locations.add(Location("4:00 PM", "Destination", "1 hour ago | 5km"))
        locations.add(Location("5:00 PM", "Stop", "30 minutes | 1km"))
        locations.add(Location("6:00 PM", "Start", "15 minutes| 5km"))
        locations.add(Location("7:00 PM", "Moto", "40 minutes | 3km"))
    }
}

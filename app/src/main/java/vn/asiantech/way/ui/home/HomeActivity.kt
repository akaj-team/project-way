package vn.asiantech.way.ui.home

import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_home.*
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Location
import vn.asiantech.way.extension.toast
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.custom.FloatingButtonHorizontal
import vn.asiantech.way.ui.register.RegisterActivity
import vn.asiantech.way.ui.share.ShareLocationActivity
import vn.asiantech.way.utils.LocationUtil
import kotlin.collections.ArrayList
import android.view.WindowManager
import vn.asiantech.way.ui.search.SearchLocationActivity

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by atHangTran on 26/09/2017.
 */
class HomeActivity : BaseActivity(), OnMapReadyCallback, FloatingButtonHorizontal.OnMenuClickListener {
    companion object {
        const val PADDING_LEFT = 0
        const val PADDING_TOP = 0
        const val PADDING_RIGHT = 0
        const val ZOOM = 16f
    }

    private var mPosition = -1
    private lateinit var mHomeAdapter: HomeAdapter
    private var mGoogleMap: GoogleMap? = null
    private var isExit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initMap()
        initViews()
        fabMenuGroup.setOnMenuItemClickListener(this)
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

    override fun onShareClick() {
        startActivity(Intent(this, SearchLocationActivity::class.java))
    }

    override fun onProfileClick() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.putExtra(RegisterActivity.INTENT_REGISTER, RegisterActivity.INTENT_CODE_HOME)
        startActivity(intent)
    }

    override fun onCalendarClick() {
        // TODO after completed calendar feature
    }

    private fun initViews() {
        setStatusBarTranslucent(true)
    }

    private fun initMap() {
        val supportMapFragment = supportFragmentManager.findFragmentById(R.id.fragmentMap) as? SupportMapFragment
        supportMapFragment?.getMapAsync(this)
    }

    private fun setPaddingGoogleLogo() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        mGoogleMap?.setPadding(PADDING_LEFT, PADDING_TOP, PADDING_RIGHT, size.y / 3)
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
            mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, ZOOM))
        } else {
            toast("Google map is null")
        }
    }

    private fun setDataForRecyclerView() {
        val positions: MutableList<Int> = mutableListOf()
        val locations = ArrayList<Location>()
        // TODO: Get data from share function into locations
        initDummyData(locations)
        mHomeAdapter = HomeAdapter(locations) {
            if (mPosition >= 0) {
                locations[mPosition].isChoose = false
                mHomeAdapter.notifyItemChanged(mPosition)
            }
            positions.add(it)
            if (positions.size > 1) {
                if (it > positions[positions.size - 2]) {
                    recycleViewLocation.scrollToPosition(it + 1)
                } else {
                    recycleViewLocation.scrollToPosition(it - 1)
                }
            }
            locations[it].isChoose = true
            mHomeAdapter.notifyItemChanged(it)
            mPosition = it
        }
        recycleViewLocation.layoutManager = LinearLayoutManager(this)
        recycleViewLocation.adapter = mHomeAdapter
    }

    private fun initDummyData(locations: ArrayList<Location>) {
        locations.add(Location("1:00 PM", "Stop", "30 minutes| You stop at the school..............."))
        locations.add(Location("2:00 PM", "Drive", "50 minutes| You went to the Hoa Khanh market........"))
        locations.add(Location("3:00 PM", "Walk", "30 minutes | 1km"))
        locations.add(Location("4:00 PM", "Destination", "1 hour ago | 5km"))
        locations.add(Location("5:00 PM", "Stop", "30 minutes | 1km"))
        locations.add(Location("6:00 PM", "Start", "15 minutes| 5km"))
        locations.add(Location("7:00 PM", "Moto", "40 minutes | 3km"))
    }

    private fun setStatusBarTranslucent(makeTranslucent: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (makeTranslucent) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }
    }

    override fun onBackPressed() {
        if (isExit) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            toast("Press back again to exit!")
            isExit = true
            Handler().postDelayed({ isExit = false }, 3 * 1000)
        }
    }
}

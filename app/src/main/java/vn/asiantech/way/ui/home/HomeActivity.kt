package vn.asiantech.way.ui.home

import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.setContentView
import vn.asiantech.way.R
import vn.asiantech.way.data.model.TrackingInformation
import vn.asiantech.way.extension.toast
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.custom.FloatingButtonHorizontal
import vn.asiantech.way.ui.group.GroupActivity
import vn.asiantech.way.ui.register.RegisterActivity
import vn.asiantech.way.ui.search.SearchLocationActivity
import vn.asiantech.way.utils.LocationUtil

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
        private const val TYPE_TIME_DELAY = 3000L
        private const val UNIT_PADDING_BOTTOM = 3
    }

    private var mPosition = -1
    private var mGoogleMap: GoogleMap? = null
    private var isExit = false
    private var mIsExpand = false
    private lateinit var mHomeAdapter: HomeAdapter
    private lateinit var mHomeActivityUI: HomeActivityUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDataForRecyclerView()
        mHomeActivityUI = HomeActivityUI(mHomeAdapter)
        initViews()
        initMap()
        mHomeActivityUI.setContentView(this)
        mHomeActivityUI.mFabMenuGroups.setOnMenuItemClickListener(this)
        mHomeActivityUI.mFrOverplay.setOnClickListener {
            if (mIsExpand) {
                mHomeActivityUI.mFabMenuGroups.collapseMenu()
                mIsExpand = false
                setGoneOverLay()
            }
        }
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

    override fun onMenuClick(isShowMenu: Boolean) {
        mHomeActivityUI.mFrOverplay.visibility = if (isShowMenu) View.VISIBLE else View.GONE
        mIsExpand = isShowMenu
    }

    override fun onShareClick() {
        startActivity(Intent(this, SearchLocationActivity::class.java))
        setGoneOverLay()
    }

    override fun onProfileClick() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.putExtra(RegisterActivity.INTENT_REGISTER, RegisterActivity.INTENT_CODE_HOME)
        startActivity(intent)
        setGoneOverLay()
    }

    override fun onCalendarClick() {
        setGoneOverLay()
        // TODO after completed calendar feature
    }

    override fun onSearchClick() {
        startActivity(Intent(this, SearchLocationActivity::class.java))
        setGoneOverLay()
    }

    override fun onGroupClick() {
        startActivity(Intent(this, GroupActivity::class.java))
        setGoneOverLay()
    }

    private fun initViews() {
        setStatusBarTranslucent(true)
    }

    private fun initMap() {
        val supportMapFragment = SupportMapFragment()
        supportFragmentManager.beginTransaction().replace(HomeActivityUI.ID_MAP, SupportMapFragment()).commit()
        supportMapFragment.getMapAsync(this)
    }

    private fun setPaddingGoogleLogo() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        mGoogleMap?.setPadding(PADDING_LEFT, PADDING_TOP, PADDING_RIGHT, size.y / UNIT_PADDING_BOTTOM)
    }

    private fun setGoneOverLay() {
        mHomeActivityUI.mFrOverplay.visibility = View.GONE
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
        val locations = ArrayList<TrackingInformation>()
        // TODO: Get data from share function into locations
        initDummyData(locations)
        mHomeAdapter = HomeAdapter(this, locations) {
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
    }

    private fun initDummyData(locations: ArrayList<TrackingInformation>) {
//        locations.add(TrackingInformation("1:00 PM", "Stop", "30 minutes| You stop at the school...............",
//                LatLng(16.0721115, 108.2302225)))
//        locations.add(TrackingInformation("2:00 PM", "Drive", "50 minutes| You went to the Hoa Khanh market........",
//                LatLng(16.0712047, 108.2193197)))
//        locations.add(TrackingInformation("3:00 PM", "Walk", "30 minutes | 1km", LatLng(16.0721611, 108.2303906)))
//        locations.add(TrackingInformation("4:00 PM", "Destination", "1 hour ago | 5km", LatLng(16.0725051, 108.2296716)))
//        locations.add(TrackingInformation("5:00 PM", "Stop", "30 minutes | 1km", LatLng(16.0717437, 108.2236926)))
//        locations.add(TrackingInformation("6:00 PM", "Start", "15 minutes| 5km", LatLng(16.0712047, 108.2193197)))
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
            Handler().postDelayed({ isExit = false }, TYPE_TIME_DELAY)
        }
    }
}

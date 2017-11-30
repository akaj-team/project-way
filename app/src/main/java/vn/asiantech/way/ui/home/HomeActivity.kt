package vn.asiantech.way.ui.home

import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.dimen
import org.jetbrains.anko.setContentView
import vn.asiantech.way.R
import vn.asiantech.way.data.model.TrackingInformation
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.custom.FloatingButtonHorizontal

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by atHangTran on 26/09/2017.
 */
class HomeActivity : BaseActivity(), OnMapReadyCallback, FloatingButtonHorizontal.OnMenuClickListener {

    companion object {
        private const val UNIT_PADDING_BOTTOM = 3
    }

    private var position = -1
    private var googleMap: GoogleMap? = null
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var ui: HomeActivityUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDataForRecyclerView()
        ui = HomeActivityUI(homeAdapter)
        ui.setContentView(this)
        initViews()
        initMap()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        setPaddingGoogleLogo()
    }

    override fun onMenuClick(isShowMenu: Boolean) {
        //TODO handel when fabMenuGroups click
    }

    override fun onShareClick() {
        // TODO move to Search screen
    }

    override fun onProfileClick() {
        // TODO move to Register screen
    }

    override fun onCalendarClick() {
        // TODO after completed calendar feature
    }

    override fun onSearchClick() {
        // TODO move to Search screen
    }

    override fun onGroupClick() {
        // TODO to  move to Group screen
    }

    private fun initViews() {
        setStatusBarTranslucent(true)
    }

    private fun initMap() {
        val supportMapFragment = SupportMapFragment()
        supportFragmentManager.beginTransaction().replace(R.id.home_activity_fr_map, SupportMapFragment()).commit()
        supportMapFragment.getMapAsync(this)
    }

    private fun setPaddingGoogleLogo() {
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        val padding = dimen(R.dimen.padding_logo_map)
        googleMap?.setPadding(padding, padding, padding, size.y / UNIT_PADDING_BOTTOM)
    }

    private fun setDataForRecyclerView() {
        val positions: MutableList<Int> = mutableListOf()
        val locations = ArrayList<TrackingInformation>()
        // TODO: Get data from share function into locations
        initDummyData(locations)
        homeAdapter = HomeAdapter(this, locations) {
            if (position >= 0) {
                locations[position].isChoose = false
                homeAdapter.notifyItemChanged(position)
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
            homeAdapter.notifyItemChanged(it)
            position = it
        }
    }

    private fun initDummyData(locations: ArrayList<TrackingInformation>) {
        // TODO set list WayLocation
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
}

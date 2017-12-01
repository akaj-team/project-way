package vn.asiantech.way.ui.home

import android.content.Intent
import android.graphics.Point
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.dimen
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.toast
import vn.asiantech.way.R
import vn.asiantech.way.data.model.TrackingInformation
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.custom.FloatingMenuButton
import vn.asiantech.way.ui.group.GroupActivity
import vn.asiantech.way.ui.register.RegisterActivity
import vn.asiantech.way.ui.search.SearchActivity
import vn.asiantech.way.ui.share.ShareActivity
import vn.asiantech.way.utils.LocationUtil

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by atHangTran on 26/09/2017.
 */
class HomeActivity : BaseActivity(), FloatingMenuButton.OnMenuClickListener {

    companion object {
        const val ZOOM = 16f
        private const val TYPE_TIME_DELAY = 3000L
        private const val UNIT_PADDING_BOTTOM = 3
    }

    private var position = -1
    private var googleMap: GoogleMap? = null
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var ui: HomeActivityUI
    private var locations: MutableList<TrackingInformation> = mutableListOf()

    private var isExit = false
    private var mIsExpand = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDataForRecyclerView()
        ui = HomeActivityUI(homeAdapter, this)
        ui.setContentView(this)
        initViews()
        initMap()
        ui.frOverlay.setOnClickListener {
            if (mIsExpand) {
                ui.fabMenuGroup.collapseMenu()
                mIsExpand = false
                setGoneOverLay()
            }
        }

    }


    override fun onMenuClick(isShowMenu: Boolean) {
        ui.frOverlay.visibility = if (isShowMenu) View.VISIBLE else View.GONE
        mIsExpand = isShowMenu
    }

    override fun onShareClick() {
        startActivity(Intent(this, ShareActivity::class.java))
        setGoneOverLay()
    }

    override fun onProfileClick() {
        val intent = Intent(this, RegisterActivity::class.java)
        // TODO to put extra for intent
        startActivity(intent)
        setGoneOverLay()
    }

    override fun onCalendarClick() {
        setGoneOverLay()
        // TODO after completed calendar feature
    }

    override fun onSearchClick() {
        startActivity(Intent(this, SearchActivity::class.java))
        setGoneOverLay()
    }

    override fun onGroupClick() {
        startActivity(Intent(this, GroupActivity::class.java))
        setGoneOverLay()
    }

    override fun onBackPressed() {
        if (isExit) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            toast(resources.getString(R.string.register_double_click_to_exit))
            isExit = true
            Handler().postDelayed({ isExit = false }, TYPE_TIME_DELAY)
        }
    }

    override fun onBindViewModel() {
        // TODO to bind with ViewModel
    }

    private fun initViews() {
        setStatusBarTranslucent(true)
    }

    private fun initMap() {
        val supportMapFragment = SupportMapFragment()
        supportFragmentManager.beginTransaction().replace(R.id.home_activity_fr_map, supportMapFragment).commit()
        supportMapFragment.getMapAsync { googleMap ->
            this.googleMap = googleMap
            setPaddingGoogleLogo()
            val location = LocationUtil(this).getCurrentLocation()
            if (location != null) {
                drawMaker(location)
            } else {
                toast(resources.getString(R.string.not_update_current_location))
            }
        }
    }

    private fun setPaddingGoogleLogo() {
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        val padding = dimen(R.dimen.padding_logo_map)
        googleMap?.setPadding(padding, padding, padding, size.y / UNIT_PADDING_BOTTOM)
    }

    private fun drawMaker(location: Location) {
        if (googleMap != null) {
            googleMap?.clear()
            val currentLocation = LatLng(location.latitude, location.longitude)
            googleMap?.addMarker(MarkerOptions()
                    .position(currentLocation)
                    .draggable(true)
                    .title(resources.getString(R.string.current_location)))
                    ?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_point))
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, ZOOM))
        } else {
            toast(resources.getString(R.string.toast_text_google_map_null))
        }
    }

    private fun setDataForRecyclerView() {
        val positions: MutableList<Int> = mutableListOf()
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

    private fun getHistoryTrackings() {
        TODO("Will update in future")
    }

    private fun setGoneOverLay() {
        ui.frOverlay.visibility = View.GONE
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

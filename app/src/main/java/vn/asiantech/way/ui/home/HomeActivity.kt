package vn.asiantech.way.ui.home

import android.content.Intent
import android.graphics.Point
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.dimen
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import vn.asiantech.way.R
import vn.asiantech.way.data.model.TrackingInformation
import vn.asiantech.way.extension.observeOnUiThread
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
        private const val ZOOM = 16f
        private const val UNIT_PADDING_BOTTOM = 3
    }

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var ui: HomeActivityUI
    private lateinit var homeViewModel: HomeViewModel

    private var locations: MutableList<TrackingInformation> = mutableListOf()
    private var position = -1
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecyclerView()
        ui = HomeActivityUI(homeAdapter, this)
        ui.setContentView(this)
        initViews()
        initMap()
        homeViewModel = HomeViewModel(this)
    }

    override fun onShareClick() {
        startActivity<ShareActivity>()
    }

    override fun onProfileClick() {
        // TODO to put extra for intent
        startActivity<RegisterActivity>()
    }

    override fun onCalendarClick() {
        // TODO after completed calendar feature
    }

    override fun onSearchClick() {
        startActivity<SearchActivity>()
    }

    override fun onGroupClick() {
        startActivity<GroupActivity>()
    }

    override fun onBackPressed() {
        homeViewModel.exitApp()
    }

    override fun onBindViewModel() {
        addDisposables(homeViewModel.getTrackingHistory()
                .observeOnUiThread()
                .subscribe(this::setDataForRecyclerView),
                homeViewModel.exitApp()
                        .subscribe(this::onBackPressButton))
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.resume()
    }

    private fun initViews() {
        setStatusBarTranslucent(true)
    }

    private fun onBackPressButton(isBack: Boolean) {
        if (isBack) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            toast(getString(R.string.register_double_click_to_exit))
        }
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
                toast(getString(R.string.not_update_current_location))
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
                    .title(getString(R.string.current_location)))
                    ?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_point))
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, ZOOM))
        } else {
            toast(getString(R.string.toast_text_google_map_null))
        }
    }

    private fun setDataForRecyclerView(data: MutableList<TrackingInformation>?) {
        data?.let { locations.addAll(it) }
        homeAdapter.notifyDataSetChanged()
    }

    private fun initRecyclerView() {
        val positions: MutableList<Int> = mutableListOf()
        homeAdapter = HomeAdapter(this, locations) {
            if (position >= 0) {
                locations[position].isChoose = false
                homeAdapter.notifyItemChanged(position)
            }
            positions.add(it)
            if (positions.size > 1) {
                if (it > positions[positions.size - 2]) {
                    ui.recycleViewLocation.scrollToPosition(it + 1)
                } else {
                    ui.recycleViewLocation.scrollToPosition(it)
                }
            }
            locations[it].isChoose = true
            homeAdapter.notifyItemChanged(it)
            position = it
        }
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

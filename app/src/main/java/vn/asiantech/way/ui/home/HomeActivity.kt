package vn.asiantech.way.ui.home

import android.content.Intent
import android.graphics.Point
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.view.View
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
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.group.GroupActivity
import vn.asiantech.way.ui.register.RegisterActivity
import vn.asiantech.way.ui.search.SearchActivity
import vn.asiantech.way.ui.share.ShareActivity
import vn.asiantech.way.utils.LocationUtil

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by atHangTran on 26/09/2017.
 */
class HomeActivity : BaseActivity() {

    companion object {
        private const val ZOOM = 16f
        private const val UNIT_PADDING_BOTTOM = 3
    }

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var ui: HomeActivityUI
    private lateinit var viewModel: HomeViewModel
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = HomeViewModel(this)
        homeAdapter = HomeAdapter(viewModel.oldHistoryTracking)
        ui = HomeActivityUI(homeAdapter)
        ui.setContentView(this)
        initViews()
        initMap()
        viewModel.trackingHistory()
    }

    override fun onBackPressed() {
        viewModel.eventBackPressed()
    }

    override fun onBindViewModel() {
        addDisposables(viewModel.updateHistoryTrackingList
                .observeOnUiThread()
                .subscribe(this::handleUpdateHistoryTrackingList),
                viewModel.backStatus.subscribe(this::handleEventBackPressed))
    }

    internal fun eventOnClickItemMenu(view: View) {
        when (view) {
            ui.fabMenuGroup.imgBtnProfile -> startActivity<RegisterActivity>()
            ui.fabMenuGroup.imgBtnGroup -> startActivity<GroupActivity>()
            ui.fabMenuGroup.imgBtnSearch -> startActivity<SearchActivity>()
            ui.fabMenuGroup.imgBtnShare -> startActivity<ShareActivity>()
        }
    }

    internal fun eventOnClickItemRecyclerView(pos: Int) {
        addDisposables(viewModel.getItemPosition(pos)
                .observeOnUiThread()
                .subscribe(this::handleScrollItemRecyclerView))
        viewModel.changeBackgroundItem(pos)
    }

    private fun initViews() {
        setStatusBarTranslucent(true)
    }

    private fun handleScrollItemRecyclerView(pos: Int) {
        ui.recycleViewLocation.scrollToPosition(pos)
    }

    private fun handleEventBackPressed(isBack: Boolean) {
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

    private fun handleUpdateHistoryTrackingList(diff: DiffUtil.DiffResult) {
        diff.dispatchUpdatesTo(homeAdapter)
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

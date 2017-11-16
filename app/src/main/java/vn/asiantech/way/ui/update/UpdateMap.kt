package vn.asiantech.way.ui.update

import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.detail_arrived.*
import kotlinx.android.synthetic.main.show_arrived.*
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Location
import vn.asiantech.way.data.model.arrived.Arrived
import vn.asiantech.way.extension.makeDistance
import vn.asiantech.way.extension.makeDuration
import vn.asiantech.way.extension.toast
import vn.asiantech.way.ui.arrived.DialogArrived
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.custom.FloatingButtonHorizontal
import vn.asiantech.way.ui.home.HomeAdapter
import vn.asiantech.way.ui.register.RegisterActivity
import vn.asiantech.way.ui.search.SearchLocationActivity
import vn.asiantech.way.ui.share.ShareLocationActivity
import vn.asiantech.way.utils.LocationUtil
import vn.asiantech.way.utils.Preference

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by at-hoavo on 25/10/2017.
 */
internal class UpdateMap : BaseActivity(), OnMapReadyCallback,
        FloatingButtonHorizontal.OnMenuClickListener {

// Todo: Get List LatLng from In-progrees tracking to show on recyclerView and draw polyline on map

    companion object {
        private const val PADDING_LEFT = 0
        private const val PADDING_TOP = 0
        private const val PADDING_RIGHT = 0
        private const val ZOOM = 16f
        private const val TYPE_PROGRESS_MAX = 100
        private const val TYPE_POLYLINE_WIDTH = 5f
        private const val TYPE_ANCHOR = 0.5f
        private const val TYPE_TIME_DELAY = 3000L
        private const val UNIT_PADDING_BOTTOM = 3
        private const val BEGIN_LAT = 16.0721115
        private const val BEGIN_LONG = 108.2302225
        private const val DESTINATION_LAT = 16.0712047
        private const val DESTINATION_LONG = 108.2193197

    }

    private var mPosition = -1
    private lateinit var mHomeAdapter: HomeAdapter
    private var mGoogleMap: GoogleMap? = null
    private var isExit = false
    private var mIsExpand = false
    private lateinit var mDestination: LatLng
    private lateinit var mBegin: LatLng
    private var mArrived = Arrived()
    private var mLocations: MutableList<Location> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Preference.init(this)
        Log.d("zxc", "action type " + Preference().getActionType())
        initMap()
        initViews()
        fabMenuGroup.setOnMenuItemClickListener(this)
        frOverlay.setOnClickListener {
            if (mIsExpand) {
                fabMenuGroup.collapseMenu()
                mIsExpand = false
                setGoneOverLay()
            }
        }
        initData()
        setDataForRecyclerView()
        btnShowSummary.setOnClickListener {
            showDialog()
        }

        imgArrowRight.setOnClickListener {
            showDetailTracking()
        }

        imgArrowDown.setOnClickListener {
            setArrowDownClick()
        }

        imgArrowRightStartItem.setOnClickListener {
            setArrowRightStartItemClick()
        }

        imgArrowRightEndItem.setOnClickListener {
            setArrowRightEndItemClick()
        }

        imgArrowDropDownStartItem.setOnClickListener {
            setArrowDropDownStartItemClick()
        }

        imgArrowDropDownEndItem.setOnClickListener {
            setArrowDropDownEndItemClick()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mGoogleMap = googleMap
        setPaddingGoogleLogo()
        val location = LocationUtil(this).getCurrentLocation()
        if (location != null) {
            drawCurrentMaker(location)
            // Todo: Set arrived screen when current position = destination
//            setArrived()
        } else {
            toast(resources.getString(R.string.not_update_current_location))
        }
    }

    override fun onMenuClick(isShowMenu: Boolean) {
        frOverlay.visibility = if (isShowMenu) View.VISIBLE else View.GONE
        mIsExpand = isShowMenu
    }

    override fun onShareClick() {
        startActivity(Intent(this, ShareLocationActivity::class.java))
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

    private fun initViews() {
        setStatusBarTranslucent(true)
    }

    private fun initMap() {
        val supportMapFragment = supportFragmentManager.
                findFragmentById(R.id.fragmentMap) as? SupportMapFragment
        supportMapFragment?.getMapAsync(this)
    }

    private fun setPaddingGoogleLogo() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        mGoogleMap?.setPadding(PADDING_LEFT, PADDING_TOP, PADDING_RIGHT,
                size.y / UNIT_PADDING_BOTTOM)
    }

    private fun setGoneOverLay() {
        frOverlay.visibility = View.GONE
    }

    private fun drawCurrentMaker(location: android.location.Location) {
        if (mGoogleMap != null) {
            mGoogleMap?.clear()
            val currentLocation = LatLng(location.latitude, location.longitude)
            addMarker(MarkerOptions()
                    .position(currentLocation)
                    .draggable(true)
                    .title(resources.getString(R.string.current_location)).icon(BitmapDescriptorFactory.
                    fromResource(R.drawable.ic_current_point)), currentLocation)
        } else {
            toast(resources.getString(R.string.toast_text_google_map_null))
        }
    }

    private fun addMarker(markerOptions: MarkerOptions, position: LatLng) {
        mGoogleMap?.addMarker(markerOptions)
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(position, ZOOM))
    }

    private fun setMarkerOption(resource: Int, position: LatLng): MarkerOptions =
            MarkerOptions().position(position).
                    icon(BitmapDescriptorFactory.fromResource(resource))
                    .anchor(TYPE_ANCHOR, TYPE_ANCHOR)

    private fun drawLine(points: List<LatLng>) {
        val polyPointOption = PolylineOptions()
        points.let {
            polyPointOption.addAll(it)
        }
        polyPointOption.color(Color.BLACK)
        polyPointOption.width(TYPE_POLYLINE_WIDTH)
        mGoogleMap?.addPolyline(polyPointOption)
    }

    private fun updateMap(points: List<LatLng>) {
        mGoogleMap?.clear()
        addMarker(setMarkerOption(R.drawable.ic_ht_source_place_marker, mBegin), mBegin)
        if (points.size > 1) {
            if (points[points.size - 1] != mDestination) {
                addMarker(setMarkerOption(R.drawable.ic_rectangle, points[points.size - 1]),
                        points[points.size - 1])
            } else {
                addMarker(setMarkerOption(R.drawable.ic_ht_expected_place_marker,
                        points[points.size - 1]), points[points.size - 1])
            }
            drawLine(points)
        }
    }

    private fun setDataForRecyclerView() {
        val positions: MutableList<Int> = mutableListOf()
        mHomeAdapter = HomeAdapter(mLocations) {
            if (mPosition >= 0) {
                mLocations[mPosition].isChoose = false
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
            mLocations[it].isChoose = true
            mHomeAdapter.notifyItemChanged(it)
            mPosition = it
            updateMap(setListToPosition(it))
        }
        recycleViewLocation.layoutManager = LinearLayoutManager(this)
        recycleViewLocation.adapter = mHomeAdapter
    }

    private fun setListToPosition(position: Int): List<LatLng> =
            (0..position).map { mLocations[it].point }

    private fun initData() {
        Preference().getTrackingHistory()?.let { mLocations.addAll(it) }
        mArrived.latLngs = mutableListOf()
        mLocations.forEach {
            it.point.let {
                mArrived.latLngs?.add(it)
            }
        }
        mBegin = LatLng(BEGIN_LAT, BEGIN_LONG)
        mDestination = LatLng(DESTINATION_LAT, DESTINATION_LONG)
    }

    private fun setArrived() {
        recycleViewLocation.visibility = View.GONE
        rlLayoutArrived.visibility = View.VISIBLE
        mArrived.latLngs?.toList()?.let {
            updateMap(it)
        }
        btnShowSummary.visibility = View.VISIBLE
        progressBarCircular.progress = TYPE_PROGRESS_MAX
        tvTimeTotalArrived.text = mArrived.time.makeDuration(this)
        tvDistanceArrived.text = mArrived.distance.makeDistance(this)
    }

    private fun showDialog() {
        val dialog = DialogArrived.newInstance(mArrived.time, mArrived.distance,
                mArrived.averageSpeed)
        val fragmentManager = supportFragmentManager as? FragmentManager
        dialog.show(fragmentManager, resources.getString(R.string.arrived_dialog_tag))
    }

    private fun showDetailTracking() {
        imgArrowRight.visibility = View.GONE
        imgArrowDown.visibility = View.VISIBLE
        cardViewDetailArrived.visibility = View.VISIBLE
        tvStartTime.text = mArrived.dateTimeFirst
        tvStartAddress.text = mArrived.firstLocation
        tvEndTime.text = mArrived.dateTimeEnd
        tvEndAddress.text = mArrived.endLocation
    }

    private fun setArrowDownClick() {
        imgArrowDown.visibility = View.GONE
        imgArrowRight.visibility = View.VISIBLE
        cardViewDetailArrived.visibility = View.GONE
    }

    private fun setArrowRightStartItemClick() {
        imgArrowRightStartItem.visibility = View.GONE
        imgArrowDropDownStartItem.visibility = View.VISIBLE
        tvStartAddress.visibility = View.VISIBLE
    }

    private fun setArrowDropDownEndItemClick() {
        imgArrowDropDownEndItem.visibility = View.GONE
        imgArrowRightEndItem.visibility = View.VISIBLE
        tvEndAddress.visibility = View.GONE
    }

    private fun setArrowDropDownStartItemClick() {
        imgArrowDropDownStartItem.visibility = View.GONE
        imgArrowRightStartItem.visibility = View.VISIBLE
        tvStartAddress.visibility = View.GONE
    }

    private fun setArrowRightEndItemClick() {
        imgArrowRightEndItem.visibility = View.GONE
        imgArrowDropDownEndItem.visibility = View.VISIBLE
        tvEndAddress.visibility = View.VISIBLE
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
            toast(resources.getString(R.string.register_double_click_to_exit))
            isExit = true
            Handler().postDelayed({ isExit = false }, TYPE_TIME_DELAY)
        }
    }
}

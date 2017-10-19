package vn.asiantech.way.ui.arrived

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.hypertrack.lib.HyperTrack
import com.hypertrack.lib.callbacks.HyperTrackCallback
import com.hypertrack.lib.models.ErrorResponse
import com.hypertrack.lib.models.HyperTrackLocation
import com.hypertrack.lib.models.SuccessResponse
import kotlinx.android.synthetic.main.activity_arrived.*
import kotlinx.android.synthetic.main.detail_arrived.*
import vn.asiantech.way.R
import vn.asiantech.way.data.model.arrived.Arrived
import vn.asiantech.way.extension.makeDistance
import vn.asiantech.way.extension.makeDuration
import vn.asiantech.way.ui.base.BaseActivity

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by at-hoavo on 26/09/2017.
 */
internal class ArrivedActivity : BaseActivity(), OnMapReadyCallback {
    companion object {
        private val TAG = ArrivedActivity::class.toString()
        private const val REQUEST_CODE_PERMISSION = 200
        private const val VERSION_SDK = 23
        private const val TYPE_PROGRESS_MAX = 100
        private const val TYPE_CAMERA_ZOOM = 14f
        private const val TYPE_MAP_ZOOM = 16.9f
        private const val TYPE_POLYLINE_WIDTH = 5f
        private const val TYPE_PADDING_BOTTOM = 500
        private const val TYPE_PADDING_TOP = 0
        private const val TYPE_PADDING_RIGHT = 0
        private const val TYPE_PADDING_LEFT = 0
        private const val TYPE_ANCHOR = 0.5f
    }

    private var mGoogleMap: GoogleMap? = null
    private lateinit var mDestinationPosition: LatLng
    private var mArrived = Arrived()
    private var mCurrentLocation: HyperTrackLocation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arrived)
        setArrivedDetail()
        checkGPS()
        askPermissionsAccessLocation()
        val supportMapFragment = fragmentGoogleMap as? SupportMapFragment
        supportMapFragment?.getMapAsync(this)

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

        imgBtnArrowBack.setOnClickListener {
            //TODO("Back to previous screen")
        }

        imgBtnResetPosition.setOnClickListener {
            mCurrentLocation?.latLng?.let {
                mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(it, TYPE_CAMERA_ZOOM))
            }
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        mGoogleMap = p0
        setOnMapReady()
        getCurrentLocation()
    }

    private fun setArrivedDetail() {
        //TODO("Set for mArrived")
        mArrived.latLngs = mutableListOf()
//        mArrived.latLngs?.add(LatLng(16.0751387, 108.1538494))
//        mArrived.dateTimeFirst = "3:08 CH,th 10 17"
//        mArrived.dateTimeEnd = "5:00 CH,th 10 17"
//        mArrived.firstLocation = "Đường số 4, An Hải Bắc, KCN An Đồn, Đà Nẵng, Việt Nam"
//        mArrived.endLocation = "Đường số 4, An Hải Bắc, KCN An Đồn, Đà Nẵng, Việt Nam"
    }

    private fun checkGPS() {
        val manager = getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        manager?.let {
            if (!it.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(getString(R.string.dialog_message_enable_gps))
                        .setPositiveButton(getString(R.string.dialog_button_ok)) { dialogInterface, _ ->
                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                            dialogInterface.dismiss()
                        }
                        .setNegativeButton(getString(R.string.dialog_button_cancel)) { dialogInterface, _ ->
                            dialogInterface.cancel()
                        }
                builder.create().show()
            }
        }
    }

    private fun askPermissionsAccessLocation() {
        // Ask for permission with API >= 23
        if (Build.VERSION.SDK_INT >= VERSION_SDK) {
            val accessFineLocationPermission = ContextCompat
                    .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            if (accessFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
                // Permissions
                val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                // Dialog
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION)
            } else {
                // No-op
            }
        }
    }

    private fun setOnMapReady() {
        mGoogleMap?.setPadding(TYPE_PADDING_LEFT, TYPE_PADDING_TOP, TYPE_PADDING_RIGHT,
                TYPE_PADDING_BOTTOM)
        mGoogleMap?.setMaxZoomPreference(TYPE_MAP_ZOOM)
        mGoogleMap?.uiSettings?.isMapToolbarEnabled = false
        mGoogleMap?.uiSettings?.isCompassEnabled = false
        mArrived.latLngs?.let {
            if (it.size > 0) {
                mGoogleMap?.addMarker(setMarkerOption(R.drawable.ic_ht_source_place_marker, it[0]))
            }
        }
    }

    private fun getCurrentLocation() {
        HyperTrack.getCurrentLocation(object : HyperTrackCallback() {
            override fun onSuccess(response: SuccessResponse) {
                Log.d(TAG, "onSuccess: Current Location Received")
                mCurrentLocation = HyperTrackLocation(response.responseObject as? Location)
                mCurrentLocation?.latLng?.let {
                    mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(it,
                            TYPE_CAMERA_ZOOM))
                    mGoogleMap?.addMarker(setMarkerOption(R.drawable.ic_ht_expected_place_marker,
                            it))
                    mArrived.latLngs?.add(it)
                    mDestinationPosition = it
                    if (checkDestination()) {
                        arrivedFinish()
                    }
                }
            }

            override fun onError(errorResponse: ErrorResponse) {
                Log.d(TAG, "onError: Current Location Receiving error")
                Log.d(TAG, "onError: " + errorResponse.errorMessage)
            }
        })
    }

    private fun setMarkerOption(resource: Int, position: LatLng): MarkerOptions =
            MarkerOptions().position(position).
                    icon(BitmapDescriptorFactory.fromResource(resource))
                    .anchor(TYPE_ANCHOR, TYPE_ANCHOR)

    private fun checkDestination(): Boolean = mCurrentLocation?.latLng?.latitude ==
            mDestinationPosition.latitude
            && mCurrentLocation?.latLng?.longitude == mDestinationPosition.longitude

    private fun arrivedFinish() {
        btnShowSummary.visibility = View.VISIBLE
        progressBarCircular.progress = TYPE_PROGRESS_MAX
        tvTimeTotalArrived.text = mArrived.time.makeDuration(this)
        tvDistanceArrived.text = mArrived.distance.makeDistance(this)
        drawLine()
    }

    private fun drawLine() {
        val polyPointOption = PolylineOptions()
        mArrived.latLngs?.let {
            polyPointOption.addAll(it)
        }
        polyPointOption.color(Color.BLACK)
        polyPointOption.width(TYPE_POLYLINE_WIDTH)
        mGoogleMap?.addPolyline(polyPointOption)
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
}

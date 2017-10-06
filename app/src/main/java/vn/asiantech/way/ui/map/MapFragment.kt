package vn.asiantech.way.ui.map

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*
import vn.asiantech.way.R
import vn.asiantech.way.extension.toast
import vn.asiantech.way.utils.GPSUtil

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 06/10/2017.
 */
class MapFragment : Fragment(), OnMapReadyCallback {
    private var mGoogleMap: GoogleMap? = null

    override fun onMapReady(googleMap: GoogleMap?) {
        MapsInitializer.initialize(activity)
        checkGPS()
        mGoogleMap = googleMap
        val location = GPSUtil(activity).getCurrentLocation()
        if (location != null) {
            drawMaker(location)
        } else {
            activity.toast(getString(R.string.toast_text_location_null))
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?
                              , savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map?.onCreate(null)
        map?.onResume()
        map?.getMapAsync(this)
    }

    private fun drawMaker(location: android.location.Location) {
        if (mGoogleMap != null) {
            mGoogleMap?.clear()
            val currentLocation = LatLng(location.latitude, location.longitude)
            mGoogleMap?.addMarker(MarkerOptions()
                    .position(currentLocation)
                    .draggable(true)
                    .title(getString(R.string.marker_title)))
                    ?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_location))
            mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16f))
        } else {
            activity.toast(getString(R.string.toast_text_google_map_null))
        }
    }

    private fun checkGPS() {
        val manager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val builder = AlertDialog.Builder(activity)
            builder.setMessage(getString(R.string.dialog_message_enable_gps))
                    .setPositiveButton(getString(R.string.dialog_button_ok)) { dialogInterface, _ ->
                        activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        dialogInterface.dismiss()
                    }
                    .setNegativeButton(getString(R.string.dialog_button_cancel)) { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }
            builder.create().show()
        }
    }

    override fun onResume() {
        super.onResume()
        val location = GPSUtil(activity).getCurrentLocation()
        if (location != null) {
            drawMaker(location)
        } else {
            activity.toast(getString(R.string.toast_text_location_null))
        }
    }
}

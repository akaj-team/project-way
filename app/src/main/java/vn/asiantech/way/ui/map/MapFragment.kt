package vn.asiantech.way.ui.map

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.provider.Settings
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*
import vn.asiantech.way.R
import vn.asiantech.way.extension.toast
import vn.asiantech.way.utils.LocationUtil

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 06/10/2017.
 */
internal class MapFragment : Fragment(), OnMapReadyCallback {
    private companion object {
        private const val LEVEL_ZOOM = 16f
    }

    private var mGoogleMap: GoogleMap? = null

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        MapsInitializer.initialize(activity)
        checkGPS()
        mGoogleMap = googleMap
        val location = LocationUtil(activity).getCurrentLocation()
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
            mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, LEVEL_ZOOM))
        } else {
            activity.toast(getString(R.string.toast_text_google_map_null))
        }
    }

    private fun checkGPS() {
        val manager = activity.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        if (!manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
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

    fun drawLocationMarker(location: android.location.Location) {
        val currentLocation = LatLng(location.latitude, location.longitude)
        mGoogleMap?.addMarker(MarkerOptions()
                .position(currentLocation)
                .draggable(true)
                .title(getString(R.string.marker_title)))
                ?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_hero_marker))
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, LEVEL_ZOOM))
    }

    private fun animateMarker(marker: Marker, latLng: LatLng, hideMarker: Boolean) {
        Log.d("at-dinhvo", "animateMarker")
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val projection = mGoogleMap?.projection
        val startPoint = projection?.toScreenLocation(marker.position)
        val startLatLng = projection?.fromScreenLocation(startPoint)
        val interpolator = LinearInterpolator()
        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = interpolator.getInterpolation((elapsed.toFloat()) / 500L)
                val lng = t * latLng.longitude + (1 - t) * startLatLng!!.longitude
                val lat = t * latLng.latitude + (1 - t) * startLatLng.latitude
                Log.d("at-dinhvo", "animateMarker:$lat - $lng")
                marker.position = LatLng(lat, lng)
                if (t < 1.0) {
                    handler.postDelayed(this, 16)
                } else {
                    marker.isVisible = !hideMarker
                }
            }
        })
    }
}

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
import com.google.android.gms.maps.*
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
            activity.toast("Not update current location!")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map?.onCreate(null)
        map?.onResume()
        map?.getMapAsync(this)
    }

    /**
     * Draw current marker
     */
    private fun drawMaker(location: android.location.Location) {
        if (mGoogleMap != null) {
            mGoogleMap!!.clear()
            val currentLocation = LatLng(location.latitude, location.longitude)
            mGoogleMap!!.addMarker(MarkerOptions()
                    .position(currentLocation)
                    .draggable(true)
                    .title("Current location"))
                    .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_location))
            mGoogleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16f))
        } else {
            activity.toast("Google map is null")
        }
    }

    /**
     * Check your GPS enable
     */
    private fun checkGPS() {
        val manager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val builder = AlertDialog.Builder(activity)
            builder.setMessage("App need GPS.Do you want enable your GPS?")
                    .setPositiveButton("OK") { dialogInterface, _ ->
                        activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        dialogInterface.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialogInterface, _ ->
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
            activity.toast("Not update current location!")
        }
    }
}
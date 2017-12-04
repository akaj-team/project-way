package vn.asiantech.way.utils

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by atHangTran on 29/09/2017.
 */
class LocationUtil(private val context: Context) : LocationListener {

    companion object {
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES = 5L
        private const val MIN_TIME_BW_UPDATES = 1000L
        private const val TAG = "Error"
    }

    private var isGPSEnabled = false
    private var canGetLocation = false
    private var isNetworkEnabled = false
    private var location: Location? = null
    private var locationManager: LocationManager? = null

    /**
     * To get the current location
     *
     * @return location is the current location
     */
    fun getCurrentLocation(): Location? {
        if (canGetLocation()) {
            canGetLocation = true
            try {
                locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                checkGPSOrWifi()
                if (isNetworkEnabled) {
                    locationManager?.let {
                        it.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER
                                , MIN_TIME_BW_UPDATES
                                , MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                        location = it.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    }
                    if (location != null) {
                        locationManager?.let {
                            location = it.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        }
                    }
                }
            } catch (e: SecurityException) {
                Log.e(TAG, e.toString())
            }
        } else {
            Log.d(TAG, "Can not get location!")
        }
        return location
    }

    private fun canGetLocation(): Boolean {
        locationManager = context
                .getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        checkGPSOrWifi()
        canGetLocation = !(!isGPSEnabled && !isNetworkEnabled)
        return canGetLocation
    }

    private fun checkGPSOrWifi() {
        locationManager?.let {
            // getting GPS status
            isGPSEnabled = it.isProviderEnabled(LocationManager.GPS_PROVIDER)

            // getting wifi status
            isNetworkEnabled = it.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }
    }

    override fun onLocationChanged(location: Location) {}

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}
}

package vn.asiantech.way.util

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
class LocationUtil(private val mContext: Context) : LocationListener {

    companion object {
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES = 5L
        private const val MIN_TIME_BW_UPDATES = 1000L
        private const val TAG = "Error"
    }

    private var mIsGPSEnabled = false
    private var mCanGetLocation = false
    private var mIsNetworkEnabled = false
    private var mLocation: Location? = null
    private var mTurnOnGps: TurnOnGPS? = null
    private var mLocationManager: LocationManager? = null

    fun getCurrentLocation(): Location? {
        if (canGetLocation()) {
            mCanGetLocation = true
            try {
                mLocationManager = mContext
                        .getSystemService(Context.LOCATION_SERVICE) as LocationManager

                // getting GPS status
                mIsGPSEnabled = mLocationManager!!
                        .isProviderEnabled(LocationManager.GPS_PROVIDER)

                // getting network status
                mIsNetworkEnabled = mLocationManager!!
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                if (mIsNetworkEnabled) {
                    mLocationManager!!.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER
                            , MIN_TIME_BW_UPDATES
                            , MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                    mLocation = mLocationManager!!
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                    if (mLocationManager != null) {
                        mLocation = mLocationManager!!
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    }
                }
            } catch (e: SecurityException) {
                Log.e(TAG, e.toString())
            }
        } else {
            Log.d(TAG, "Can not get location!")
        }
        return mLocation
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    private fun canGetLocation(): Boolean {
        mLocationManager = mContext
                .getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mIsGPSEnabled = mLocationManager!!
                .isProviderEnabled(LocationManager.GPS_PROVIDER)
        mIsNetworkEnabled = mLocationManager!!
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        mCanGetLocation = !(!mIsGPSEnabled && !mIsNetworkEnabled)
        return mCanGetLocation
    }

    override fun onLocationChanged(location: Location) {
        if (mTurnOnGps != null) {
            mTurnOnGps!!.onChangeLocation(location)
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}

    /**
     * To handler click do not open GPS
     */
    interface TurnOnGPS {
        fun onChangeLocation(location: Location)
    }
}

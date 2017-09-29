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
class GPSUtil(private val mContext: Context) : LocationListener {

    companion object {
        private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 5L
        private val MIN_TIME_BW_UPDATES: Long = 1000L
        private val TAG = GPSUtil::class.java.simpleName
    }

    private var mLatitude = 0.toDouble()
    private var mLongitude = 0.toDouble()
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
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                    mLocation = mLocationManager!!
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                    if (mLocationManager != null) {
                        mLocation = mLocationManager!!
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    }

                    if (mLocation != null) {
                        mLatitude = mLocation!!.latitude
                        mLongitude = mLocation!!.longitude
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (mIsGPSEnabled && mLocation == null) {
                    mLocationManager!!.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                    mLocation = mLocationManager!!
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER)

                    if (mLocation != null) {
                        mLatitude = mLocation!!.latitude
                        mLongitude = mLocation!!.longitude
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
        this.mCanGetLocation = !(!mIsGPSEnabled && !mIsNetworkEnabled)
        return this.mCanGetLocation
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
     * To handler click do not open gps
     */
    interface TurnOnGPS {
        fun onChangeLocation(location: Location)
    }
}

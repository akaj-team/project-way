package vn.asiantech.way.utils

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 06/10/2017.
 */
class GPSUtil(private val mContext: Context) : LocationListener, Service() {

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
    /**
     * Get Current Location
     * @return location your location
     */
    internal fun getCurrentLocation(): Location? {
        if (canGetLocation()) {
            mCanGetLocation = true
            try {
                mLocationManager = mContext
                        .getSystemService(Context.LOCATION_SERVICE) as LocationManager

                // getting GPS status
                mLocationManager?.let {
                    mIsGPSEnabled = it.isProviderEnabled(LocationManager.GPS_PROVIDER)
                }

                // getting network status
                mLocationManager?.let {
                    mIsNetworkEnabled = it.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                }

                if (mIsNetworkEnabled) {
                    mLocationManager?.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                    mLocationManager?.let {
                        mLocation = it.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    }
                    if (mLocationManager != null) {
                        mLocationManager?.let {
                            mLocation = it.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        }
                    }

                    if (mLocation != null) {
                        mLocation?.let {
                            mLatitude = it.latitude
                            mLongitude = it.longitude
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (mIsGPSEnabled && mLocation == null) {
                    mLocationManager?.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                    mLocationManager?.let {
                        mLocation = it.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    }

                    if (mLocation != null) {
                        mLocation?.let {
                            mLatitude = it.latitude
                            mLongitude = it.longitude
                        }
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

    private fun canGetLocation(): Boolean {
        mLocationManager = mContext
                .getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Check GPS/Wifi enabled
        mLocationManager?.let {
            mIsGPSEnabled = it.isProviderEnabled(LocationManager.GPS_PROVIDER)
            mIsNetworkEnabled = it.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }
        this.mCanGetLocation = !(!mIsGPSEnabled && !mIsNetworkEnabled)
        return this.mCanGetLocation
    }

    override fun onLocationChanged(location: Location) {
        if (mTurnOnGps != null) {
            mTurnOnGps?.onChangeLocation(location)
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}

    override fun onBind(p0: Intent?): IBinder? = null


    /**
     * To handler click do not open gps
     */
    internal interface TurnOnGPS {
        fun onChangeLocation(location: Location)
    }
}

package vn.asiantech.way.utils

import android.annotation.SuppressLint
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
 * Created by toan on 28/09/2017.
 */
class GPSUtil(var context: Context) : Service(), LocationListener {
    companion object {
        // The minimum distance to change Updates in meters
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES = 5
        // The minimum time between updates in milliseconds
        private const val MIN_TIME_BW_UPDATES = 1000
    }

    val TAG = GPSUtil.javaClass.simpleName
    var turnOnGps: TurnOnGPS? = null
    var mCanGetLocation = false
    var mLocation: Location? = null
    var mLatitude: Double? = 0.toDouble()
    var mLongitude: Double? = 0.toDouble()
    var mIsGPSEnabled: Boolean? = false
    var mIsNetworkEnabled: Boolean? = false
    var mLocationManager: LocationManager? = null
    var mIsShow: Boolean? = false

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onLocationChanged(location: Location?) {
        turnOnGps?.onChangeLocation(location!!)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        TODO("Will update late")
    }

    override fun onProviderEnabled(provider: String?) {
        TODO("Will update late")
    }

    override fun onProviderDisabled(provider: String?) {
        TODO("Will update late")
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(): Location {
        this.mCanGetLocation = true
        try {
            mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            mIsGPSEnabled = mLocationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
            mIsNetworkEnabled = mLocationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (mIsNetworkEnabled!!) {
                mLocationManager?.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES.toLong(),
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                mLocation = mLocationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (mLocationManager != null) {
                    mLocation = mLocationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }
                if (mLocation != null) {
                    mLatitude = mLocation?.latitude
                    mLongitude = mLocation?.longitude
                }
            }
        } catch (e: SecurityException) {
            Log.e(TAG, e.toString())
        }
        return mLocation!!
    }

    fun getLatitude(): Double {
        if (mLocation != null) {
            mLatitude = mLocation?.getLatitude()
        }
        return mLatitude!!
    }

    fun getLongitude(): Double {
        if (mLocation != null) {
            mLongitude = mLocation?.getLongitude()
        }
        return mLongitude!!
    }

    interface TurnOnGPS {
        fun onChangeLocation(location: Location)
    }
}
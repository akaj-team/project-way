package vn.asiantech.way.ui.share

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.BatteryManager
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.hypertrack.lib.HyperTrack
import com.hypertrack.lib.callbacks.HyperTrackCallback
import com.hypertrack.lib.models.*
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import vn.asiantech.way.data.model.LocationRoad
import vn.asiantech.way.data.model.Rows
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.utils.AppConstants
import java.text.SimpleDateFormat
import java.util.*

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 04/12/2017.
 */
class ShareViewModel(val context: Context) {
    var batteryCapacity = BehaviorSubject.create<Int>()
    val result: BehaviorSubject<HyperTrackLocation> = BehaviorSubject.create<HyperTrackLocation>()
    private val resultDistancePerSecond = BehaviorSubject.create<List<Float>>()
    private val resultTimeDuring = BehaviorSubject.create<String>()
    private val wayRepository = WayRepository()
    private val mCurrentBatteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val level = p1?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            level?.let { batteryCapacity.onNext(it) }
        }
    }

    companion object {
        private val ONE_MINUTE_VALUE = 60
        private val ONE_HOUR_VALUE = 3600
        private val HOUR_VALUE = 3599
        private val NUMBER_FOUR = 4.0
    }

    init {
        context.registerReceiver(mCurrentBatteryReceiver, IntentFilter(Intent
                .ACTION_BATTERY_CHANGED))
    }

    internal fun getLocationDistance(origins: String, destinations: String)
            : Observable<List<Rows>> {
        return wayRepository.getLocationDistance(origins, destinations)
                .observeOnUiThread()
                .map { it.rows }
    }

    internal fun getListLocationWhenReOpen(url: String): Observable<List<LocationRoad>> {
        return wayRepository.getListLocation(url)
                .observeOnUiThread()
                .map { it.snappedPoints }
    }

    internal fun getTrackingURL(): Observable<String> {
        val link = BehaviorSubject.create<String>()
        val builder = ActionParamsBuilder()
        HyperTrack.createAndAssignAction(builder.build(), object : HyperTrackCallback() {
            override fun onSuccess(response: SuccessResponse) {
                if (response.responseObject != null) {
                    val action = response.responseObject as? Action
                    HyperTrack.clearServiceNotificationParams()
                    link.onNext(action?.trackingURL.toString())
                }
            }

            override fun onError(errorResponse: ErrorResponse) {
                Log.d("zxc", "at-datbui " + errorResponse.errorMessage)
            }
        })
        return link
    }

    internal fun getLocationName(latLng: LatLng): Observable<String> {
        val locationName = BehaviorSubject.create<String>()
        val geoCoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (addresses.isNotEmpty()) {
            val address: Address = addresses[0]
            locationName.onNext(address.getAddressLine(0))
        } else {
            locationName.onNext("")
        }
        return locationName
    }

    internal fun getCalendarDateTime(): Observable<String> {
        val result = BehaviorSubject.create<String>()
        val calendar = Calendar.getInstance().time
        val simple = SimpleDateFormat("KK:mm a', Th'MM dd", Locale.ENGLISH)
        result.onNext(simple.format(calendar))
        return result
    }

    internal fun getCurrentLocation() {
        HyperTrack.getCurrentLocation(object : HyperTrackCallback() {
            override fun onSuccess(p0: SuccessResponse) {
                val hyperTrackLocation = HyperTrackLocation((p0.responseObject) as Location?)
                result.onNext(hyperTrackLocation)
            }

            override fun onError(p0: ErrorResponse) {
                Log.d("at-dinhvo", "ErrorResponse: " + p0.errorMessage)
            }
        })
    }

    internal fun compareLocation(currentLatLng: LatLng, destinationLatLng: LatLng): Observable<Boolean> {
        val currentLat = String.format("%.3f", currentLatLng.latitude)
        val currentLng = String.format("%.3f", currentLatLng.longitude)
        val destinationLat = String.format("%.3f", destinationLatLng.latitude)
        val destinationLng = String.format("%.3f", destinationLatLng.longitude)
        val result = BehaviorSubject.create<Boolean>()
        result.onNext(currentLat == destinationLat && currentLng == destinationLng)
        return result
    }

    internal fun getDistancePerSecond(source: LatLng, destination: LatLng): Observable<List<Float>> {
        val des = Location("Point")
        des.latitude = source.latitude
        des.longitude = source.longitude
        val src = Location("Point")
        src.latitude = destination.latitude
        src.longitude = destination.longitude
        val list = mutableListOf<Float>()
        list.add((src.distanceTo(des) * AppConstants.TIME_CONVERT).toFloat())
        list.add(src.distanceTo(des) / AppConstants.SECOND_VALUE)
        resultDistancePerSecond.onNext(list)
        return resultDistancePerSecond
    }

    internal fun getTimeDuringStatus(time: Long): Observable<String> {
        val stringTime: String = when {
            time in ShareViewModel.ONE_MINUTE_VALUE..ShareViewModel.HOUR_VALUE -> (time / ShareViewModel
                    .ONE_MINUTE_VALUE).toString().plus("min")
            time < ShareViewModel.ONE_MINUTE_VALUE -> time.toString().plus("second")
            else -> (time / ShareViewModel.ONE_HOUR_VALUE).toString().plus("hour")
        }
        resultTimeDuring.onNext(stringTime)
        return resultTimeDuring
    }

    private fun radians(n: Double) = n * (Math.PI / (AppConstants.RADIUS / 2))

    private fun degrees(n: Double) = n * ((AppConstants.RADIUS / 2) / Math.PI)

    internal fun getAngleMarker(startLatLong: LatLng, endLatLong: LatLng): Observable<Float> {
        val startLat = radians(startLatLong.latitude)
        val startLong = radians(startLatLong.longitude)
        val endLat = radians(endLatLong.latitude)
        val endLong = radians(endLatLong.longitude)
        var deltaLong = endLong - startLong
        val deltaPhi = Math.log(Math.tan(endLat / 2.0 + Math.PI / ShareViewModel.NUMBER_FOUR) / Math.tan
        (startLat / 2.0 + Math.PI / ShareViewModel.NUMBER_FOUR))
        if (Math.abs(deltaLong) > Math.PI) {
            deltaLong = if (deltaLong > 0.0) {
                -(2.0 * Math.PI - deltaLong)
            } else {
                (2.0 * Math.PI + deltaLong)
            }
        }
        val result = BehaviorSubject.create<Float>()
        result.onNext(((degrees(Math.atan2(deltaLong, deltaPhi)) + AppConstants.RADIUS) %
                AppConstants.RADIUS).toFloat())
        return result
    }
}

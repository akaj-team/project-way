package vn.asiantech.way.ui.share

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.BatteryManager
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.SingleSubject
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.utils.AppConstants
import java.text.SimpleDateFormat
import java.util.*

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 04/12/2017.
 */
class ShareViewModel(private val wayRepository: WayRepository) {
    var batteryCapacity: BehaviorSubject<Int> = BehaviorSubject.create<Int>()
    private val resultDistancePerSecond = SingleSubject.create<List<Float>>()
    private val resultTimeDuring = SingleSubject.create<String>()
    private val currentBatteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val level = p1?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            level?.let { batteryCapacity.onNext(it) }
        }
    }

    companion object {
        private const val ONE_MINUTE_VALUE = 60
        private const val ONE_HOUR_VALUE = 3600
        private const val HOUR_VALUE = 3599
        private const val NUMBER_FOUR = 4.0
    }

    internal fun registerBatteryReceiver(context: Context) {
        context.registerReceiver(currentBatteryReceiver, IntentFilter(Intent
                .ACTION_BATTERY_CHANGED))
    }

    internal fun getLocationDistance(origin: String, destination: String) =
            wayRepository.getLocationDistance(origin, destination).map { it.rows }

    internal fun getListLocationWhenReOpen(url: String) =
            wayRepository.getListLocation(url).map { it.locationRoads }

    internal fun getTrackingURL(): Single<String> = wayRepository.getTrackingURL()

    internal fun getLocationName(context: Context, latLng: LatLng): Single<String> =
            wayRepository.getLocationName(context, latLng)

    internal fun getCurrentLocationHyperTrack() = wayRepository.getCurrentLocationHyperTrack()

    internal fun getCurrentLocation(context: Context) = wayRepository.getCurrentLocation(context)

    internal fun getCalendarDateTime(): Single<String> {
        val result = SingleSubject.create<String>()
        val calendar = Calendar.getInstance().time
        val simple = SimpleDateFormat("KK:mm a', Th'MM dd", Locale.ENGLISH)
        result.onSuccess(simple.format(calendar))
        return result
    }

    internal fun compareLocation(currentLatLng: LatLng, destinationLatLng: LatLng): Single<Boolean> {
        val currentLat = String.format("%.3f", currentLatLng.latitude)
        val currentLng = String.format("%.3f", currentLatLng.longitude)
        val destinationLat = String.format("%.3f", destinationLatLng.latitude)
        val destinationLng = String.format("%.3f", destinationLatLng.longitude)
        val result = SingleSubject.create<Boolean>()
        result.onSuccess(currentLat == destinationLat && currentLng == destinationLng)
        return result
    }

    /**
     * This method return distance moving per second
     *
     * @param source start LatLng
     * @param destination destination LatLng
     *
     * @return list contain speed and distance value
     */
    internal fun getDistancePerSecond(source: LatLng, destination: LatLng): Single<List<Float>> {
        val des = Location("Point")
        des.latitude = source.latitude
        des.longitude = source.longitude
        val src = Location("Point")
        src.latitude = destination.latitude
        src.longitude = destination.longitude
        val list = mutableListOf<Float>()
        list.add((src.distanceTo(des) * AppConstants.TIME_CONVERT).toFloat())
        list.add(src.distanceTo(des) / AppConstants.SECOND_VALUE)
        resultDistancePerSecond.onSuccess(list)
        return resultDistancePerSecond
    }

    internal fun getTimeDuringStatus(time: Long): Single<String> {
        val stringTime: String = when {
            time in ShareViewModel.ONE_MINUTE_VALUE..ShareViewModel.HOUR_VALUE -> (time / ShareViewModel
                    .ONE_MINUTE_VALUE).toString().plus("min")
            time < ShareViewModel.ONE_MINUTE_VALUE -> time.toString().plus("second")
            else -> (time / ShareViewModel.ONE_HOUR_VALUE).toString().plus("hour")
        }
        resultTimeDuring.onSuccess(stringTime)
        return resultTimeDuring
    }

    internal fun getAngleMarker(startLatLong: LatLng, endLatLong: LatLng): Single<Float> {
        val startLat = radians(startLatLong.latitude)
        val startLong = radians(startLatLong.longitude)
        val endLat = radians(endLatLong.latitude)
        val endLong = radians(endLatLong.longitude)
        var deltaLong = endLong - startLong
        val deltaPi = Math.log(Math.tan(endLat / 2.0 + Math.PI / ShareViewModel.NUMBER_FOUR) / Math.tan
        (startLat / 2.0 + Math.PI / ShareViewModel.NUMBER_FOUR))
        if (Math.abs(deltaLong) > Math.PI) {
            deltaLong = if (deltaLong > 0.0) {
                -(2.0 * Math.PI - deltaLong)
            } else {
                (2.0 * Math.PI + deltaLong)
            }
        }
        val result = SingleSubject.create<Float>()
        result.onSuccess(((degrees(Math.atan2(deltaLong, deltaPi)) + AppConstants.RADIUS) %
                AppConstants.RADIUS).toFloat())
        return result
    }

    private fun radians(n: Double) = n * (Math.PI / (AppConstants.RADIUS / 2))

    private fun degrees(n: Double) = n * ((AppConstants.RADIUS / 2) / Math.PI)
}

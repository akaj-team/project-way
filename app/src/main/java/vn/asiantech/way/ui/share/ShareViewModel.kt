package vn.asiantech.way.ui.share

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import vn.asiantech.way.data.model.LocationRoad
import vn.asiantech.way.data.model.Rows
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.extension.observeOnUiThread

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 04/12/2017.
 */
class ShareViewModel(val context: Context) {
    var bbb = BehaviorSubject.create<Int>()
    private val wayRepository = WayRepository()
    private val mCurrentBatteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val level = p1?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            level?.let { bbb.onNext(it) }
        }
    }

    init {
        context.registerReceiver(mCurrentBatteryReceiver, IntentFilter(Intent
                .ACTION_BATTERY_CHANGED))
    }

    internal fun getLocationDistance(units: String, origins: String, destinations: String)
            : Observable<List<Rows>> {
        return wayRepository.getLocationDistance(units, origins, destinations)
                .observeOnUiThread()
                .map { it.rows }
    }

    internal fun getListLocation(url: String): Observable<List<LocationRoad>> {
        return wayRepository.getListLocation(url)
                .observeOnUiThread()
                .map { it.snappedPoints }
    }
}

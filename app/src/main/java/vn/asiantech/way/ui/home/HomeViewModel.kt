package vn.asiantech.way.ui.home

import android.content.Context
import android.os.SystemClock
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.SingleSubject
import vn.asiantech.way.data.model.TrackingInformation
import vn.asiantech.way.data.source.LocalRepository
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.utils.AppConstants

/**
 * HomeViewModel.
 *
 * @author at-ToanNguyen
 */

class HomeViewModel(val context: Context) {
    private val localRepository = LocalRepository(context)
    private var lastClickTime = 0L
    private val backStatus: PublishSubject<Boolean> = PublishSubject.create()

    internal fun getTrackingHistory(): Single<MutableList<TrackingInformation>> {
        val subject = SingleSubject.create<MutableList<TrackingInformation>>()
        localRepository.getTrackingHistory()?.let { subject.onSuccess(it) }
        return subject
    }

    internal fun exitApp(): Observable<Boolean> {
        backStatus.onNext(getDelayTime())
        return backStatus.observeOnUiThread()
    }

    private fun getDelayTime(): Boolean {
        if (SystemClock.elapsedRealtime() - lastClickTime < AppConstants.TIME_EXIT_APP) {
            return true
        }
        lastClickTime = SystemClock.elapsedRealtime()
        return false
    }

    internal fun resume() {
        lastClickTime = 0L
    }
}

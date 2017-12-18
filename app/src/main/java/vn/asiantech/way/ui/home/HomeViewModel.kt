package vn.asiantech.way.ui.home

import android.content.Context
import android.os.SystemClock
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.SingleSubject
import vn.asiantech.way.data.model.TrackingInformation
import vn.asiantech.way.data.source.LocalRepository
import vn.asiantech.way.utils.AppConstants

/**
 * HomeViewModel.
 *
 * @author at-ToanNguyen
 */
class HomeViewModel(private val assetDataRepository: LocalRepository) {
    internal val backStatus: PublishSubject<Boolean> = PublishSubject.create()
    private var lastClickTime = 0L

    constructor(context: Context) : this(LocalRepository(context))

    internal fun getTrackingHistory(): Single<MutableList<TrackingInformation>> {
        val subject = SingleSubject.create<MutableList<TrackingInformation>>()
        assetDataRepository.getTrackingHistory()?.let { subject.onSuccess(it) }
        return subject
    }

    internal fun eventBackPressed() {
        backStatus.onNext(handleBackKeyEvent())
    }

    private fun handleBackKeyEvent(): Boolean {
        if (SystemClock.elapsedRealtime() - lastClickTime < AppConstants.TIME_EXIT_APP) {
            return true
        }
        lastClickTime = SystemClock.elapsedRealtime()
        return false
    }
}

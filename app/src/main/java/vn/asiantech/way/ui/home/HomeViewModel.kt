package vn.asiantech.way.ui.home

import android.content.Context
import android.os.SystemClock
import android.support.v7.util.DiffUtil
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import vn.asiantech.way.data.model.TrackingInformation
import vn.asiantech.way.data.source.LocalRepository
import vn.asiantech.way.ui.base.Diff
import vn.asiantech.way.utils.AppConstants

/**
 * HomeViewModel.
 *
 * @author at-ToanNguyen
 */
class HomeViewModel(private val assetDataRepository: LocalRepository) {
    internal val backStatus: PublishSubject<Boolean> = PublishSubject.create()
    internal var updateHistoryTrackingList = PublishSubject.create<DiffUtil.DiffResult>()
    internal var historyTrackingList = mutableListOf<TrackingInformation>()
    private var trackingHistorySubject = PublishSubject.create<MutableList<TrackingInformation>>()
    private var lastClickTime = 0L

    init {
        initTrackingHistory()
    }

    constructor(context: Context) : this(LocalRepository(context))

    private fun initTrackingHistory() {
        trackingHistorySubject.observeOn(Schedulers.computation()).doOnNext {
            val diff = Diff(historyTrackingList, it)
                    .areItemsTheSame { oldItem, newItem ->
                        oldItem.point == newItem.point
                    }
                    .areContentsTheSame { oldItem, newItem ->
                        oldItem.status == newItem.status
                    }
                    .calculateDiff()

            historyTrackingList.clear()
            historyTrackingList.addAll(it)
            updateHistoryTrackingList.onNext(diff)
        }.subscribe()
    }

    internal fun getTrackingHistory() {
        assetDataRepository.getTrackingHistory()?.let { trackingHistorySubject.onNext(it) }

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

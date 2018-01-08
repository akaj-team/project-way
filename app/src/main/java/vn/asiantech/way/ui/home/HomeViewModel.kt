package vn.asiantech.way.ui.home

import android.content.Context
import android.os.SystemClock
import android.support.v7.util.DiffUtil
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.SingleSubject
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
    internal val oldHistoryTracking = mutableListOf<TrackingInformation>()
    private var trackingHistorySubject = PublishSubject.create<Boolean>()
    private val positions: MutableList<Int> = mutableListOf()
    private var lastClickTime = 0L
    private var position = -1

    init {
        initTrackingHistory()
    }

    constructor(context: Context) : this(LocalRepository(context))

    internal fun trackingHistory(isGetHistory: Boolean = false) {
        trackingHistorySubject.onNext(isGetHistory)
    }

    internal fun eventBackPressed() {
        backStatus.onNext(handleBackKeyEvent())
    }

    internal fun getItemPosition(pos: Int): Single<Int> {
        val trackingPositionSubject = SingleSubject.create<Int>()
        positions.add(pos)
        if (positions.size > 1) {
            if (pos > positions[positions.size - 2]) {
                trackingPositionSubject.onSuccess(pos + 1)
            } else {
                trackingPositionSubject.onSuccess(pos)
            }
        }
        return trackingPositionSubject
    }

    internal fun changeBackgroundItem(pos: Int) {
        historyTrackingList = oldHistoryTracking.map {
            with(it) {
                TrackingInformation(time, status, description, point)
            }
        }.toMutableList()
        if (position >= 0) {
            historyTrackingList[position].isChoose = false
        }
        historyTrackingList[pos].isChoose = true
        trackingHistory(true)
        position = pos
    }

    private fun initTrackingHistory() {
        trackingHistorySubject
                .observeOn(Schedulers.computation())
                .flatMap {
                    if (!it) {
                        getListTrackingHistory().toObservable()
                    } else {
                        getListTrackingChanged().toObservable()
                    }
                }
                .doOnNext {
                    notifyItemChange(oldHistoryTracking, it)
                }.subscribe()
    }

    private fun notifyItemChange(oldList: MutableList<TrackingInformation>, newList: MutableList<TrackingInformation>) {
        val diff = Diff(oldList, newList)
                .areContentsTheSame { oldItem, newItem ->
                    oldItem.isChoose == newItem.isChoose
                }
                .areItemsTheSame { oldItem, newItem ->
                    oldItem.time == newItem.time
                }
                .calculateDiff()
        oldList.clear()
        oldList.addAll(newList)
        updateHistoryTrackingList.onNext(diff)
    }

    private fun getListTrackingHistory(): Single<MutableList<TrackingInformation>> {
        val subject = SingleSubject.create<MutableList<TrackingInformation>>()
        assetDataRepository.getTrackingHistory()?.let { subject.onSuccess(it) }
        return subject
    }

    private fun getListTrackingChanged(): Single<MutableList<TrackingInformation>> {
        val subject = SingleSubject.create<MutableList<TrackingInformation>>()
        subject.onSuccess(historyTrackingList)
        return subject
    }

    private fun handleBackKeyEvent(): Boolean {
        if (SystemClock.elapsedRealtime() - lastClickTime < AppConstants.TIME_EXIT_APP) {
            return true
        }
        lastClickTime = SystemClock.elapsedRealtime()
        return false
    }
}

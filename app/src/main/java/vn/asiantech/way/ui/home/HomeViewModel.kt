package vn.asiantech.way.ui.home

import android.content.Context
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import vn.asiantech.way.data.model.TrackingInformation
import vn.asiantech.way.data.source.LocalRepository

/**
 * HomeViewModel.
 *
 * @author at-ToanNguyen
 */

class HomeViewModel(val context: Context) {
    private val localRepository = LocalRepository(context)

    internal fun getTrackingHistory(): Single<MutableList<TrackingInformation>> {
        val subject = SingleSubject.create<MutableList<TrackingInformation>>()
        subject.onSuccess(localRepository.getTrackingHistory())
        return subject
    }
}

package vn.asiantech.way.ui.search

import android.content.Context
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.SingleSubject
import vn.asiantech.way.data.model.AutoCompleteLocation
import vn.asiantech.way.data.model.WayLocation
import vn.asiantech.way.data.source.LocalRepository
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.utils.AppConstants

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 01/12/2017
 */
class SearchViewModel(val context: Context) {
    internal var progressBarStatus: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val wayRepository = WayRepository()
    private val localRepository = LocalRepository(context)

    internal fun searchLocation(query: String, language: String = "vi",
                                sensor: Boolean = false): Observable<List<AutoCompleteLocation>> {
        progressBarStatus.onNext(true)
        return wayRepository
                .searchLocations(query, AppConstants.GOOGLE_MAP_API_KEY, language, sensor)
                .observeOnUiThread()
                .doOnNext { progressBarStatus.onNext(false) }
                .map { it.predictions }
    }

    internal fun getLocationDetail(placeId: String): Observable<WayLocation> {
        return wayRepository.getLocationDetail(placeId, AppConstants.GOOGLE_MAP_API_KEY)
                .observeOnUiThread()
                .map { it.result }
    }

    internal fun loadSearchHistories(): Single<List<WayLocation>> {
        val subject = SingleSubject.create<List<WayLocation>>()
        subject.onSuccess(localRepository.getSearchHistory()!!)
        return subject

    }

    internal fun saveSearchHistories(location: WayLocation) {
        localRepository.saveSearchHistory(location)
    }
}

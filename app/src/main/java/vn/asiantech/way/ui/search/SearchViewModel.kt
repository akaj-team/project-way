package vn.asiantech.way.ui.search

import android.content.Context
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.SingleSubject
import vn.asiantech.way.data.model.WayLocation
import vn.asiantech.way.data.source.LocalRepository
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.utils.AppConstants
import java.util.concurrent.TimeUnit

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 01/12/2017
 */
class SearchViewModel(val context: Context) {
    internal var progressBarStatus: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val wayRepository = WayRepository()
    private val localRepository = LocalRepository(context)
    private val searchObservable = PublishSubject.create<String>()

    internal fun searchLocations(query: String = "") {
        searchObservable.onNext(query)
    }

    internal fun triggerSearchLocationResult(language: String = "vi", sensor: Boolean = false):
            Observable<List<WayLocation>> {
        return searchObservable
                .debounce(AppConstants.WAITING_TIME_FOR_SEARCH_FUNCTION, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .flatMap {
                    if (it.isBlank()) {
                        loadSearchHistories().toObservable()
                    } else {
                        searchLocationsApi(it, language, sensor)
                    }

                }
    }

    internal fun getLocationDetail(placeId: String): Observable<WayLocation> {
        return wayRepository.getLocationDetail(placeId)
                .map { it.result }
    }

    internal fun saveSearchHistories(location: WayLocation) {
        localRepository.saveSearchHistory(location)
    }

    private fun loadSearchHistories(): Single<List<WayLocation>> {
        val subject = SingleSubject.create<List<WayLocation>>()
        subject.onSuccess(localRepository.getSearchHistory()!!)
        return subject

    }

    private fun searchLocationsApi(input: String, language: String, sensor: Boolean): Observable<List<WayLocation>> {
        return wayRepository
                .searchLocations(input, language, sensor)
                .doOnSubscribe { progressBarStatus.onNext(true) }
                .doOnNext { progressBarStatus.onNext(false) }
                .map { it.predictions }
                .flatMapIterable { list -> list }
                .map {
                    with(it) {
                        WayLocation(id, placeId, description, structuredFormatting.mainText)
                    }
                }
                .toList()
                .toObservable()
    }
}

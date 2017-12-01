package vn.asiantech.way.ui.search

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import vn.asiantech.way.data.model.AutoCompleteLocation
import vn.asiantech.way.data.model.WayLocation
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.utils.AppConstants

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 01/12/2017
 */
class SearchViewModel {
    internal var progressBarStatus: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val wayRepository = WayRepository()

    internal fun searchLocation(query: String, language: String = "vi",
                                sensor: Boolean = false): Observable<List<AutoCompleteLocation>> {
        progressBarStatus.onNext(true)
        return wayRepository
                .searchLocations(query, AppConstants.GOOGLE_MAP_API_KEY, language, sensor)
                .doOnNext { progressBarStatus.onNext(false) }
                .map { it.predictions }
    }

    internal fun getLocationDetail(placeId: String): Observable<WayLocation> {
        return wayRepository.getLocationDetail(placeId, AppConstants.GOOGLE_MAP_API_KEY)
                .map { it.result }
    }
}

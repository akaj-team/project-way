package vn.asiantech.way.ui.search

import android.content.Context
import android.support.v7.util.DiffUtil
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.SingleSubject
import vn.asiantech.way.data.model.WayLocation
import vn.asiantech.way.data.source.LocalRepository
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.ui.base.Diff
import vn.asiantech.way.utils.AppConstants
import java.util.concurrent.TimeUnit

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 01/12/2017
 */
class SearchViewModel(private val wayRepository: WayRepository, private val localRepository: LocalRepository) {
    internal var progressBarStatus: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val searchObservable = PublishSubject.create<String>()
    internal val updateAutocompleteList = PublishSubject.create<DiffUtil.DiffResult>()
    internal var locations = mutableListOf<WayLocation>()

    init {
        initSearchLocations()
    }

    constructor(context: Context) : this(WayRepository(), LocalRepository(context))

    internal fun searchLocations(query: String = "") {
        searchObservable.onNext(query)
    }

    private fun initSearchLocations(language: String = "vi", sensor: Boolean = false) {
        searchObservable
                .observeOn(Schedulers.computation())
                .debounce(AppConstants.WAITING_TIME_FOR_SEARCH_FUNCTION, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .flatMap {
                    if (it.isBlank()) {
                        loadSearchHistories().toObservable()
                    } else {
                        searchLocationsApi(it, language, sensor)
                    }
                }.doOnNext {
            val diff = Diff(locations, it)
                    .areItemsTheSame { oldItem, newItem ->
                        oldItem.id == newItem.id
                    }
                    .areContentsTheSame { oldItem, newItem ->
                        oldItem.name == newItem.name
                    }
                    .calculateDiff()

            locations.clear()
            locations.addAll(it)
            updateAutocompleteList.onNext(diff)
        }.subscribe()
    }

    internal fun getLocationDetail(placeId: String): Observable<WayLocation>
            = wayRepository.getLocationDetail(placeId).map { it.result }

    internal fun saveSearchHistories(location: WayLocation) {
        localRepository.saveSearchHistory(location)
    }

    private fun loadSearchHistories(): Single<List<WayLocation>> {
        val subject = SingleSubject.create<List<WayLocation>>()
        subject.onSuccess(localRepository.getSearchHistory()!!)
        return subject

    }

    private fun searchLocationsApi(input: String, language: String, sensor: Boolean):
            Observable<List<WayLocation>> = wayRepository
            .searchLocations(input, language, sensor)
            .doOnSubscribe { progressBarStatus.onNext(true) }
            .doFinally { progressBarStatus.onNext(false) }
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

package vn.asiantech.way.ui.group.search

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.utils.AppConstants
import java.util.concurrent.TimeUnit

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 04/12/2017.
 */
class SearchGroupViewModel {
    private val searchGroupObservable = PublishSubject.create<String>()
    private val groupRepository = GroupRepository()
    internal val progressDialogObservable = BehaviorSubject.create<Boolean>()

    private fun searchGroup(query: String): Observable<List<Group>> {
        return groupRepository
                .searchGroup(query)
                .observeOnUiThread()
    }

    internal fun eventAfterTextChanged(query: String) {
        searchGroupObservable.onNext(query)
    }

    internal fun postRequestToGroup(groupId: String, request: Invite): Single<Boolean> {
        progressDialogObservable.onNext(false)
        return groupRepository
                .postRequestToGroup(groupId, request)
                .observeOnUiThread()
                .doOnSuccess { progressDialogObservable.onNext(true) }
    }

    internal fun getCurrentRequest(userId: String): Observable<Invite> {
        return groupRepository
                .getCurrentRequestOfUser(userId)
                .observeOnUiThread()
    }

    internal fun searchGroupQuery(): Observable<List<Group>> {
        return searchGroupObservable
                .observeOnUiThread()
                .debounce(AppConstants.WAITING_TIME_FOR_SEARCH_FUNCTION, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .flatMap { searchGroup(it) }
    }
}

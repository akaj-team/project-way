package vn.asiantech.way.ui.group.search

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.utils.AppConstants
import java.util.concurrent.TimeUnit

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 04/12/2017.
 */
class SearchGroupViewModel(private val userId: String) {
    private val searchGroupObservable = PublishSubject.create<String>()
    private val groupRepository = GroupRepository()
    internal var currentRequest: Invite = Invite("", "", "", false)

    internal val progressDialogObservable = BehaviorSubject.create<Boolean>()

    private fun searchGroup(query: String): Observable<List<Group>> {
        return groupRepository
                .searchGroup(query)
                .subscribeOn(Schedulers.io())
    }

    internal fun eventAfterTextChanged(query: String) {
        searchGroupObservable.onNext(query)
    }

    internal fun postRequestToGroup(group: Group): Single<Boolean> {
        val invite = Invite(userId, group.id, group.name, true)
        return groupRepository
                .postRequestToGroup(group.id, invite)
                .doOnSubscribe {
                    progressDialogObservable.onNext(false)
                }
                .doOnSuccess {
                    currentRequest = invite
                    progressDialogObservable.onNext(true)
                }
    }

    internal fun triggerSearchGroup(): Observable<List<Group>> {
        return groupRepository
                .getCurrentRequestOfUser(userId)
                .doOnNext { currentRequest = it }
                .flatMap {
                    searchGroupQuery()
                }
    }

    private fun searchGroupQuery(): Observable<List<Group>> {
        return searchGroupObservable
                .debounce(AppConstants.WAITING_TIME_FOR_SEARCH_FUNCTION, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .flatMap { searchGroup(it) }
    }
}

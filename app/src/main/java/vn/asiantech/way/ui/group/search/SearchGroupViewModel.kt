package vn.asiantech.way.ui.group.search

import android.support.v7.util.DiffUtil
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.ui.base.Diff
import vn.asiantech.way.utils.AppConstants
import java.util.concurrent.TimeUnit

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 04/12/2017.
 */
class SearchGroupViewModel(private val groupRepository: GroupRepository, private var userId: String = "") {
    private val searchGroupObservable = PublishSubject.create<String>()
    internal val progressDialogObservable = BehaviorSubject.create<Boolean>()
    internal val updateAutocompleteList = PublishSubject.create<DiffUtil.DiffResult>()
    internal val updateCurrentRequest = PublishSubject.create<Invite>()

    internal var groups = mutableListOf<Group>()

    constructor(userId: String) : this(GroupRepository(), userId) {
        triggerSearchGroup()
    }

    init {
        searchGroupQuery()
    }

    private fun searchGroup(query: String): Observable<List<Group>> =
            groupRepository
                    .searchGroup(query)
                    .subscribeOn(Schedulers.io())

    internal fun eventAfterTextChanged(query: String) {
        searchGroupObservable.onNext(query)
    }

    internal fun postRequestToGroup(group: Group): Single<Boolean> {
        val invite = Invite(userId, group.id, group.name, true)
        return groupRepository
                .postRequestToGroup(group.id, invite)
                .doOnSubscribe {
                    progressDialogObservable.onNext(true)
                }
                .doOnSuccess {
                    updateCurrentRequest.onNext(invite)
                    progressDialogObservable.onNext(false)
                }
    }

    private fun triggerSearchGroup() {
        groupRepository
                .getCurrentRequestOfUser(userId)
                .doOnNext {
                    updateCurrentRequest.onNext(it)
                }.subscribe()
    }

    private fun searchGroupQuery() {
        searchGroupObservable
                .debounce(AppConstants.WAITING_TIME_FOR_SEARCH_FUNCTION, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .flatMap { searchGroup(it) }
                .doOnNext {
                    val diff = Diff(groups, it)
                            .areItemsTheSame { oldItem, newItem ->
                                oldItem.id == newItem.id
                            }
                            .areContentsTheSame { oldItem, newItem ->
                                oldItem.name == newItem.name
                            }
                            .calculateDiff()

                    groups.clear()
                    groups.addAll(it)
                    updateAutocompleteList.onNext(diff)
                }
                .subscribe()
    }
}

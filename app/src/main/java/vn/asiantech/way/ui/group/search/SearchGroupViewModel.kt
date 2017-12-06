package vn.asiantech.way.ui.group.search

import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.extension.observeOnUiThread

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 04/12/2017.
 */
class SearchGroupViewModel {
    internal var progressBarStatus: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val groupRepository = GroupRepository()

    internal fun searchGroup(query: String): Observable<List<Group>> {
        progressBarStatus.onNext(true)
        return groupRepository
                .searchGroup(query)
                .observeOnUiThread()
                .doOnNext {
                    progressBarStatus.onNext(false)
                }
    }

    internal fun postRequestToGroup(groupId: String, request: Invite):Single<Boolean>{
        return groupRepository
                .postRequestToGroup(groupId,request)
    }
}

package vn.asiantech.way.ui.group.search

import io.reactivex.Observable
import io.reactivex.Single
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.extension.observeOnUiThread

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 04/12/2017.
 */
class SearchGroupViewModel {
    private val groupRepository = GroupRepository()

    internal fun searchGroup(query: String): Observable<List<Group>> {
        return groupRepository
                .searchGroup(query)
                .observeOnUiThread()
    }

    internal fun postRequestToGroup(groupId: String, request: Invite): Single<Boolean> {
        return groupRepository
                .postRequestToGroup(groupId, request)
    }

    internal fun getCurrentRequest(userId: String): Observable<Invite> {
        return groupRepository
                .getCurrentRequestOfUser(userId)
    }
}

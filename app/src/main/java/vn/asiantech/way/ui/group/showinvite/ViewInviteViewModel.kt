package vn.asiantech.way.ui.group.showinvite

import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.Single
import vn.asiantech.way.data.model.BodyAddUserToGroup
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.extension.observeOnUiThread

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 06/12/2017.
 */
class ViewInviteViewModel {
    private val groupRepository = GroupRepository()
    private val wayRepository = WayRepository()

    internal fun getCurrentRequest(userId: String): Observable<Invite> {
        return groupRepository
                .getCurrentRequestOfUser(userId)
                .observeOnUiThread()
    }

    internal fun getInvitesOfUser(userId: String): Observable<Invite> {
        return groupRepository
                .getInvite(userId)
                .observeOnUiThread()
    }

    internal fun addUserToGroup(userId: String, groupId: String): Observable<User> {
        return wayRepository
                .addUserToGroup(userId, BodyAddUserToGroup(groupId))
                .observeOnUiThread()
    }

    internal fun removeInviteOfUserToAnotherGroup(userId: String, invite: Invite): Single<Boolean> {
        return groupRepository
                .deleteUserInvite(userId, invite)
                .observeOnUiThread()
    }

}
package vn.asiantech.way.ui.group.showinvite

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.SingleSubject
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.extension.observeOnUiThread

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 06/12/2017.
 */
class ViewInviteViewModel {
    internal val isOwnerInviteObservable = SingleSubject.create<Boolean>()
    internal val progressDialogObservable = BehaviorSubject.create<Boolean>()
    private val groupRepository = GroupRepository()

    internal fun getInvitesOfUser(userId: String): Observable<Invite> {
        return groupRepository
                .getInvite(userId)
                .observeOnUiThread()
    }

    internal fun removeInviteUserFromGroup(userId: String, invite: Invite): Single<Boolean> {
        return groupRepository
                .deleteUserInvite(userId, invite)
                .observeOnUiThread()
                .doOnSuccess {
                    progressDialogObservable.onNext(true)
                }
    }

    internal fun acceptInvite(userId: String, invite: Invite): Single<Boolean> {
        return groupRepository
                .acceptInvite(userId, invite)
                .observeOnUiThread()
                .doOnSubscribe { isOwnerInviteObservable.onSuccess(invite.request) }
                .flatMap { isOwnerInviteObservable }
                .doOnSuccess { progressDialogObservable.onNext(true) }
    }
}

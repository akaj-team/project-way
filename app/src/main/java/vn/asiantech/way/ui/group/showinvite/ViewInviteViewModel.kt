package vn.asiantech.way.ui.group.showinvite

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.data.source.GroupRepository

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 06/12/2017.
 */
class ViewInviteViewModel(private val userId: String) {
    internal val progressDialogObservable = BehaviorSubject.create<Boolean>()
    private val groupRepository = GroupRepository()

    internal fun getInvitesOfUser(): Observable<Invite> = groupRepository.getInvite(userId)

    internal fun removeInviteUserFromGroup(invite: Invite): Single<Boolean> = groupRepository
            .deleteUserInvite(userId, invite)
            .doOnSubscribe {
                progressDialogObservable.onNext(true)
            }
            .doFinally {
                progressDialogObservable.onNext(false)
            }

    internal fun acceptInvite(invite: Invite): Single<Boolean> = groupRepository
            .acceptInvite(userId, invite)
            .doOnSubscribe {
                progressDialogObservable.onNext(true)
            }
            .doFinally {
                progressDialogObservable.onNext(false)
            }
}

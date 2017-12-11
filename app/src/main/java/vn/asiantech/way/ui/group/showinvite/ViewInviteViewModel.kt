package vn.asiantech.way.ui.group.showinvite

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.extension.observeOnUiThread

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 06/12/2017.
 */
class ViewInviteViewModel(private val userId: String) {
    internal val progressDialogObservable = BehaviorSubject.create<Boolean>()
    private val groupRepository = GroupRepository()

    internal fun getInvitesOfUser(): Observable<Invite> {
        return groupRepository
                .getInvite(userId)
                .observeOnUiThread()
    }

    internal fun removeInviteUserFromGroup(invite: Invite): Single<Boolean> {
        return groupRepository
                .deleteUserInvite(userId, invite)
                .observeOnUiThread()
                .doOnSubscribe {
                    progressDialogObservable.onNext(false)
                }
                .doAfterTerminate {
                    progressDialogObservable.onNext(true)
                }
    }

    internal fun acceptInvite(invite: Invite): Single<Boolean> {
        return groupRepository
                .acceptInvite(userId, invite)
                .observeOnUiThread()
                .doOnSubscribe {
                    progressDialogObservable.onNext(false)
                }
                .doAfterTerminate {
                    progressDialogObservable.onNext(true)
                }
    }
}

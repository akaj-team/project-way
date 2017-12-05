package vn.asiantech.way.ui.group

import com.hypertrack.lib.models.User
import io.reactivex.Observable
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.extension.observeOnUiThread

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 04/12/2017
 */
class GroupActivityViewModel {

    private val groupRepository = GroupRepository()
    private val wayRepository = WayRepository()

    internal fun getUser(): Observable<User> {
        return wayRepository.getUser()
                .observeOnUiThread()
    }

    internal fun getGroupInfo(groupId: String): Observable<Group> {
        return groupRepository.getGroupInfo(groupId)
                .observeOnUiThread()
    }

    internal fun getGroupId(userId: String): Observable<String> {
        return groupRepository.getGroupId(userId)
                .observeOnUiThread()
    }

    internal fun getInviteList(userId: String): Observable<Invite> {
        return groupRepository.getInvite(userId)
                .observeOnUiThread()
    }
}

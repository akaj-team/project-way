package vn.asiantech.way.ui.group.info

import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.Single
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.extension.observeOnUiThread

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 05/12/2017
 */
class GroupInfoViewModel {
    private val groupRepository = GroupRepository()

    internal fun getGroupInfo(groupId: String): Observable<Group> {
        return groupRepository.getGroupInfo(groupId)
                .observeOnUiThread()
    }

    internal fun getMemberList(groupId: String): Observable<MutableList<User>> {
        return groupRepository.getMemberList(groupId)
                .toObservable()
                .observeOnUiThread()
    }

    internal fun leaveGroup(userId: String): Observable<User> {
        return groupRepository.removeUserFromGroup(userId).toObservable()
    }

    internal fun changeGroupOwner(groupId: String, newOwnerId: String): Single<Boolean> {
        return groupRepository.changeGroupOwner(groupId, newOwnerId)
    }
}

package vn.asiantech.way.ui.group.info

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 11/12/2017
 */
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
        return groupRepository.getGroupInfo(groupId).observeOnUiThread()
    }

    internal fun getMemberList(groupId: String): Single<MutableList<User>> {
        return groupRepository.getMemberList(groupId).observeOnUiThread()
    }

    internal fun leaveGroup(userId: String): Single<User> {
        return groupRepository.removeUserFromGroup(userId).observeOnUiThread()
    }

    internal fun changeGroupOwner(groupId: String, newOwnerId: String): Single<Boolean> {
        return groupRepository.changeGroupOwner(groupId, newOwnerId)
    }
}

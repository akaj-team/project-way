package vn.asiantech.way.ui.group.info

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 11/12/2017
 */
import android.support.v7.util.DiffUtil
import com.hypertrack.lib.models.User
import io.reactivex.subjects.PublishSubject
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.ui.base.Diff

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 05/12/2017
 */
class GroupInfoViewModel(private val groupRepository: GroupRepository) {
    internal val updateMemberList = PublishSubject.create<DiffUtil.DiffResult>()
    internal val users = mutableListOf<User>()

    constructor() : this(GroupRepository())

    internal fun getGroupInfo(groupId: String) = groupRepository.getGroupInfo(groupId)

    internal fun getMemberList(groupId: String) {
        groupRepository.getMemberList(groupId)
                .subscribe({
                    val diff = Diff(users, it)
                            .areItemsTheSame { oldItem, newItem ->
                                oldItem.id == newItem.id
                            }
                            .areContentsTheSame { oldItem, newItem ->
                                oldItem.name == newItem.name
                                        && oldItem.photo == newItem.photo
                            }
                            .calculateDiff()
                    users.clear()
                    users.addAll(it)
                    updateMemberList.onNext(diff)
                }, {
                    updateMemberList.onError(it)
                })
    }

    internal fun leaveGroup(userId: String) = groupRepository.removeUserFromGroup(userId)

    internal fun changeGroupOwner(groupId: String, newOwnerId: String) = groupRepository.changeGroupOwner(groupId, newOwnerId)
}

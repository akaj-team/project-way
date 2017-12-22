package vn.asiantech.way.ui.group.info
import vn.asiantech.way.data.source.GroupRepository

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 05/12/2017
 */
class GroupInfoViewModel(private val groupRepository: GroupRepository) {

    constructor() : this(GroupRepository())

    internal fun getGroupInfo(groupId: String) = groupRepository.getGroupInfo(groupId)

    internal fun getMemberList(groupId: String) = groupRepository.getMemberList(groupId)

    internal fun leaveGroup(userId: String) = groupRepository.removeUserFromGroup(userId)

    internal fun changeGroupOwner(groupId: String, newOwnerId: String) = groupRepository.changeGroupOwner(groupId, newOwnerId)
}

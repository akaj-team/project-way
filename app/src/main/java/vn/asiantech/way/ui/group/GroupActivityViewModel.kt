package vn.asiantech.way.ui.group

import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.data.source.WayRepository

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 04/12/2017
 */
class GroupActivityViewModel(private val groupRepository: GroupRepository, private val wayRepository: WayRepository) {

    internal fun getUser() = wayRepository.getUser()

    internal fun listenerForGroupChange(userId: String) = groupRepository.listenerForGroupChange(userId)
}

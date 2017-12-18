package vn.asiantech.way.ui.group.create

import io.reactivex.Single
import vn.asiantech.way.data.source.GroupRepository

/**
 * CreateGroupViewModel.
 *
 * @author at-ToanNguyen
 */
class CreateGroupViewModel(private val groupRepository: GroupRepository) {
    constructor() : this(GroupRepository())

    internal fun createGroup(name: String, userId: String): Single<Boolean>
            = groupRepository.createGroup(name, userId)
}

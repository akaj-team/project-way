package vn.asiantech.way.ui.group.info

import io.reactivex.Observable
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
}

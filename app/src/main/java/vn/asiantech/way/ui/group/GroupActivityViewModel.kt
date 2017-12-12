package vn.asiantech.way.ui.group

import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.Single
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

    internal fun getUser(): Single<User> {
        return wayRepository.getUser().observeOnUiThread()

    }

    internal fun listenerForGroupChange(userId: String): Observable<String> {
        return groupRepository.listenerForGroupChange(userId).observeOnUiThread()
    }
}

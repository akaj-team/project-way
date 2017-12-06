package vn.asiantech.way.ui.group

import android.content.Context
import com.hypertrack.lib.models.User
import io.reactivex.Observable
import vn.asiantech.way.data.model.BodyAddUserToGroup
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.data.source.WayRepository

/**
 * CreateGroupViewModel.
 *
 * @author at-ToanNguyen
 */
class CreateGroupViewModel(val context: Context) {
    private val wayRepository = WayRepository()
    private val groupRepository = GroupRepository()

    internal fun createGroup(name: String): Observable<Group> {
        return wayRepository.createGroup(name)
    }

    internal fun addUserToGroup(userId: String, body: BodyAddUserToGroup): Observable<User> {
        return wayRepository.addUserToGroup(userId, body)
    }

    internal fun upGroupInfo(group: Group): Observable<Boolean> {
        return groupRepository.upGroupInfo(group)
    }
}

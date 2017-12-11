package vn.asiantech.way.ui.group

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
class CreateGroupViewModel {
    private val wayRepository = WayRepository()
    private val groupRepository = GroupRepository()

    internal fun createGroup(name: String, userId: String): Observable<Boolean> {
        return wayRepository.createGroup(name).flatMap { group -> addUserToGroup(userId, group) }.flatMap { postGroupInfo(it) }
    }

    private fun addUserToGroup(userId: String, body: BodyAddUserToGroup): Observable<User> {
        return wayRepository.addUserToGroup(userId, body)
    }

    private fun addUserToGroup(userId: String, group: Group): Observable<Group> {
        group.ownerId = userId
        return addUserToGroup(userId, BodyAddUserToGroup(group.id)).map { group }
    }

    private fun postGroupInfo(group: Group): Observable<Boolean> {
        return groupRepository.postGroupInfo(group)
    }
}

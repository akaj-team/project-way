package vn.asiantech.way.ui.group

import android.content.Context
import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import vn.asiantech.way.data.model.BodyAddUserToGroup
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.extension.observeOnUiThread

/**
 * CreateGroupViewModel.
 *
 * @author at-ToanNguyen
 */
class CreateGroupViewModel(val context: Context) {
    private val wayRepository = WayRepository()
    private val groupRepository = GroupRepository()
    internal fun createGroup(name: String): Observable<Group> {
        val group = PublishSubject.create<Group>()
        wayRepository.createGroup(name).observeOnUiThread().subscribe({
            group.onNext(it)
        }, {
            group.onError(it)
        })
        return group
    }

    internal fun addUserToGroup(userId: String, body: BodyAddUserToGroup): Observable<User> {
        return wayRepository.addUserToGroup(userId, body)
    }

    internal fun upGroupInfo(group: Group): Observable<Boolean> {
        val result = PublishSubject.create<Boolean>()
        groupRepository.upGroupInfo(group)
                .observeOnUiThread()
                .subscribe({
                    result.onNext(it)
                }, {
                    result.onError(Throwable(it))
                })
        return result
    }
}

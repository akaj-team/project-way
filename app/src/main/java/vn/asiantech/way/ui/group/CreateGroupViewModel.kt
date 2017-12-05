package vn.asiantech.way.ui.group

import android.content.Context
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

    internal fun createGroup(userId: String?, name: String): Observable<Boolean> {
        val result = PublishSubject.create<Boolean>()
        var group: Group?
        wayRepository.createGroup(name).observeOnUiThread().subscribe {
            it.ownerId = userId!!
            group = it
            wayRepository.addUserToGroup(userId, BodyAddUserToGroup(it.id))
                    .observeOnUiThread()
                    .subscribe {
                        GroupRepository().upGroupInfo(group!!)
                                .observeOnUiThread()
                                .subscribe({
                                    result.onNext(it)
                                }, {
                                    result.onError(Throwable(it))
                                })
                    }
        }
        return result
    }
}

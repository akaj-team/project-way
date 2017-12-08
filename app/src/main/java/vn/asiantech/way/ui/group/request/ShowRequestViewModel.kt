package vn.asiantech.way.ui.group.request

import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import vn.asiantech.way.data.model.BodyAddUserToGroup
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.extension.observeOnUiThread

/**
 * Request View Model
 * @author NgocTTN
 */
class ShowRequestViewModel {
    internal var progressBarStatus: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val wayRepository = WayRepository()

    internal fun addUserToGroup(userId: String, groupId: String): Observable<User> {
        progressBarStatus.onNext(true)
        return wayRepository.addUserToGroup(userId, BodyAddUserToGroup(groupId))
                .doOnNext { progressBarStatus.onNext(false) }
                .observeOnUiThread()
    }

}

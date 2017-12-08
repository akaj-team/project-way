package vn.asiantech.way.ui.group.request

import io.reactivex.subjects.BehaviorSubject
import vn.asiantech.way.data.source.WayRepository

/**
 * Request View Model
 * @author NgocTTN
 */
class RequestViewModel {
    internal var progressBarStatus: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val wayRepository = WayRepository()

/*
    internal fun addUserToGroup(userId: String): Observable<User> {
        return wayRepository.addUserToGroup(userId, )
                .observeOnUiThread()
    }
*/

}

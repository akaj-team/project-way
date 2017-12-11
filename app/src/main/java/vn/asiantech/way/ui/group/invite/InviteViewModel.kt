package vn.asiantech.way.ui.group.invite

import android.content.Context
import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.utils.AppConstants
import java.util.concurrent.TimeUnit

/**
 * Invite View Model
 * @author NgocTTN
 */
class InviteViewModel(val context: Context) {
    internal var resetDataStatus: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val groupRepository = GroupRepository()
    private val searchInviteObservable = PublishSubject.create<String>()

    internal fun searchListUser(query: String = "") {
        searchInviteObservable.onNext(query)
    }

    internal fun triggerSearchListUser(): Observable<List<User>> {
        return searchInviteObservable
                .observeOnUiThread()
                .debounce(AppConstants.WAITING_TIME_FOR_SEARCH_FUNCTION, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter { it.isNotEmpty() }
                .flatMap {
                    getListUserInvite(it)
                }
    }

    private fun getListUserInvite(name: String): Observable<List<User>> {
        return groupRepository
                .searchUser(name)
                .doOnSubscribe { resetDataStatus.onNext(true) }
                .observeOnUiThread()
    }
}

package vn.asiantech.way.ui.group.invite

import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.utils.AppConstants
import java.util.concurrent.TimeUnit

/**
 * Invite View Model
 * @author NgocTTN
 */
class InviteViewModel(private val groupRepository: GroupRepository) {
    internal var resetDataStatus: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val searchInviteObservable = PublishSubject.create<String>()

    constructor() : this(GroupRepository())

    internal fun searchListUser(query: String = "") {
        searchInviteObservable.onNext(query)
    }

    internal fun triggerSearchListUser(): Observable<List<User>> {
        return searchInviteObservable
                .debounce(AppConstants.WAITING_TIME_FOR_SEARCH_FUNCTION, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter { it.isNotEmpty() }
                .flatMap {
                    getListUser(it)
                }
    }

    internal fun inviteUserJoinToGroup(userId: String, invite: Invite) {
        groupRepository.inviteUserJoinGroup(userId, invite)
    }

    private fun getListUser(name: String): Observable<List<User>> {
        return groupRepository
                .searchUser(name)
                .doOnSubscribe { resetDataStatus.onNext(true) }
                .subscribeOn(Schedulers.io())
    }
}

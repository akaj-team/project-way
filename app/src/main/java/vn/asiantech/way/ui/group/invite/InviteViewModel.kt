package vn.asiantech.way.ui.group.invite

import android.support.v7.util.DiffUtil
import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.ui.base.Diff
import vn.asiantech.way.utils.AppConstants
import java.util.concurrent.TimeUnit

/**
 * Invite View Model
 * @author NgocTTN
 */
class InviteViewModel(private val groupRepository: GroupRepository) {

    internal val updateAutocompleteList = PublishSubject.create<DiffUtil.DiffResult>()
    internal var users = mutableListOf<User>()
    private val searchInviteObservable = PublishSubject.create<String>()

    init {
        initSearchListUser()
    }

    constructor() : this(GroupRepository())

    internal fun searchListUser(query: String = "") {
        searchInviteObservable.onNext(query)
    }

    private fun initSearchListUser() {
        searchInviteObservable
                .debounce(AppConstants.WAITING_TIME_FOR_SEARCH_FUNCTION, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter { it.isNotEmpty() }
                .flatMap {
                    getListUser(it)
                }.doOnNext {
            val diff = Diff(users, it)
                    .areItemsTheSame { oldItem, newItem ->
                        oldItem.id == newItem.id
                    }
                    .areContentsTheSame { oldItem, newItem ->
                        oldItem.name == newItem.name
                    }
                    .calculateDiff()

            users.clear()
            users.addAll(it)
            updateAutocompleteList.onNext(diff)
        }.subscribe()
    }

    internal fun inviteUserJoinToGroup(userId: String, invite: Invite) {
        groupRepository.inviteUserJoinGroup(userId, invite)
    }

    private fun getListUser(name: String): Observable<List<User>> = groupRepository
            .searchUser(name)
            .subscribeOn(Schedulers.io())
}

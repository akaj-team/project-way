package vn.asiantech.way.ui.group.request

import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import vn.asiantech.way.data.model.BodyAddUserToGroup
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.data.source.WayRepository

/**
 * Request View Model
 * @author NgocTTN
 */
class ShowRequestViewModel(private val wayRepository: WayRepository, private val groupRepository: GroupRepository) {
    internal var progressDialogObservable: BehaviorSubject<Boolean> = BehaviorSubject.create()

    constructor() : this(WayRepository(), GroupRepository())

    internal fun getRequestsOfUser(groupId: String): Observable<User> {
        return groupRepository
                .getUserInfo(groupId)
    }

    internal fun addUserToGroup(groupId: String, userId: String): Observable<User> {
        return wayRepository.addUserToGroup(userId, BodyAddUserToGroup(groupId))
                .doOnSubscribe { progressDialogObservable.onNext(true) }
                .doFinally { progressDialogObservable.onNext(false) }
    }

    internal fun removeRequestInGroup(groupId: String, userId: String): Single<Boolean> {
        return groupRepository.deleteGroupRequest(groupId, userId)
    }
}

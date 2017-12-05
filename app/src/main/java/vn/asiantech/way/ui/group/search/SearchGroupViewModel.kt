package vn.asiantech.way.ui.group.search

import io.reactivex.subjects.BehaviorSubject
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.source.GroupRepository
import java.util.*

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 04/12/2017.
 */
class SearchGroupViewModel {
    internal var progressBarStatus: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val groupRepository = GroupRepository()

    internal fun searchGroup(query: String): Observable<Group> {
        progressBarStatus.onNext(true)
        return groupRepository
                .searchGroup(query)
                .observeOnUiThread()
                .doOnNext { progressBarStatus.onNext(false) }
    }
}

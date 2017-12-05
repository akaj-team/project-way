package vn.asiantech.way.ui.group

import android.content.Context
import io.reactivex.Observable
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.source.WayRepository

/**
 * CreateGroupViewModel.
 *
 * @author at-ToanNguyen
 */
class CreateGroupViewModel(val context: Context) {
    private val wayRepository = WayRepository()
    internal fun createGroup(name: String): Observable<Group> {
        return wayRepository.createGroup(name)
    }
}
package vn.asiantech.way.ui.group.invite

import android.content.Context
import com.hypertrack.lib.models.User
import io.reactivex.Observable
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.extension.observeOnUiThread

/**
 * Invite View Model
 * @author NgocTTN
 */
class InviteViewModel(val context: Context) {
    private val groupRepository = GroupRepository()

    internal fun searchListUser(name : String) : Observable<List<User>> {
        return groupRepository
                .searchUser(name)
                .observeOnUiThread()
                .map { it }
    }
}

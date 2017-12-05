package vn.asiantech.way.ui.group.invite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hypertrack.lib.models.User
import org.jetbrains.anko.AnkoContext
import vn.asiantech.way.ui.base.BaseFragment

/**
 * Invite Activity
 */
class InviteFragment : BaseFragment() {

    companion object {
        private const val KEY_USER = "key_user_id"
        private const val KEY_GROUP = "key_group_id"

        /**
         * Get instance of InviteFragment
         */
        fun getInstance(userId: String, groupId: String, groupName: String, ownerId: String)
                : InviteFragment {
            val instance = InviteFragment()
            val bundle = Bundle()
            bundle.putString(KEY_USER, userId)
            bundle.putString(KEY_GROUP, groupId)
            bundle.putString("KEY_GROUP_NAME", groupName)
            bundle.putString("KEY_GROUP_OWNER", ownerId)
            instance.arguments = bundle
            return instance
        }
    }

    private var userId = ""
    private var groupId = ""
    private var groupName = ""
    private var ownerId = ""
    private lateinit var ui: InviteFragmentUI
    private val users = mutableListOf<User>()
    private lateinit var inviteViewModel: InviteViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        ui = InviteFragmentUI(activity, users)
        return ui.createView(AnkoContext.create(context, this))
    }

    override fun onBindViewModel() {
        onGetInfomationOfUserInvite()
    }

    /**
     * On get list user from search
     */

    /**
     * On item of  RecyclerView click.
     */
    internal fun onItemClick(user: User) {

    }

    internal fun searchUserList(name : String) {
        addDisposables(inviteViewModel.searchListUser(name)
                .observeOnUiThread()
                .subscribe(this :: onSearchUser))
    }

    /**
     * On get list user from search
     */
    internal fun onSearchUser(usersList : List<User>?) {
        users.clear()
        if (usersList != null) {
            users.addAll(usersList!!)
        }
        ui.userListAdapter.notifyDataSetChanged()
    }

    /**
     * On get infomation of user invite
     */
    internal fun onGetInfomationOfUserInvite() {
        userId = arguments.getString(KEY_USER)
        groupId = arguments.getString(KEY_GROUP)
        groupName = arguments.getString("KEY_GROUP_NAME")
        ownerId = arguments.getString("KEY_GROUP_OWNER")
    }
}

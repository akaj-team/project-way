package vn.asiantech.way.ui.group.invite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hypertrack.lib.models.User
import org.jetbrains.anko.AnkoContext
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.ui.base.BaseFragment

/**
 * Invite Fragment
 * @author NgocTTN
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
    private val users = mutableListOf<User>()
    private lateinit var ui: InviteFragmentUI
    private lateinit var inviteViewModel: InviteViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        ui = InviteFragmentUI(users)
        return ui.createView(AnkoContext.create(context, this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Init invite view model
        inviteViewModel = InviteViewModel(context)
        onGetInformationOfUserInvite()
    }

    override fun onBindViewModel() {
        addDisposables(inviteViewModel.resetDataStatus
                .observeOnUiThread()
                .subscribe(this::onResetDataListWhenStartSearch))
    }

    /**
     * On item invite click of  RecyclerView list.
     */
    internal fun onItemInviteClick(user: User) {
        // TODO: Handle save database firebase
    }

    /**
     * On get list user invite from search action
     */
    internal fun onGetListUserInvite(name: String) {
        if (name.isEmpty()) {
            return
        }
        addDisposables(inviteViewModel.searchListUser(name)
                .observeOnUiThread()
                .subscribe(this::onGetListUserInviteComplete))
    }

    /**
     * On back pressed activity
     */
    internal fun onBackPressed() {
        activity.onBackPressed()
    }

    /**
     * On get list user invite complete from search action
     */
    private fun onGetListUserInviteComplete(usersList: List<User>?) {
        if (usersList != null) {
            users.addAll(usersList)
            ui.userListAdapter.notifyDataSetChanged()
        }
    }

    /**
     * On get infomation of user invite
     */
    private fun onGetInformationOfUserInvite() {
        userId = arguments.getString(KEY_USER)
        groupId = arguments.getString(KEY_GROUP)
        groupName = arguments.getString("KEY_GROUP_NAME")
        ownerId = arguments.getString("KEY_GROUP_OWNER")
    }

    /**
     * On reload list when start search action
     */
    private fun onResetDataListWhenStartSearch(isReset: Boolean) {
        if (isReset) {
            users.clear()
            ui.userListAdapter.notifyDataSetChanged()
        }
    }
}

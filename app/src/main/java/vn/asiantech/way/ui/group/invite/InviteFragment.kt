package vn.asiantech.way.ui.group.invite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.FirebaseDatabase
import com.hypertrack.lib.models.User
import org.jetbrains.anko.AnkoContext
import vn.asiantech.way.data.model.Invite
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
        private const val KEY_GROUP_NAME = "key_group_name"
        private const val KEY_GROUP_OWNER = "key_group_owner"

        /**
         * Get instance of InviteFragment
         */
        fun getInstance(userId: String, groupId: String, groupName: String, ownerId: String)
                : InviteFragment {
            val instance = InviteFragment()
            val bundle = Bundle()
            bundle.putString(KEY_USER, userId)
            bundle.putString(KEY_GROUP, groupId)
            bundle.putString(KEY_GROUP_NAME, groupName)
            bundle.putString(KEY_GROUP_OWNER, ownerId)
            instance.arguments = bundle
            return instance
        }
    }

    private lateinit var userId: String
    private lateinit var groupId: String
    private lateinit var groupName: String
    private lateinit var ownerId: String
    private lateinit var ui: InviteFragmentUI
    private lateinit var viewModel: InviteViewModel
    private lateinit var adapter: InviteUserListAdapter
    private val users = mutableListOf<User>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Init invite view model
        viewModel = InviteViewModel(context)
        initInvitedUser()
        // Init adapter
        adapter = InviteUserListAdapter(users)
        // Init UI
        ui = InviteFragmentUI(adapter)
        return ui.createView(AnkoContext.create(context, this))
    }

    override fun onBindViewModel() {
        addDisposables(viewModel.triggerSearchListUser()
                .observeOnUiThread()
                .subscribe(this::handleGetListUserInviteComplete),

                viewModel.resetDataStatus
                        .observeOnUiThread()
                        .subscribe(this::handleClearDataListWhenStartSearch))
        viewModel.searchListUser()
    }

    /**
     * On item invite click of  RecyclerView list.
     */
    internal fun eventItemInviteClicked(user: User) {
        val inviteRef = FirebaseDatabase.getInstance().getReference("user/${user.id}/invites/$groupId")
        inviteRef.setValue(Invite(userId, groupId, groupName, userId == ownerId))
    }

    /**
     * On get list user invite from search action
     */
    internal fun eventUserNameTextChanged(name: String) {
        viewModel.searchListUser(name)
    }

    /**
     * On back pressed activity
     */
    internal fun eventBackButtonPressed() {
        activity.onBackPressed()
    }

    /**
     * On get list user invite complete from search action
     */
    private fun handleGetListUserInviteComplete(usersList: List<User>?) {
        if (usersList != null) {
            users.addAll(usersList)
            ui.userListAdapter.notifyDataSetChanged()
        }
    }

    /**
     * On get infomation of user invite
     */
    private fun initInvitedUser() {
        userId = arguments.getString(KEY_USER)
        groupId = arguments.getString(KEY_GROUP)
        groupName = arguments.getString(KEY_GROUP_NAME)
        ownerId = arguments.getString(KEY_GROUP_OWNER)
    }

    /**
     * On reload list when start search action
     */
    private fun handleClearDataListWhenStartSearch(isReset: Boolean) {
        if (isReset) {
            users.clear()
            ui.userListAdapter.notifyDataSetChanged()
        }
    }
}

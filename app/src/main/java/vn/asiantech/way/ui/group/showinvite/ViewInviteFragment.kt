package vn.asiantech.way.ui.group.showinvite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hypertrack.lib.models.User
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.toast
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.ui.base.BaseFragment

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 05/12/2017.
 */
class ViewInviteFragment : BaseFragment() {

    companion object {

        private const val KEY_USER_ID = "key_user_id"

        /**
         * Get instance of ViewInviteFragment with a given user.
         */
        fun getInstance(userId: String): ViewInviteFragment {
            val instance = ViewInviteFragment()
            val bundle = Bundle()
            bundle.putString(KEY_USER_ID, userId)
            instance.arguments = bundle
            return instance
        }
    }

    private lateinit var userId: String
    private lateinit var ui: ViewInviteFragmentUI
    private var invites = mutableListOf<Invite>()
    private lateinit var adapter: InviteListAdapter
    private var viewInviteViewModel = ViewInviteViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments.getString(KEY_USER_ID)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        adapter = InviteListAdapter(context, invites, object : InviteListAdapter.OnItemClick {
            override fun onOkClick(invite: Invite) {
                eventOnItemClickOk(invite)
            }

            override fun onCancelClick(invite: Invite) {
                eventOnItemClickCancel(invite)
            }

        })
        ui = ViewInviteFragmentUI(adapter)
        return ui.createView(AnkoContext.create(context, this))
    }

    override fun onBindViewModel() {
        addDisposables(viewInviteViewModel
                .getInvitesOfUser(userId)
                .subscribe(
                        this::handleGetInviteSuccess,
                        this::handleGetInviteError)
        )
    }

    private fun handleGetInviteSuccess(invite: Invite) {
        invites.add(invite)
        adapter.notifyDataSetChanged()
    }

    private fun handleGetInviteError(error: Throwable) {
        toast("error when get invite  ${error.message}")
    }

    private fun handleAddUserToGroupSuccess(user: User) {
        // TODO: remove invite pending of user
        // TODO : send broadcast to Group Activity
    }

    private fun handleAddUserToGroupError(error: Throwable) {
        toast("error when add user to group  ${error.message}")
    }

    private fun handleGetCurrentRequestSuccess(invite: Invite) {
        addDisposables(viewInviteViewModel
                .removeInviteOfUserToAnotherGroup(userId, invite)
                .subscribe(this::handleRemoveInviteOfUserToAnotherGroupSuccess))
    }

    private fun handleRemoveInviteOfUserToAnotherGroupSuccess(isSuccess: Boolean) {

    }

    private fun handleGetCurrentRequestError(error: Throwable) {
        toast("error when get current request  ${error.message}")
    }

    private fun eventOnItemClickOk(invite: Invite) {
        if (invite.request) {
            addDisposables(
                    viewInviteViewModel
                            .addUserToGroup(userId, invite.to)
                            .subscribe(
                                    this::handleAddUserToGroupSuccess,
                                    this::handleAddUserToGroupError
                            )
            )
        } else {
            addDisposables(viewInviteViewModel
                    .getCurrentRequest(userId)
                    .subscribe(
                            this::handleGetCurrentRequestSuccess,
                            this::handleGetCurrentRequestError
                    ))
        }
    }

    private fun eventOnItemClickCancel(invite: Invite) {

    }
}

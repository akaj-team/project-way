package vn.asiantech.way.ui.group.info

import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hypertrack.lib.models.User
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.extension.addFragment
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.ui.base.BaseFragment
import vn.asiantech.way.ui.group.invite.InviteFragment
import vn.asiantech.way.utils.AppConstants

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 11/12/2017
 */
class GroupInfoFragment : BaseFragment() {

    private lateinit var ui: GroupInfoFragmentUI
    private lateinit var group: Group
    private var userId = ""
    private var groupId = ""
    private val groupInfoViewModel = GroupInfoViewModel()

    companion object {

        /**
         * This function used to get Instance of GroupInfoFragment
         */
        fun getInstance(userId: String, groupId: String): GroupInfoFragment {
            val instance = GroupInfoFragment()
            val bundle = Bundle()
            bundle.putString(AppConstants.KEY_USER_ID, userId)
            bundle.putString(AppConstants.KEY_GROUP_ID, groupId)
            instance.arguments = bundle
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments.getString(AppConstants.KEY_USER_ID)
        groupId = arguments.getString(AppConstants.KEY_GROUP_ID)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ui = GroupInfoFragmentUI(userId, groupInfoViewModel.users)
        return ui.createView(AnkoContext.create(context, this))
    }

    override fun onBindViewModel() {
        addDisposables(
                groupInfoViewModel.getGroupInfo(groupId)
                        .observeOnUiThread()
                        .subscribe(this::handleGetGroupInfoCompleted),
                groupInfoViewModel.updateMemberList
                        .observeOnUiThread()
                        .subscribe(this::handleGetMemberListCompleted, this::handleGetMemberListFailed)
        )
    }

    internal fun eventViewOnClicked(view: View) {
        when (view.id) {
            R.id.group_info_img_invite -> handleInviteOnClicked()

            R.id.group_info_img_approve -> handleApproveOnClicked()

            R.id.group_info_img_leave_group -> handleLeaveGroupOnClicked()
        }
    }

    internal fun eventImageUpToAdminClicked(userId: String) {
        alert(R.string.confirm_message_change_owner, R.string.confirm) {
            yesButton {
                it.dismiss()
                addDisposables(groupInfoViewModel.changeGroupOwner(groupId, userId)
                        .observeOnUiThread()
                        .subscribe(this@GroupInfoFragment::handleChangeGroupOwnerCompleted))
            }
            noButton {
                it.dismiss()
            }
        }.show()
    }

    internal fun handleSwipeRefreshLayoutOnRefresh() {
        reloadGroupInfo()
    }

    private fun handleInviteOnClicked() {
        activity.addFragment((this.view?.parent as View).id,
                InviteFragment.getInstance(userId, group.id, group.name, group.ownerId))
    }

    private fun handleLeaveGroupOnClicked() {
        if (userId == group.ownerId) {
            alert(R.string.admin_leave_group, R.string.confirm) {
                yesButton {
                    it.dismiss()
                }
            }.show()
        } else {
            alert(R.string.confirm_message_leave_group, R.string.confirm) {
                yesButton {
                    addDisposables(groupInfoViewModel.leaveGroup(userId)
                            .observeOnUiThread()
                            .subscribe(this@GroupInfoFragment::handleLeaveGroupCompleted))
                }
                noButton { it.dismiss() }
            }.show()
        }
    }

    private fun handleApproveOnClicked() {
        // TODO: Add ShowRequestFragment to container view.
    }

    private fun handleGetGroupInfoCompleted(groupToBind: Group) {
        bindGroupInfoToView(groupToBind)
        groupInfoViewModel.getMemberList(groupId)
    }

    private fun bindGroupInfoToView(groupToBind: Group) {
        group = groupToBind
        with(group) {
            ui.memberListAdapter.groupOwnerId = ownerId
            ui.tvGroupName.text = name
            ui.tvCreateAt.text = getString(R.string.create_at, createAt.substring(0,
                    AppConstants.STANDARD_DATE_TIME_LENGTH))
            if (userId == group.ownerId) {
                ui.imgApprove.visibility = View.VISIBLE
            } else {
                ui.imgApprove.visibility = View.GONE
            }
        }
    }

    private fun handleGetMemberListCompleted(diffResult: DiffUtil.DiffResult) {
        diffResult.dispatchUpdatesTo(ui.memberListAdapter)
        ui.tvMembersCount.text = getString(R.string.members_count, groupInfoViewModel.users.size)
    }

    private fun handleGetMemberListFailed(throwable: Throwable) {
        toast(R.string.error_message)
        ui.swipeRefreshLayout.isRefreshing = false
    }

    private fun handleLeaveGroupCompleted(user: User) {
        if (user.groupId != groupId) {
            toast(getString(R.string.leave_group_notification, group.name))
        } else {
            toast(R.string.error_message)
        }
    }

    private fun handleChangeGroupOwnerCompleted(success: Boolean) {
        if (success) {
            toast(R.string.success)
        } else {
            toast(R.string.error_message)
        }
    }

    private fun reloadGroupInfo() {
        addDisposables(groupInfoViewModel.getGroupInfo(groupId).observeOnUiThread()
                .subscribe(this::handleGetGroupInfoCompleted))
    }
}

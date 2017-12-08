package vn.asiantech.way.ui.group.info

import android.os.Bundle
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
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.ui.base.BaseFragment
import vn.asiantech.way.utils.AppConstants

/**
 * Fragment group user
 * Created by haingoq on 28/11/2017.
 */
class GroupInfoFragment : BaseFragment() {

    private lateinit var ui: GroupInfoFragmentUI
    private val groupInfoViewModel = GroupInfoViewModel()
    private var userId = ""
    private var groupId = ""
    private lateinit var group: Group
    private val members = mutableListOf<User>()

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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        ui = GroupInfoFragmentUI(userId, "", members)
        return ui.createView(AnkoContext.create(context, this))
    }

    override fun onBindViewModel() {
        addDisposables(
                groupInfoViewModel.getGroupInfo(groupId)
                        .observeOnUiThread()
                        .subscribe(this::handleGetGroupInfoCompleted)
        )
    }

    internal fun eventViewOnClicked(view: View) {
        when (view.id) {
            R.id.group_info_img_invite -> sendBroadCast(AppConstants.ACTION_CALL_INVITE_FRAGMENT)

            R.id.group_info_img_approve -> sendBroadCast(AppConstants.ACTION_CALL_VIEW_GROUP_REQUEST_FRAGMENT)

            R.id.group_info_img_leave_group -> handleLeaveGroupOnClicked()
        }
    }

    internal fun eventImageUpToAdminClicked(userId: String) {
        alert(R.string.confirm_message_change_owner, R.string.confirm) {
            yesButton {
                it.dismiss()
                groupInfoViewModel.changeGroupOwner(groupId, userId)
                        .observeOnUiThread()
                        .subscribe(this@GroupInfoFragment::handleChangeGroupOwnerCompleted)
            }
            noButton {
                it.dismiss()
            }
        }.show()
    }

    internal fun handleSwipeRefreshLayoutOnRefresh() {
        reloadGroupInfo()
    }

    private fun handleLeaveGroupOnClicked() {
        alert(R.string.confirm_message_leave_group, R.string.confirm) {
            yesButton {
                addDisposables(groupInfoViewModel.leaveGroup(userId)
                        .observeOnUiThread()
                        .subscribe(this@GroupInfoFragment::handleLeaveGroupCompleted))

            }
            noButton { dialog -> dialog.dismiss() }
        }.show()
    }

    private fun handleGetGroupInfoCompleted(groupToBind: Group) {
        bindGroupInfoToView(groupToBind)
        addDisposables(
                groupInfoViewModel.getMemberList(groupId)
                        .observeOnUiThread()
                        .subscribe(this::handleGetMemberListCompleted, {
                            toast(R.string.error_message)
                            ui.swipeRefreshLayout.isRefreshing = false
                        })
        )
    }

    private fun bindGroupInfoToView(groupToBind: Group) {
        group = groupToBind
        with(group) {
            ui.memberListAdapter.groupOwnerId = group.ownerId
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

    private fun handleGetMemberListCompleted(users: MutableList<User>) {
        ui.swipeRefreshLayout.isRefreshing = false
        ui.tvMembersCount.text = getString(R.string.members_count, users.size)
        members.clear()
        members.addAll(users)
        ui.memberListAdapter.notifyDataSetChanged()
    }

    private fun handleLeaveGroupCompleted(user: User) {
        if (user.groupId != groupId) {
            toast(R.string.leave_group_notification)
        } else {
            toast(R.string.error_message)
        }
    }

    private fun handleChangeGroupOwnerCompleted(success: Boolean) {
        if (success) {
            toast(getString(R.string.leave_group_notification, group.name))
            sendBroadCast(AppConstants.ACTION_RELOAD)
        } else {
            toast(R.string.error_message)
        }
    }

    private fun reloadGroupInfo() {
        groupInfoViewModel.getGroupInfo(groupId)
                .observeOnUiThread()
                .subscribe(this::handleGetGroupInfoCompleted)
    }
}

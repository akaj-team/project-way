package vn.asiantech.way.ui.group.info

import android.os.Bundle
import android.util.Log
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
        ui = GroupInfoFragmentUI(userId, members)
        return ui.createView(AnkoContext.create(context, this))
    }

    override fun onBindViewModel() {
        addDisposables(
                groupInfoViewModel.getGroupInfo(groupId)
                        .observeOnUiThread()
                        .subscribe(this::bindGroupInfo)
        )
    }

    internal fun callToInviteFragment() {
        sendBroadCast(AppConstants.ACTION_CALL_INVITE_FRAGMENT)
    }

    internal fun callToViewRequestFragment() {
        sendBroadCast(AppConstants.ACTION_CALL_VIEW_GROUP_REQUEST_FRAGMENT)
    }

    internal fun leaveGroup() {
        alert(R.string.confirm_message_leave_group, R.string.confirm) {
            yesButton {
                addDisposables(groupInfoViewModel.leaveGroup(userId)
                        .observeOnUiThread()
                        .subscribe(this@GroupInfoFragment::afterLeaveGroup))
            }
            noButton { dialog -> dialog.dismiss() }
        }.show()
    }

    private fun bindGroupInfo(groupToBind: Group) {
        group = groupToBind
        with(group) {
            ui.tvGroupName.text = name
            ui.tvCreateAt.text = getString(R.string.create_at, createAt.substring(0,
                    AppConstants.STANDARD_DATE_TIME_LENGTH))
            if (userId == group.ownerId) {
                ui.imgApprove.visibility = View.VISIBLE
            } else {
                ui.imgApprove.visibility = View.GONE
            }
        }
        addDisposables(
                groupInfoViewModel.getMemberList(groupId)
                        .observeOnUiThread()
                        .subscribe(this::updateMemberList)
        )
    }

    private fun updateMemberList(users: MutableList<User>) {
        ui.tvMembersCount.text = getString(R.string.members_count, users.size)
        members.clear()
        members.addAll(users)
        ui.memberListAdapter.notifyDataSetChanged()
    }

    private fun afterLeaveGroup(user: User) {
        if (user.groupId != groupId) {
            Log.i("tag11", "ok")
            sendBroadCast(AppConstants.ACTION_RELOAD)
        } else {
            toast(R.string.error_message)
        }
    }
}

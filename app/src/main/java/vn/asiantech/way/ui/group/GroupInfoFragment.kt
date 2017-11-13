package vn.asiantech.way.ui.group

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hypertrack.lib.models.User
import kotlinx.android.synthetic.main.fragment_group_info.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.asiantech.way.R
import vn.asiantech.way.data.model.group.BodyAddUserToGroup
import vn.asiantech.way.data.model.group.Group
import vn.asiantech.way.data.model.group.UserListResult
import vn.asiantech.way.data.remote.hypertrackremote.HypertrackRemote
import vn.asiantech.way.ui.base.BaseFragment

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 10/11/2017
 */
class GroupInfoFragment : BaseFragment() {

    companion object {
        const val KEY_GROUP = "group"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_GROUP_ID = "group_id"
        private const val BEGIN_INDEX = 0
        private const val SUBSTRING_LENGTH = 10

        /**
         * Get instance with given user id and group.
         */
        fun getInstance(userId: String, group: Group): GroupInfoFragment {
            val instance = GroupInfoFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_GROUP, group)
            bundle.putString(KEY_USER_ID, userId)
            instance.arguments = bundle
            return instance
        }

        /**
         * Get instance with given user id and group id.
         */
        fun getInstance(userId: String, groupId: String): GroupInfoFragment {
            val instance = GroupInfoFragment()
            val bundle = Bundle()
            bundle.putString(KEY_GROUP_ID, groupId)
            bundle.putString(KEY_USER_ID, userId)
            instance.arguments = bundle
            return instance
        }
    }

    private var userId: String = ""
    private var group: Group? = null
    private val members = mutableListOf<User>()
    private lateinit var adapter: MemberListAdapter
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        group = arguments.getSerializable(KEY_GROUP) as? Group
        userId = arguments.getString(KEY_USER_ID)
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage(getString(R.string.processing))
        progressDialog.setCancelable(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        adapter = MemberListAdapter(userId, members)
        val view = inflater.inflate(R.layout.fragment_group_info, container, false)
        initView(view)
        onClick(view)
        view.recyclerViewMembers.layoutManager = LinearLayoutManager(context)
        view.recyclerViewMembers.adapter = adapter
        return view
    }

    private fun initView(view: View) {
        if (group != null) {
            view.tvGroupName.text = group?.name
            view.tvCreateAt.text = getString(R.string.create_at, group?.createAt
                    ?.substring(BEGIN_INDEX, SUBSTRING_LENGTH))
            loadGroupMemberList(view)
        } else {
            progressDialog.show()
            val groupId = arguments.getString(KEY_GROUP_ID)
            HypertrackRemote.getApiService().getGroupInfo(groupId)
                    .enqueue(object : Callback<Group> {
                        override fun onFailure(call: Call<Group>?, t: Throwable?) {
                            Toast.makeText(context, R.string.error_message,
                                    Toast.LENGTH_LONG).show()
                            progressDialog.dismiss()
                        }

                        override fun onResponse(call: Call<Group>?, response: Response<Group>?) {
                            group = response?.body()
                            progressDialog.dismiss()
                            if (group != null) {
                                initView(view)
                                return
                            }
                            Toast.makeText(context, R.string.error_message,
                                    Toast.LENGTH_LONG).show()
                        }

                    })
        }
    }

    private fun loadGroupMemberList(view: View) {
        val id = group?.id
        members.clear()
        adapter.notifyDataSetChanged()
        if (id != null) {
            HypertrackRemote.getApiService().getMembersList(id)
                    .enqueue(object : Callback<UserListResult> {
                        override fun onResponse(call: Call<UserListResult>?,
                                                response: Response<UserListResult>?) {
                            val result = response?.body()?.results
                            if (result != null) {
                                members.addAll(result)
                                view.tvMembersCount.text = getString(R.string.members_count,
                                        members.size)
                                adapter.notifyDataSetChanged()
                            }
                            if (view.swipeRefreshLayout.isRefreshing) {
                                view.swipeRefreshLayout.isRefreshing = false
                            }
                        }

                        override fun onFailure(call: Call<UserListResult>?, t: Throwable?) {
                            if (view.swipeRefreshLayout.isRefreshing) {
                                view.swipeRefreshLayout.isRefreshing = false
                            }
                            Toast.makeText(context, R.string.can_not_get_members_list,
                                    Toast.LENGTH_LONG).show()
                        }
                    })
        }
    }

    private fun leaveGroup() {
        HypertrackRemote.getApiService().removeUserFromGroup(userId,
                BodyAddUserToGroup(null))
                .enqueue(object : Callback<User> {
                    override fun onFailure(call: Call<User>?, t: Throwable?) {
                        Toast.makeText(context, R.string.error_message,
                                Toast.LENGTH_LONG).show()
                        if (progressDialog.isIndeterminate) {
                            progressDialog.dismiss()
                        }
                    }

                    override fun onResponse(call: Call<User>?, response: Response<User>?) {
                        if (progressDialog.isIndeterminate) {
                            progressDialog.dismiss()
                        }
                        Toast.makeText(context, getString(R.string.leave_group_notification,
                                group?.name),
                                Toast.LENGTH_LONG).show()
                        activity.sendBroadcast(Intent(GroupActivity.ACTION_LEAVE_GROUP))
                    }
                })
    }

    private fun onClick(view: View) {
        view.imgInvite.setOnClickListener {
            // TODO: Invite a given person to group
        }

        view.imgLeave.setOnClickListener {
            showConfirmDialog()
        }

        view.swipeRefreshLayout.setOnRefreshListener {
            loadGroupMemberList(view)
        }
    }

    private fun showConfirmDialog() {
        AlertDialog.Builder(context)
                .setTitle(R.string.confirm)
                .setMessage(R.string.confirm_message_leave_group)
                .setPositiveButton(R.string.ok) { p0, _ ->
                    p0?.dismiss()
                    leaveGroup()
                }
                .setNegativeButton(R.string.cancel) { p0, _ -> p0?.dismiss() }
                .create()
                .show()
    }
}

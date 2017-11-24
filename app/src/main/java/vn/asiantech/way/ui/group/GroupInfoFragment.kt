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
import kotlinx.android.synthetic.main.fragment_group_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.asiantech.way.R
import vn.asiantech.way.data.model.BodyAddUserToGroup
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.model.UserListResult
import vn.asiantech.way.data.source.remote.hypertrackapi.HypertrackApi
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
        return inflater.inflate(R.layout.fragment_group_info, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initOnclick()
        recyclerViewMembers.layoutManager = LinearLayoutManager(context)
        recyclerViewMembers.adapter = adapter
    }


    private fun initView() {
        if (group != null) {
            tvGroupName.text = group?.name
            tvCreateAt.text = getString(R.string.create_at, group?.createAt
                    ?.substring(BEGIN_INDEX, SUBSTRING_LENGTH))
            loadGroupMemberList()
        } else {
            progressDialog.show()
            val groupId = arguments.getString(KEY_GROUP_ID)
            HypertrackApi.getApiService().getGroupInfo(groupId)
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
                                initView()
                                return
                            }
                            Toast.makeText(context, R.string.error_message,
                                    Toast.LENGTH_LONG).show()
                        }
                    })
        }
    }

    private fun loadGroupMemberList() {
        val id = group?.id
        members.clear()
        adapter.notifyDataSetChanged()
        if (id != null) {
            HypertrackApi.getApiService().getMembersList(id)
                    .enqueue(object : Callback<UserListResult> {
                        override fun onResponse(call: Call<UserListResult>?,
                                                response: Response<UserListResult>?) {
                            val result = response?.body()?.results
                            if (result != null) {
                                members.addAll(result)
                                tvMembersCount.text = getString(R.string.members_count,
                                        members.size)
                                adapter.notifyDataSetChanged()
                            }
                            if (swipeRefreshLayout.isRefreshing) {
                                swipeRefreshLayout.isRefreshing = false
                            }
                        }

                        override fun onFailure(call: Call<UserListResult>?, t: Throwable?) {
                            if (swipeRefreshLayout.isRefreshing) {
                                swipeRefreshLayout.isRefreshing = false
                            }
                            Toast.makeText(context, R.string.can_not_get_members_list,
                                    Toast.LENGTH_LONG).show()
                        }
                    })
        }
    }

    private fun leaveGroup() {
        HypertrackApi.getApiService().removeUserFromGroup(userId,
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

    private fun initOnclick() {
        imgInvite.setOnClickListener {
            // TODO: Invite a given person to group
        }

        imgLeave.setOnClickListener {
            showConfirmDialog()
        }

        swipeRefreshLayout.setOnRefreshListener {
            loadGroupMemberList()
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

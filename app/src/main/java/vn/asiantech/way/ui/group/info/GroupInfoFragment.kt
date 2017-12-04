package vn.asiantech.way.ui.group.info

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.hypertrack.lib.models.User
import kotlinx.android.synthetic.main.fragment_group_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.asiantech.way.R
import vn.asiantech.way.data.model.group.BodyAddUserToGroup
import vn.asiantech.way.data.model.group.Group
import vn.asiantech.way.data.model.group.GroupInfo
import vn.asiantech.way.data.model.group.UserListResult
import vn.asiantech.way.data.remote.hypertrackremote.HypertrackApi
import vn.asiantech.way.ui.base.BaseFragment
import vn.asiantech.way.ui.group.GroupActivity

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
    private var groupInfo: GroupInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        group = arguments.getSerializable(KEY_GROUP) as? Group
        userId = arguments.getString(KEY_USER_ID)
        if (group != null) {
            loadGroupInfo()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        adapter = MemberListAdapter(userId, members)
        return inflater.inflate(R.layout.fragment_group_info, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewMembers.layoutManager = LinearLayoutManager(context)
        recyclerViewMembers.adapter = adapter
        initView()
        initOnclick()
    }


    private fun initView() {
        imgInvite.isEnabled = false
        if (group != null) {
            tvName.text = group?.name
            tvCreateAt.text = getString(R.string.create_at, group?.createAt
                    ?.substring(BEGIN_INDEX, SUBSTRING_LENGTH))
            loadGroupMemberList()
        } else {
            progressDialog.show()
            val groupId = arguments.getString(KEY_GROUP_ID)
            HypertrackApi.getApiService().getGroupInfo(groupId)
                    .enqueue(object : Callback<Group> {
                        override fun onFailure(call: Call<Group>?, t: Throwable?) {
                            showToast(R.string.error_message)
                            progressDialog.dismiss()
                        }

                        override fun onResponse(call: Call<Group>?, response: Response<Group>?) {
                            group = response?.body()
                            progressDialog.dismiss()
                            if (group != null) {
                                loadGroupInfo()
                                initView()
                                return
                            }
                            showToast(R.string.error_message)
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
                            showToast(R.string.can_not_get_members_list)
                        }
                    })
        }
    }

    private fun leaveGroup() {
        if (!progressDialog.isShowing) {
            progressDialog.show()
        }
        HypertrackApi.getApiService().removeUserFromGroup(userId, BodyAddUserToGroup(null))
                .enqueue(object : Callback<User> {
                    override fun onFailure(call: Call<User>?, t: Throwable?) {
                        Toast.makeText(context, R.string.error_message,
                                Toast.LENGTH_LONG).show()
                        if (progressDialog.isShowing) {
                            progressDialog.dismiss()
                        }
                    }

                    override fun onResponse(call: Call<User>?, response: Response<User>?) {
                        if (progressDialog.isShowing) {
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
            val intent = Intent(GroupActivity.ACTION_CALL_INVITE_FRAGMENT)
            intent.putExtra(GroupActivity.KEY_GROUP_OWNER, groupInfo?.ownerId)
            intent.putExtra(GroupActivity.KEY_GROUP_NAME, groupInfo?.name)
            activity.sendBroadcast(intent)
        }

        imgLeave.setOnClickListener {
            showConfirmDialog()
        }

        imgCheckRequest.setOnClickListener {
            sendBroadcast(GroupActivity.ACTION_VIEW_REQUEST)
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

    private fun loadGroupInfo() {
        if (imgInvite != null) {
            imgInvite.isEnabled = false
        }
        firebaseDatabase.getReference("group/" + group?.id + "/info")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) = Unit

                    override fun onDataChange(p0: DataSnapshot?) {
                        if (imgInvite != null) {
                            imgInvite.isEnabled = true
                        }
                        groupInfo = Gson().fromJson(Gson().toJson(p0?.value),
                                GroupInfo::class.java)
                        if (userId == groupInfo?.ownerId) {
                            imgCheckRequest.visibility = View.VISIBLE
                        } else {
                            imgCheckRequest.visibility = View.GONE
                        }
                    }
                })
    }
}

package vn.asiantech.way.ui.group.invite

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hypertrack.lib.models.User
import kotlinx.android.synthetic.main.fragment_invite.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.asiantech.way.R
import vn.asiantech.way.data.model.group.Invite
import vn.asiantech.way.data.model.group.UserListResult
import vn.asiantech.way.data.remote.hypertrackremote.HypertrackApi
import vn.asiantech.way.ui.base.BaseFragment
import vn.asiantech.way.ui.group.GroupActivity

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 17/11/2017
 */
class InviteFragment : BaseFragment() {

    companion object {
        private const val KEY_USER = "key_user_id"
        private const val KEY_GROUP = "key_group_id"

        fun getInstance(userId: String, groupId: String, groupName: String, ownerId: String): InviteFragment {
            val instance = InviteFragment()
            val bundle = Bundle()
            bundle.putString(KEY_USER, userId)
            bundle.putString(KEY_GROUP, groupId)
            bundle.putString(GroupActivity.KEY_GROUP_NAME, groupName)
            bundle.putString(GroupActivity.KEY_GROUP_OWNER, ownerId)
            instance.arguments = bundle
            return instance
        }
    }

    private var userId = ""
    private var groupId = ""
    private var groupName = ""
    private var ownerId = ""
    private var users = mutableListOf<User>()
    private lateinit var adapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments.getString(KEY_USER)
        groupId = arguments.getString(KEY_GROUP)
        groupName = arguments.getString(GroupActivity.KEY_GROUP_NAME)
        ownerId = arguments.getString(GroupActivity.KEY_GROUP_OWNER)
        adapter = UserListAdapter(users, object : UserListAdapter.OnItemOnclick {
            override fun onInviteOnclick(user: String) {
                val inviteRef = firebaseDatabase.getReference("user/$user/invites/$groupId")
                inviteRef.setValue(Invite(userId, groupId, groupName, userId == ownerId))
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_invite, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewUsers.layoutManager = LinearLayoutManager(context)
        recyclerViewUsers.adapter = adapter
        initItemEvent()
    }

    private fun initItemEvent() {
        imgBtnBack.setOnClickListener {
            activity.onBackPressed()
        }
        edtUserName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    searchUser(p0.toString())
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        })
    }

    private fun searchUser(query: String) {
        users.clear()
        adapter.notifyDataSetChanged()
        HypertrackApi.getApiService().searchUser(query)
                .enqueue(object : Callback<UserListResult> {
                    override fun onFailure(call: Call<UserListResult>?, t: Throwable?) {
                        showToast(R.string.error_message)
                    }

                    override fun onResponse(call: Call<UserListResult>?,
                                            response: Response<UserListResult>?) {
                        if (query == edtUserName.text.toString()) {
                            val rs = response?.body()?.results
                            if (rs != null) {
                                users.clear()
                                users.addAll(rs)
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                })
    }
}

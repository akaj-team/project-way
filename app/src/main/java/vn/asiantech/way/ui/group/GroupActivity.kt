package vn.asiantech.way.ui.group

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.hypertrack.lib.HyperTrack
import com.hypertrack.lib.callbacks.HyperTrackCallback
import com.hypertrack.lib.models.ErrorResponse
import com.hypertrack.lib.models.SuccessResponse
import com.hypertrack.lib.models.User
import vn.asiantech.way.R
import vn.asiantech.way.data.model.group.Group
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.group.create.CreateGroupFragment
import vn.asiantech.way.ui.group.home.GroupHomeFragment
import vn.asiantech.way.ui.group.info.GroupInfoFragment
import vn.asiantech.way.ui.group.invite.InviteFragment
import vn.asiantech.way.ui.group.reload.ReloadFragment
import vn.asiantech.way.ui.group.showrequest.ViewRequestFragment
import vn.asiantech.way.ui.group.search.SearchGroupFragment
import vn.asiantech.way.ui.group.showinvite.ViewInviteFragment

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 09/11/2017
 */
class GroupActivity : BaseActivity() {

    companion object {
        const val KEY_GROUP_OWNER = "key_owner"
        const val KEY_GROUP_NAME = "key_group_name"
        const val ACTION_GROUP_CREATED = "action_group_create"
        const val ACTION_RELOAD = "action_reload"
        const val ACTION_CALL_CREATE_GROUP_FRAGMENT = "action_call_create_group"
        const val ACTION_VIEW_INVITES = "action_view_invites"
        const val ACTION_BACK = "action_back"
        const val ACTION_LEAVE_GROUP = "action_leave_group"
        const val ACTION_BACK_TO_HOME = "action_back_to_home"
        const val ACTION_SEARCH_GROUP = "action_search_group"
        const val ACTION_CALL_INVITE_FRAGMENT = "action_invite_fragment"
        const val ACTION_JOIN_TO_GROUP = "action_join_to_group"
        const val ACTION_VIEW_REQUEST = "action_view_request"
    }

    private lateinit var user: User
    private lateinit var progressDialog: ProgressDialog
    private val fbDatabase = FirebaseDatabase.getInstance()
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent) {
            when (p1.action) {
                ACTION_RELOAD -> loadUser()

                ACTION_GROUP_CREATED -> {
                    val group = p1.extras.getSerializable(GroupInfoFragment.KEY_GROUP) as? Group
                    loadUser()
                    if (group != null) {
                        replaceFragment(GroupInfoFragment.getInstance(user.id, group))
                    }
                }

                ACTION_CALL_CREATE_GROUP_FRAGMENT -> replaceFragment(CreateGroupFragment
                        .getInstance(user))

                ACTION_VIEW_INVITES -> addFragment(ViewInviteFragment.getInstance(user.id))

                ACTION_BACK -> finish()

                ACTION_LEAVE_GROUP, ACTION_BACK_TO_HOME -> replaceFragment(GroupHomeFragment())

                ACTION_SEARCH_GROUP -> replaceFragment(SearchGroupFragment.getInstance(user))

                ACTION_CALL_INVITE_FRAGMENT -> {
                    val ownerId = p1.getStringExtra(KEY_GROUP_OWNER)
                    val groupName = p1.getStringExtra(KEY_GROUP_NAME)
                    addFragment(InviteFragment.getInstance(user.id, user.groupId, groupName,
                            ownerId))
                }

                ACTION_JOIN_TO_GROUP -> {
                    val tmp = p1.extras.getSerializable(ACTION_JOIN_TO_GROUP) as? User
                    if (tmp != null) {
                        user = tmp
                        replaceFragment(GroupInfoFragment.getInstance(user.id, user.groupId))
                    }
                }

                ACTION_VIEW_REQUEST -> addFragment(ViewRequestFragment.getInstance(user.groupId))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage(getString(R.string.processing))
        progressDialog.setCancelable(false)
        loadUser()
        registerBroadcast()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun loadUser() {
        progressDialog.show()
        HyperTrack.getUser(object : HyperTrackCallback() {
            override fun onSuccess(p0: SuccessResponse) {
                user = Gson().fromJson<User>(Gson().toJson(p0.responseObject).toString(),
                        User::class.java)
                if (user.groupId == null) {
                    replaceFragment(GroupHomeFragment())
                } else {
                    replaceFragment(GroupInfoFragment.getInstance(user.id, user.groupId))
                }
                initDatabaseReferences()
                progressDialog.dismiss()
            }

            override fun onError(p0: ErrorResponse) {
                Toast.makeText(this@GroupActivity, getString(R.string.error_message),
                        Toast.LENGTH_LONG).show()
                replaceFragment(ReloadFragment())
                progressDialog.dismiss()
            }
        })
    }

    private fun registerBroadcast() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_GROUP_CREATED)
        intentFilter.addAction(ACTION_RELOAD)
        intentFilter.addAction(ACTION_CALL_CREATE_GROUP_FRAGMENT)
        intentFilter.addAction(ACTION_VIEW_INVITES)
        intentFilter.addAction(ACTION_BACK)
        intentFilter.addAction(ACTION_LEAVE_GROUP)
        intentFilter.addAction(ACTION_BACK_TO_HOME)
        intentFilter.addAction(ACTION_SEARCH_GROUP)
        intentFilter.addAction(ACTION_CALL_INVITE_FRAGMENT)
        intentFilter.addAction(ACTION_JOIN_TO_GROUP)
        intentFilter.addAction(ACTION_VIEW_REQUEST)
        registerReceiver(receiver, intentFilter)
    }

    private fun initDatabaseReferences() {
        val groupRef = fbDatabase.getReference("user/${user.id}/groupId")
        groupRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) = Unit

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0?.value != null && p0.value.toString() != user.groupId) {
                    loadUser()
                }
            }
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flContent, fragment)
        transaction.commit()
    }

    private fun addFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack("")
        transaction.add(R.id.flContent, fragment)
        transaction.commit()
    }
}

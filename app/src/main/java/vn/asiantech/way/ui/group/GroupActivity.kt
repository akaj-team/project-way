package vn.asiantech.way.ui.group

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.hypertrack.lib.HyperTrack
import com.hypertrack.lib.callbacks.HyperTrackCallback
import com.hypertrack.lib.models.ErrorResponse
import com.hypertrack.lib.models.SuccessResponse
import com.hypertrack.lib.models.User
import vn.asiantech.way.R
import vn.asiantech.way.data.model.group.Group
import vn.asiantech.way.ui.base.BaseActivity

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 09/11/2017
 */
class GroupActivity : BaseActivity() {

    companion object {
        const val ACTION_GROUP_CREATED = "action_group_create"
        const val ACTION_RELOAD = "action_reload"
        const val ACTION_CALL_CREATE_GROUP_FRAGMENT = "action_call_create_group"
        const val ACTION_VIEW_INVITES = "action_view_invites"
        const val ACTION_BACK = "action_back"
        const val ACTION_LEAVE_GROUP = "action_leave_group"
    }

    private lateinit var user: User
    private lateinit var progressDialog: ProgressDialog
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent) {
            when (p1.action) {
                ACTION_RELOAD -> {
                    loadUser()
                }

                ACTION_GROUP_CREATED -> {
                    val group = p1.extras.getSerializable(GroupInfoFragment.KEY_GROUP) as? Group
                    if (group != null) {
                        replaceFragment(GroupInfoFragment.getInstance(user.id, group))
                    }
                }

                ACTION_CALL_CREATE_GROUP_FRAGMENT -> {
                    replaceFragment(CreateGroupFragment.getInstance(user))
                }

                ACTION_VIEW_INVITES -> {
                    // TODO: Call Invites fragment
                }

                ACTION_BACK -> {
                    finish()
                }

                ACTION_LEAVE_GROUP -> {
                    replaceFragment(NonGroupMemberFragment())
                }
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
                    replaceFragment(NonGroupMemberFragment())
                } else {
                    replaceFragment(GroupInfoFragment.getInstance(user.id, user.groupId))
                }
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
        registerReceiver(receiver, intentFilter)
    }

    private fun replaceFragment(fragment: android.support.v4.app.Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flContent, fragment)
        transaction.commit()
    }
}

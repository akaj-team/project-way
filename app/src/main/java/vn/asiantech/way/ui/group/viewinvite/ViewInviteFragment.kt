package vn.asiantech.way.ui.group.viewinvite

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import com.google.gson.Gson
import com.hypertrack.lib.models.User
import kotlinx.android.synthetic.main.fragment_view_invites.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.asiantech.way.R
import vn.asiantech.way.data.model.group.BodyAddUserToGroup
import vn.asiantech.way.data.model.group.Invite
import vn.asiantech.way.data.remote.hypertrackremote.HypertrackApi
import vn.asiantech.way.ui.base.BaseFragment
import vn.asiantech.way.ui.group.GroupActivity

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 17/11/2017
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
    private var currentRequest: Invite? = null
    private lateinit var currentRequestRef: DatabaseReference
    private lateinit var invitesRef: DatabaseReference
    private lateinit var adapter: InviteListAdapter
    private val invites = mutableListOf<Invite>()
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments.getString(KEY_USER_ID)
        currentRequestRef = firebaseDatabase.getReference("user/$userId/request")
        invitesRef = firebaseDatabase.getReference("user/$userId/invites")
        currentRequestRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) = Unit

            override fun onDataChange(p0: DataSnapshot?) {
                currentRequest = gson.fromJson(gson.toJson(p0?.value), Invite::class.java)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_invites, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = InviteListAdapter(invites, object : InviteListAdapter.OnItemClick {
            override fun onOkClick(invite: Invite) {
                if (invite.request) {
                    if (!progressDialog.isShowing) {
                        progressDialog.show()
                    }
                    HypertrackApi.getApiService().addUserToGroup(userId,
                            BodyAddUserToGroup(invite.to))
                            .enqueue(object : Callback<User> {
                                override fun onFailure(call: Call<User>?, t: Throwable?) {
                                    if (progressDialog.isShowing) {
                                        progressDialog.dismiss()
                                    }
                                    showToast(R.string.error_message)
                                }

                                override fun onResponse(call: Call<User>?,
                                                        response: Response<User>?) {
                                    if (progressDialog.isShowing) {
                                        progressDialog.dismiss()
                                    }
                                    val user = response?.body()
                                    if (user != null) {
                                        val intent = Intent(GroupActivity.ACTION_JOIN_TO_GROUP)
                                        val bundle = Bundle()
                                        bundle.putSerializable(GroupActivity.ACTION_JOIN_TO_GROUP,
                                                user)
                                        intent.putExtras(bundle)
                                        activity.sendBroadcast(intent)
                                        val inviteRef = firebaseDatabase.getReference("user/" +
                                                "$userId/invites/${invite.to}")
                                        inviteRef.removeValue()
                                    } else {
                                        showToast(R.string.error_message)
                                    }
                                }
                            })
                } else {
                    val inviteRef = firebaseDatabase.getReference("user/" +
                            "$userId/invites/${invite.to}")
                    inviteRef.removeValue()
                    invite.request = true
                    val currentRequestGroupRef = firebaseDatabase.getReference("group/" +
                            "${currentRequest?.to}/$userId")
                    currentRequestGroupRef.removeValue()
                    currentRequestRef.setValue(invite)
                    val groupRef = firebaseDatabase.getReference("group/" +
                            "${invite.to}/request/$userId")
                    invite.to = userId
                    groupRef.setValue(invite)
                }
            }

            override fun onCancelClick(invite: Invite) {
                val inviteRef = firebaseDatabase.getReference("user/" +
                        "$userId/invites/${invite.to}")
                inviteRef.removeValue()
            }
        })
        recyclerViewInvites.layoutManager = LinearLayoutManager(context)
        recyclerViewInvites.adapter = adapter
        invitesRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) = Unit

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) = Unit

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) = Unit

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                invites.add(gson.fromJson(gson.toJson(p0?.value), Invite::class.java))
                adapter.notifyItemInserted(invites.size - 1)
            }

            override fun onChildRemoved(p0: DataSnapshot?) = Unit
        })
    }
}

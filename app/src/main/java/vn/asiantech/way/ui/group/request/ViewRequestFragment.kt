package vn.asiantech.way.ui.group.request

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import com.hypertrack.lib.models.User
import kotlinx.android.synthetic.main.fragment_view_requests.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.asiantech.way.R
import vn.asiantech.way.data.model.group.BodyAddUserToGroup
import vn.asiantech.way.data.model.group.Invite
import vn.asiantech.way.data.remote.hypertrackremote.HypertrackApi
import vn.asiantech.way.ui.base.BaseFragment

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 20/11/2017
 */
class ViewRequestFragment : BaseFragment() {

    companion object {

        const val KEY_GROUP_ID = "key_group_id"

        /**
         * Get instance of ViewRequestFragment with a given group.
         */
        fun getInstance(groupId: String): ViewRequestFragment {
            val instance = ViewRequestFragment()
            val bundle = Bundle()
            bundle.putString(KEY_GROUP_ID, groupId)
            instance.arguments = bundle
            return instance
        }
    }

    private lateinit var requestsRef: DatabaseReference
    private lateinit var adapter: RequestListAdapter
    private val requestUsers = mutableListOf<User>()
    private var groupId = ""
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupId = arguments.getString(KEY_GROUP_ID)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_requests, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initDatabaseReferences()
    }

    private fun initAdapter() {
        adapter = RequestListAdapter(requestUsers, object : RequestListAdapter.OnViewItemClick {
            override fun onOkClick(userId: String) {
                addUserToGroup(userId)
            }

            override fun onCancelClick(userId: String) {
                val requestRef = firebaseDatabase.getReference("group/$groupId" +
                        "/request/$userId")
                requestRef.removeValue()
                val userRequestRef = firebaseDatabase.getReference("user/$userId" +
                        "/request")
                userRequestRef.removeValue()
            }
        })
        recyclerViewRequests.layoutManager = LinearLayoutManager(context)
        recyclerViewRequests.adapter = adapter
    }

    private fun initDatabaseReferences() {
        requestsRef = firebaseDatabase.getReference("group/$groupId/request")
        requestsRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) = Unit

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) = Unit

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) = Unit

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val invite = gson.fromJson(gson.toJson(p0?.value), Invite::class.java)
                HypertrackApi.getApiService().getUserProfile(invite.to)
                        .enqueue(object : retrofit2.Callback<User> {
                            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                                val user = response?.body()
                                if (user != null) {
                                    requestUsers.add(user)
                                    adapter.notifyItemInserted(requestUsers.size - 1)
                                }
                            }

                            override fun onFailure(call: Call<User>?, t: Throwable?) = Unit
                        })
            }

            override fun onChildRemoved(p0: DataSnapshot?) = Unit
        })
    }

    private fun addUserToGroup(userId: String) {
        progressDialog.show()
        HypertrackApi.getApiService().addUserToGroup(userId, BodyAddUserToGroup(groupId))
                .enqueue(object : Callback<User> {
                    override fun onFailure(call: Call<User>?, t: Throwable?) {
                        if (progressDialog.isShowing) {
                            progressDialog.show()
                        }
                        showToast(R.string.error_message)
                    }

                    override fun onResponse(call: Call<User>?, response: Response<User>?) {
                        if (progressDialog.isShowing) {
                            progressDialog.dismiss()
                        }
                        val user = response?.body()
                        if (user != null) {
                            val requestRef = firebaseDatabase.getReference("group/$groupId" +
                                    "/request/$userId")
                            requestRef.removeValue()
                            val userRequestRef = firebaseDatabase.getReference("user/$userId" +
                                    "/request")
                            userRequestRef.removeValue()
                            val userGroup = firebaseDatabase.getReference("user/$userId/groupId")
                            userGroup.setValue(groupId)
                            showToast(R.string.success)
                        }
                    }
                })
    }
}

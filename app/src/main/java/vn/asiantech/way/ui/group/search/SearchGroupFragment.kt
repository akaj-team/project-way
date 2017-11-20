package vn.asiantech.way.ui.group.search

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.hypertrack.lib.models.User
import kotlinx.android.synthetic.main.fragment_search_group.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.asiantech.way.R
import vn.asiantech.way.data.model.group.Group
import vn.asiantech.way.data.model.group.Invite
import vn.asiantech.way.data.model.group.SearchGroupResult
import vn.asiantech.way.data.remote.hypertrackremote.HypertrackApi
import vn.asiantech.way.ui.base.BaseFragment
import vn.asiantech.way.ui.group.GroupActivity

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 16/11/2017
 */
class SearchGroupFragment : BaseFragment() {

    companion object {

        const val KEY_USER = "key_user"

        fun getInstance(user: User): SearchGroupFragment {
            val instance = SearchGroupFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_USER, user)
            instance.arguments = bundle
            return instance
        }
    }

    private var user: User? = null
    private var groups = mutableListOf<Group>()
    private lateinit var adapter: GroupListAdapter
    private lateinit var userRef: DatabaseReference
    private var currentRequest: Invite? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = arguments.getSerializable(KEY_USER) as? User
        userRef = firebaseDatabase.getReference("user/" + user?.id + "/request")
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) = Unit

            override fun onDataChange(p0: DataSnapshot?) {
                val gson = Gson()
                currentRequest = gson.fromJson(gson.toJson(p0?.value), Invite::class.java)
            }

        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_group, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewGroups.layoutManager = LinearLayoutManager(context)
        adapter = GroupListAdapter(groups, object : GroupListAdapter.GroupItemOnclick {
            override fun onJoinGroupClick(group: Group) {
                if (currentRequest != null) {
                    val groupRef = firebaseDatabase.getReference("group/" + currentRequest?.to
                            + "/request/" + user?.id)
                    groupRef.removeValue()
                }
                val invite = Invite(user?.id!!, group.id, group.name, true)
                userRef.setValue(invite)
                firebaseDatabase.getReference("group/" + group.id
                        + "/request/" + user?.id).setValue(invite)
                currentRequest = invite
            }
        })
        recyclerViewGroups.adapter = adapter
        initViewEvent()
    }

    private fun initViewEvent() {
        imgBtnBack.setOnClickListener {
            sendBroadcast(GroupActivity.ACTION_BACK_TO_HOME)
        }
        edtGroupName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    searchGroups(p0.toString())
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        })
    }

    private fun searchGroups(query: String) {
        groups.clear()
        adapter.notifyDataSetChanged()
        HypertrackApi.getApiService().searchGroup(query)
                .enqueue(object : Callback<SearchGroupResult> {
                    override fun onFailure(call: Call<SearchGroupResult>?, t: Throwable?) {
                        showToast(R.string.error_message)
                    }

                    override fun onResponse(call: Call<SearchGroupResult>?,
                                            response: Response<SearchGroupResult>?) {
                        if (query == edtGroupName.text.toString()) {
                            val rs = response?.body()?.results
                            if (rs != null) {
                                groups.clear()
                                groups.addAll(rs)
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }

                })
    }
}

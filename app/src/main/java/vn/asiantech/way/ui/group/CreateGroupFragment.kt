package vn.asiantech.way.ui.group

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hypertrack.lib.models.User
import kotlinx.android.synthetic.main.fragment_create_group.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.asiantech.way.R
import vn.asiantech.way.data.model.group.BodyAddUserToGroup
import vn.asiantech.way.data.model.group.Group
import vn.asiantech.way.data.remote.hypertrackremote.HypertrackRemote
import vn.asiantech.way.ui.base.BaseFragment

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 09/11/2017
 */
class CreateGroupFragment : BaseFragment() {

    companion object {
        const val KEY_USER = "user"

        /**
         * get instance with given user.
         */
        fun getInstance(user: User): CreateGroupFragment {
            val instance = CreateGroupFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_USER, user)
            instance.arguments = bundle
            return instance
        }
    }

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = arguments.getSerializable(KEY_USER) as? User
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_group, container, false)
        onClick(view)
        return view
    }

    private fun onClick(view: View) {
        view.btnCreateGroup.setOnClickListener {
            val groupName = view.edtGroupName.text.toString().trim()
            if (groupName != "") {
                createGroup(groupName)
            }
        }
        view.btnBack.setOnClickListener {
            activity.finish()
        }
    }

    private fun createGroup(name: String) {
        HypertrackRemote.getApiService().createGroup(name)
                .enqueue(object : Callback<Group> {
                    override fun onResponse(call: Call<Group>?, response: Response<Group>?) {
                        val group = response?.body()
                        val userId = user?.id
                        if (group != null && userId != null) {
                            addUserToGroup(group, userId)
                        }
                    }

                    override fun onFailure(call: Call<Group>?, t: Throwable?) {
                        Toast.makeText(context, getString(R.string.error_message),
                                Toast.LENGTH_LONG).show()
                    }

                })
    }

    private fun addUserToGroup(group: Group, userId: String) {
        HypertrackRemote.getApiService().addUserToGroup(userId, BodyAddUserToGroup(group.id))
                .enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>?, response: Response<User>?) {
                        val user = response?.body()
                        if (user != null) {
                            val intent = Intent(GroupActivity.ACTION_GROUP_CREATED)
                            val bundle = Bundle()
                            bundle.putSerializable(GroupInfoFragment.KEY_GROUP, group)
                            intent.putExtras(bundle)
                            activity.sendBroadcast(intent)
                        }
                    }

                    override fun onFailure(call: Call<User>?, t: Throwable?) {
                        Toast.makeText(context, getString(R.string.error_message),
                                Toast.LENGTH_LONG).show()
                    }
                })
    }
}

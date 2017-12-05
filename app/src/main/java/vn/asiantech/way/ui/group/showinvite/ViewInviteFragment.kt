package vn.asiantech.way.ui.group.showinvite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoContext
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.ui.base.BaseFragment

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 05/12/2017.
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

    private lateinit var ui: ViewInviteFragmentUI
    private val invites = mutableListOf<Invite>()
    internal lateinit var adapter: InviteListAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        ui = ViewInviteFragmentUI()
        return ui.createView(AnkoContext.create(context, this))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = InviteListAdapter(context, invites, object : InviteListAdapter.OnItemClick {
            override fun onOkClick(invite: Invite) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onCancelClick(invite: Invite) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
        ui.recyclerView.adapter = adapter
    }

    override fun onBindViewModel() {
    }
}

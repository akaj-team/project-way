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
    private lateinit var ui: ViewInviteFragmentUI
    private val invites = mutableListOf<Invite>()
    private lateinit var adapter: InviteListAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        adapter = InviteListAdapter(context, invites, object : InviteListAdapter.OnItemClick {
            override fun onOkClick(invite: Invite) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onCancelClick(invite: Invite) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
        ui = ViewInviteFragmentUI(adapter)
        return ui.createView(AnkoContext.create(context, this))
    }

    override fun onBindViewModel() {
    }
}

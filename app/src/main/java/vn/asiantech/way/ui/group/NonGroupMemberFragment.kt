package vn.asiantech.way.ui.group

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_non_member.*
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseFragment

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 11/11/2017
 */
class NonGroupMemberFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_non_member, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOnclick()
    }

    private fun initOnclick() {
        btnCreateGroup.setOnClickListener {
            activity.sendBroadcast(Intent(GroupActivity.ACTION_CALL_CREATE_GROUP_FRAGMENT))
        }
        btnViewInvites.setOnClickListener {
            activity.sendBroadcast(Intent(GroupActivity.ACTION_VIEW_INVITES))
        }
        btnSearchGroup.setOnClickListener {
            activity.sendBroadcast(Intent(GroupActivity.ACTION_SEARCH_GROUP))
        }
        btnBack.setOnClickListener {
            activity.sendBroadcast(Intent(GroupActivity.ACTION_BACK))
        }
    }
}

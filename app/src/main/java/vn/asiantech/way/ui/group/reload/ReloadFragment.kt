package vn.asiantech.way.ui.group.reload

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_reload.*
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseFragment
import vn.asiantech.way.ui.group.GroupActivity

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 11/11/2017
 */
class ReloadFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reload, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOnclick()
    }

    private fun initOnclick() {
        imgReload.setOnClickListener {
            activity.sendBroadcast(Intent(GroupActivity.ACTION_RELOAD))
        }
    }
}

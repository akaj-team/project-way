package vn.asiantech.way.ui.group

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_reload.view.*
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseFragment

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 11/11/2017
 */
class ReloadFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_reload, container, false)
        onClick(view)
        return view
    }

    private fun onClick(view: View) {
        view.imgReload.setOnClickListener {
            activity.sendBroadcast(Intent(GroupActivity.ACTION_RELOAD))
        }
    }
}

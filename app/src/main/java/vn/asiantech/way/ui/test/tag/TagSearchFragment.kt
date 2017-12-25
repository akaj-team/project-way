package vn.asiantech.way.ui.test.tag

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoContext

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 21/12/2017
 */
class TagSearchFragment : Fragment() {

    private val viewModel = TagSearchFragmentViewModel()
    private lateinit var ui: TagSearchFragmentUI

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ui = TagSearchFragmentUI(viewModel.tagSearches)
        return ui.createView(AnkoContext.create(context, this))
    }
}
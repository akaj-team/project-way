package vn.asiantech.way.ui.test.recent

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
class RecentSearchFragment:Fragment() {

    private lateinit var ui: RecentSearchFragmentUI
    private val viewModel = RecentSearchFragmentViewModel()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ui = RecentSearchFragmentUI(viewModel.recentSearches)
        return ui.createView(AnkoContext.create(context, this))
    }
}

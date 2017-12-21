package vn.asiantech.way.ui.test

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 21/12/2017
 */
class RecentSearchFragmentUI(val recent: MutableList<Any>) : AnkoComponent<RecentSearchFragment> {
    private lateinit var recyclerViewSearchItem: RecyclerView
    private val recentSearchAdapter: RecentSearchAdapter = RecentSearchAdapter(recent)

    override fun createView(ui: AnkoContext<RecentSearchFragment>) = with(ui) {
        relativeLayout {
            lparams(matchParent, matchParent)
            padding = dimen(R.dimen.text_search_padding)

            recyclerViewSearchItem = recyclerView {
                layoutManager = LinearLayoutManager(ctx)
                adapter = recentSearchAdapter
            }
        }
    }
}
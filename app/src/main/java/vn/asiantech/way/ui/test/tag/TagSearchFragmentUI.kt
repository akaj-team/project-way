package vn.asiantech.way.ui.test.tag

import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import vn.asiantech.way.R
import vn.asiantech.way.ui.test.recent.RecentSearchAdapter

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 21/12/2017
 */
class TagSearchFragmentUI(tagSearches: MutableList<Any>) : AnkoComponent<TagSearchFragment> {

    internal val tagSearchAdapter = RecentSearchAdapter(tagSearches, {
        Log.i("tag11", it.toString())
    })

    override fun createView(ui: AnkoContext<TagSearchFragment>) = with(ui) {
        relativeLayout {
            lparams(matchParent, matchParent)
            leftPadding = dimen(R.dimen.text_search_padding)

            recyclerView {
                layoutManager = LinearLayoutManager(ctx)
                adapter = tagSearchAdapter
            }.lparams(matchParent, matchParent)
        }
    }

}
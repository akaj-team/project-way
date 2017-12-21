package vn.asiantech.way.ui.test

import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.*
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 19/12/2017
 */
class TagSearchItemUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        val view = with(ui) {
            verticalLayout {
                lparams(matchParent, wrapContent)

                linearLayout {
                    textView {
                        id = R.id.text_search_tag_item_tv_name
                    }.lparams(0, wrapContent) {
                        weight = 1f
                    }
                }

                linearLayout {
                    textView {
                        id = R.id.text_search_tag_item_tv_post_count
                    }.lparams {
                        leftMargin = dimen(R.dimen.text_search_padding)
                    }
                }
            }
        }
        view.tag = RecentSearchAdapter.TagSearchItemViewHolder(view)
        return view
    }
}

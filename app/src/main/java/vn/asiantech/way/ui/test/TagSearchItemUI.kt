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
                leftPadding = dimen(R.dimen.text_search_padding)

                linearLayout {
                    verticalPadding = dimen(R.dimen.text_search_item_vertical_padding)

                    textView {
                        id = R.id.text_search_tag_item_tv_name
                        textColorResource = R.color.colorBlack
                        textSize = 14f
                    }.lparams(0, wrapContent) {
                        weight = 1f
                    }

                    textView {
                        id = R.id.text_search_tag_item_tv_post_count
                        textColorResource = R.color.colorGray
                        textSize = 14f
                    }.lparams {
                        leftMargin = dimen(R.dimen.text_search_padding)
                    }
                }

                view {
                    backgroundResource = R.color.colorGray
                }.lparams(matchParent, dimen(R.dimen.text_search_item_break_line))
            }
        }
        view.tag = RecentSearchAdapter.TagSearchItemViewHolder(view)
        return view
    }
}

package vn.asiantech.way.ui.test

import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.*
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 19/12/2017
 */
class HeaderSearchItemUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        val textView = with(ui) {
            linearLayout {
                textView {
                    id = R.id.text_search_recent_iten_header_name
                    textSize = 14f
                    textColorResource = R.color.colorBlack
                    typeface = Typeface.DEFAULT_BOLD
                    verticalPadding = dimen(R.dimen.text_search_padding)
                }
            }
        }
        textView.tag = RecentSearchAdapter.HeaderViewHolder(textView)
        return textView
    }
}

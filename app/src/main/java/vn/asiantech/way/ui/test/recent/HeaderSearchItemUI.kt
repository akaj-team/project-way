package vn.asiantech.way.ui.test.recent

import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.*
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 19/12/2017
 */
class HeaderSearchItemUI : AnkoComponent<ViewGroup> {
    internal lateinit var tvHeaderName: TextView
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        relativeLayout {
            tvHeaderName = textView {
                id = R.id.text_search_recent_item_header_name
                textSize = 14f
                textColorResource = R.color.colorBlack
                typeface = Typeface.DEFAULT_BOLD
                verticalPadding = dimen(R.dimen.text_search_padding)
            }
        }
    }
}

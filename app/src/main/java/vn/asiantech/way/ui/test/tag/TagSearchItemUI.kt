package vn.asiantech.way.ui.test.tag

import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.*
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 19/12/2017
 */
class TagSearchItemUI : AnkoComponent<ViewGroup> {

    internal lateinit var tvTagName: TextView
    internal lateinit var tvPostCount: TextView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        verticalLayout {
            lparams(matchParent, wrapContent)
            leftPadding = dimen(R.dimen.text_search_padding)

            linearLayout {
                verticalPadding = dimen(R.dimen.text_search_item_vertical_padding)

                tvTagName = textView {
                    textColorResource = R.color.colorBlack
                    textSize = 14f
                    typeface = Typeface.DEFAULT_BOLD
                }.lparams(0, wrapContent) {
                    weight = 1f
                }

                tvPostCount = textView {
                    textColorResource = R.color.colorGrayLight
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
}

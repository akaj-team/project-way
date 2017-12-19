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
class PopularSearchItemUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        val view = with(ui) {
            linearLayout {
                lparams(matchParent, wrapContent)

                imageView {
                    id = R.id.text_search_recent_item_img_icon
                    backgroundColorResource = R.color.colorAccentLight
                }.lparams(dimen(R.dimen.text_search_item_icon_width), dimen(R.dimen.text_search_item_icon_height))

                verticalLayout {

                    view().lparams(matchParent, 0) {
                        weight = 1f
                    }

                    textView {
                        id = R.id.text_search_recent_item_tv_name
                        textSize = 14f
                        textColorResource = R.color.colorBlack
                        typeface = Typeface.DEFAULT_BOLD
                    }

                    view().lparams(matchParent, 0) {
                        weight = 1f
                    }

                    view {
                        backgroundResource = R.color.colorGray
                    }.lparams(matchParent, dimen(R.dimen.text_search_item_break_line))
                }.lparams(matchParent, matchParent) {
                    leftMargin = dimen(R.dimen.text_search_padding)
                }
            }
        }
        return view
    }
}

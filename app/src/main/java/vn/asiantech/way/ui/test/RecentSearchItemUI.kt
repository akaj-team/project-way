package vn.asiantech.way.ui.test

import android.view.View
import org.jetbrains.anko.*
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 19/12/2017
 */
class RecentSearchItemUI : AnkoComponent<TestActivity> {
    override fun createView(ui: AnkoContext<TestActivity>): View {
        val view = with(ui) {
            linearLayout {
                lparams(matchParent, 96)

                imageView {
                    id = R.id.text_search_recent_item_img_icon
                }.lparams(72,96)

                verticalLayout {

                    textView(R.string.coming_soon).lparams {
                        leftMargin = dimen(R.dimen.text_search_padding)
                    }

                    textView(R.string.coming_soon).lparams {
                        leftMargin = 10
                    }

                }.lparams(matchParent, matchParent){
                    leftMargin = dimen(R.dimen.text_search_padding)
                }
            }
        }

        return view
    }
}
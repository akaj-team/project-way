package vn.asiantech.way.ui.group.search

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import vn.asiantech.way.R

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 04/12/2017.
 */
class SearchGroupFragmentUI : AnkoComponent<SearchGroupFragment> {
    override fun createView(ui: AnkoContext<SearchGroupFragment>): View = with(ui) {

        verticalLayout {
            lparams(matchParent, matchParent) {
                padding = dip(10)
            }
            linearLayout {
                lparams(matchParent, dip(100))
                gravity = Gravity.CENTER_VERTICAL

                imageButton {
                    backgroundColor = Color.TRANSPARENT
                    backgroundResource = R.drawable.ic_back_icon_button
                }

                editText {
                    textColor = Color.BLACK
                    backgroundColor = Color.WHITE
                    textSize = px2dip(dimen(R.dimen.search_screen_text_size))
                    hint = resources.getString(R.string.enter_group_name)
                }.lparams(matchParent, matchParent) {
                    leftMargin = dip(10)
                }
            }

            recyclerView {
                layoutManager = LinearLayoutManager(context)
            }.lparams(matchParent, matchParent) {
                topMargin = dip(20)
            }
        }
    }
}

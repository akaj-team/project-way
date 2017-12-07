package vn.asiantech.way.ui.group.search

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.R
import vn.asiantech.way.extension.afterTextChanged

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 04/12/2017.
 */
class SearchGroupFragmentUI(private val SearchGroupAdapter: GroupListAdapter) : AnkoComponent<SearchGroupFragment> {

    override fun createView(ui: AnkoContext<SearchGroupFragment>): View = with(ui) {

        verticalLayout {
            lparams(matchParent, matchParent) {
                padding = dimen(R.dimen.search_group_padding)
            }
            linearLayout {
                lparams(matchParent, dip(dimen(R.dimen.search_group_height_ll)))
                gravity = Gravity.CENTER_VERTICAL

                imageButton {
                    backgroundColor = Color.TRANSPARENT
                    backgroundResource = R.drawable.ic_back_icon_button
                    onClick {
                        owner.onBackClick()
                    }
                }

                editText {
                    textColor = Color.BLACK
                    backgroundColor = Color.WHITE
                    textSize = px2dip(dimen(R.dimen.search_screen_text_size))
                    hint = resources.getString(R.string.enter_group_name)

                    afterTextChanged {
                        if (it.toString().trim().isNotEmpty()) {
                            owner.eventOnTextChangedSearchGroup(it.toString().trim())
                        }
                    }

                }.lparams(matchParent, matchParent) {
                    leftMargin = dimen(R.dimen.search_group_padding)
                }
            }

            recyclerView {
                layoutManager = LinearLayoutManager(context)
                adapter = SearchGroupAdapter
            }.lparams(matchParent, matchParent) {
                topMargin = dimen(R.dimen.search_group_top_margin)
            }
        }
    }
}

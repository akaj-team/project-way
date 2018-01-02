package vn.asiantech.way.ui.group.search

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.extension.onTextChangeListener

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 04/12/2017.
 */
class SearchGroupFragmentUI(groups: MutableList<Group>, context: Context) : AnkoComponent<SearchGroupFragment> {
    internal val searchGroupAdapter = GroupListAdapter(context, groups)

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
                        owner.eventOnBackClick()
                    }
                }

                editText {
                    textColor = ContextCompat.getColor(context, R.color.colorBlack)
                    backgroundResource = R.color.colorWhite
                    textSize = px2dip(dimen(R.dimen.search_screen_text_size))
                    hint = resources.getString(R.string.enter_group_name)

                    onTextChangeListener({}, {
                        if (it.toString().trim().isNotEmpty()) {
                            owner.eventOnTextChangedSearchGroup(it.toString().trim())
                        }
                    }, {})

                }.lparams(matchParent, matchParent) {
                    leftMargin = dimen(R.dimen.search_group_padding)
                }
            }

            recyclerView {
                layoutManager = LinearLayoutManager(context)
                adapter = searchGroupAdapter
            }.lparams(matchParent, matchParent) {
                topMargin = dimen(R.dimen.search_group_top_margin)
            }
        }
    }
}

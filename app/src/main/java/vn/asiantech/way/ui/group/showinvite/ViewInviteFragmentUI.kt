package vn.asiantech.way.ui.group.showinvite

import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import vn.asiantech.way.R

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 05/12/2017.
 */
class ViewInviteFragmentUI : AnkoComponent<ViewInviteFragment> {
    internal lateinit var recyclerView: RecyclerView
    override fun createView(ui: AnkoContext<ViewInviteFragment>) = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)

            textView {
                padding = 10
                backgroundResource = R.color.colorGrayLight
                textColor = Color.BLACK
                gravity = Gravity.CENTER_VERTICAL
                text = resources.getString(R.string.invite_list)
                textSize = px2dip(dimen(R.dimen.text_size_normal))
                typeface = Typeface.DEFAULT_BOLD
            }.lparams(matchParent, dimen(R.dimen.toolbar_height))

            recyclerView = recyclerView {
                layoutManager = LinearLayoutManager(context)
            }.lparams(matchParent, matchParent) {
                margin = dip(5)
            }
        }
    }
}

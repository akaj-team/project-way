package vn.asiantech.way.ui.group.showinvite

import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import vn.asiantech.way.R

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 05/12/2017.
 */
class ViewInviteFragmentUI(private val inviteListAdapter: InviteListAdapter) : AnkoComponent<ViewInviteFragment> {
    override fun createView(ui: AnkoContext<ViewInviteFragment>) = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)

            textView(R.string.invite_list) {
                padding = dimen(R.dimen.group_invite_fragment_padding_title)
                backgroundResource = R.color.colorGrayLight
                textColor = Color.BLACK
                gravity = Gravity.CENTER_VERTICAL
                textSize = px2dip(dimen(R.dimen.text_size_normal))
                typeface = Typeface.DEFAULT_BOLD
            }.lparams(matchParent, dimen(R.dimen.toolbar_height))

            recyclerView {
                layoutManager = LinearLayoutManager(context)
                adapter = inviteListAdapter
            }.lparams(matchParent, matchParent) {
                margin = dimen(R.dimen.group_invite_fragment_margin_recyclerview)
            }
        }
    }
}

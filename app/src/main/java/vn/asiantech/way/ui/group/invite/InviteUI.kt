package vn.asiantech.way.ui.group.invite

import android.graphics.Color
import android.support.v4.app.ActivityCompat
import android.view.Gravity
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import vn.asiantech.way.R

/**
 * Invite UI
 * @author NgocTTN
 */

class InviteUI() : AnkoComponent<InviteActivity> {

    override fun createView(ui: AnkoContext<InviteActivity>): View = ui.apply {
        verticalLayout {
            lparams(matchParent, wrapContent)
            padding = dip(value = 10)
            backgroundColor = ActivityCompat.getColor(context, R.color.colorSearchScreenBackground)

            verticalLayout {
                lparams(matchParent, wrapContent)
                gravity = Gravity.CENTER_VERTICAL

                imageButton(R.drawable.ic_back_icon_button) {
                    backgroundColor = Color.TRANSPARENT
                    contentDescription = null
                }.lparams(wrapContent, wrapContent)

                editText {
                    backgroundColor = Color.BLACK
                    hint = resources.getString(R.string.enter_user_name)
                    singleLine = true
                    padding = dip(value = 10)
                    textSize = resources.getDimension(R.dimen.search_screen_text_size)
                }.lparams(matchParent, matchParent) {
                    leftMargin = dip(value = 10)
                    rightMargin = dip(value = 10)
                }

                // RecycleView
                recyclerView {
                    id = R.id.invite_recycle_view
                }.lparams(matchParent, matchParent) {
                    topMargin = dip(value = 20)
                }
            }
        }

    }.view
}

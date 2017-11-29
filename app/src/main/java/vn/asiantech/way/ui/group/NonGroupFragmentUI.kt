package vn.asiantech.way.ui.group

import android.graphics.Color
import android.support.v4.app.ActivityCompat
import android.view.Gravity
import android.view.ViewManager
import android.widget.Button
import org.jetbrains.anko.*
import vn.asiantech.way.R

/**
 *
 * Created by haingoq on 29/11/2017.
 */
class NonGroupFragmentUI : AnkoComponent<NonGroupFragment> {
    internal lateinit var btnCreateGroup: Button
    internal lateinit var btnViewInvite: Button
    internal lateinit var btnBack: Button

    override fun createView(ui: AnkoContext<NonGroupFragment>) = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)
            gravity = Gravity.CENTER
            padding = dimen(R.dimen.group_screen_group_name_padding)

            textView(R.string.not_yet_join_to_group) {
                textColor = Color.BLACK
                textSize = px2dip(dimen(R.dimen.notification_size))
            }.lparams {
                gravity = Gravity.CENTER
            }

            btnCreateGroup = buttonNonGroup(R.string.create_new_group, R.color.blue)
                    .lparams(matchParent, wrapContent) {
                        topMargin = dimen((R.dimen.group_screen_group_name_padding))
                    }

            btnViewInvite = buttonNonGroup(R.string.view_invites, R.color.colorPinkLight)
                    .lparams(matchParent, wrapContent) {
                        topMargin = dimen((R.dimen.group_screen_group_name_padding))
                    }

            btnBack = buttonNonGroup(R.string.back, android.R.color.darker_gray)
                    .lparams(matchParent, wrapContent) {
                        topMargin = dimen((R.dimen.group_screen_group_name_padding))
                    }
        }
    }

    private fun ViewManager.buttonNonGroup(strResource: Int, color: Int) = button {
        backgroundColor = ActivityCompat.getColor(context, color)
        text = resources.getString(strResource)
        setAllCaps(false)
        textColor = Color.WHITE
        textSize = px2dip(dimen(R.dimen.group_text_size_normal))
    }
}

package vn.asiantech.way.ui.group

import android.graphics.Color
import android.view.Gravity
import android.widget.ImageView
import org.jetbrains.anko.*
import vn.asiantech.way.R

/**
 *
 * Created by haingoq on 29/11/2017.
 */
class ReloadFragmentUI : AnkoComponent<ReloadFragment> {
    internal lateinit var imgReload: ImageView

    override fun createView(ui: AnkoContext<ReloadFragment>) = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)
            gravity = Gravity.CENTER

            textView(R.string.touch_to_reload) {
                textColor = Color.BLACK
                textSize = px2dip(dimen(R.dimen.notification_size))
            }.lparams {
                gravity = Gravity.CENTER
            }

            imgReload = imageView {
                backgroundResource = R.drawable.ic_replay_indigo_300_48dp
            }.lparams {
                topMargin = dimen((R.dimen.group_screen_group_name_padding))
            }
        }
    }
}

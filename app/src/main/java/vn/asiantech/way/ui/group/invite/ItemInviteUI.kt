package vn.asiantech.way.ui.group.invite

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.*
import vn.asiantech.way.R

/**
 * Item Invite UI
 * @author NgocTTN
 */
class ItemInviteUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View = ui.apply {
        relativeLayout {
            lparams(matchParent, matchParent)
            backgroundColor = Color.WHITE
            // Text view name
            val tvName = textView {
                id = R.id.item_invite_name
                singleLine = true
                textColor = Color.BLACK
                textSize = resources.getDimension(R.dimen.text_size_normal)
                typeface = Typeface.DEFAULT_BOLD

            }.lparams(wrapContent, wrapContent) {
                margin = dip(value = 10)
                leftMargin = dip(value = 20)
                rightMargin = dip(value = 20)
            }

            // Text view ok
            textView {
                id = R.id.item_invite_tv_ok
                backgroundColor = Color.BLUE
                gravity = Gravity.CENTER
                text = resources.getString(R.string.invite_text_ok)
                textSize = resources.getDimension(R.dimen.text_size_normal)
                typeface = Typeface.DEFAULT_BOLD

            }.lparams(dip(value = 80), wrapContent) {
                below(R.id.item_invite_name)
                leftMargin = dip(value = 10)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    marginStart = dip(value = 10)
                }
            }

            // Text view cancel
            textView {
                id = R.id.item_invite_tv_cancel
                backgroundColor = ActivityCompat.getColor(context, R.color.grayLight)
                gravity = Gravity.CENTER
                text = resources.getString(R.string.invite_text_cancel)
                textSize = resources.getDimension(R.dimen.text_size_little)
                typeface = Typeface.DEFAULT_BOLD
                padding = dip(value = 5)

            }.lparams(dip(value = 80), wrapContent) {
                below(R.id.item_invite_name)
                rightOf(R.id.item_invite_tv_ok)
                leftMargin = dip(value = 30)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    marginStart = dip(value = 30)
                }
            }

            // View mask
            view {
                backgroundColor = ActivityCompat.getColor(context, R.color.grayLight)
            }.lparams(matchParent, dip(value = 0.5f)) {
                below(R.id.item_invite_tv_ok)
                leftMargin = dip(value = 10)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    marginStart = dip(value = 10)
                }
                topMargin = dip(value = 5)
            }
        }

    }.view
}

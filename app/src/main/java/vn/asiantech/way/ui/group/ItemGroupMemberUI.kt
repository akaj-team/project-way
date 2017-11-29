package vn.asiantech.way.ui.group

import android.graphics.Color
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.ImageView
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import vn.asiantech.way.R

/**
 *
 * Created by haingoq on 29/11/2017.
 */
class ItemGroupMemberUI : AnkoComponent<ViewGroup> {
    companion object {
        private const val WEIGHT = 1f
    }

    internal lateinit var imgAvatar: CircleImageView
    internal lateinit var tvName: TextView
    internal lateinit var imgCall: ImageView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        linearLayout {
            lparams(matchParent, wrapContent)
            val paddingLeft = dimen(R.dimen.group_screen_tv_count_padding_left)
            val paddingTop = dimen(R.dimen.group_screen_recycler_view_padding)
            bottomPadding = paddingTop
            rightPadding = paddingLeft
            leftPadding = paddingLeft
            topPadding = paddingTop
            backgroundColor = Color.WHITE

            imgAvatar = circleImageView {
                backgroundResource = R.mipmap.ic_launcher_round
            }.lparams(dimen(R.dimen.group_screen_avatar_width),
                    dimen(R.dimen.group_screen_avatar_width))

            tvName = textView {
                textColor = Color.BLACK
                textSize = px2dip(dimen(R.dimen.text_size_normal))
            }.lparams(dimen(R.dimen.group_screen_tv_name_width), wrapContent) {
                leftMargin = dimen(R.dimen.group_text_size_normal)
                weight = WEIGHT
            }

            imgCall = imageView(R.drawable.ic_phone_forwarded_blue_a700_48dp)
                    .lparams(dimen(R.dimen.group_screen_avatar_width),
                            dimen(R.dimen.group_screen_avatar_width))
        }
    }

    /*
     * Add circleImageView library
     */
    private inline fun ViewManager.circleImageView(theme: Int = 0, init: CircleImageView.() -> Unit):
            CircleImageView {
        return ankoView({ CircleImageView(it) }, theme, init)
    }
}

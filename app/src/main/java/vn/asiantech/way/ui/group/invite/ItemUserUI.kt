package vn.asiantech.way.ui.group.invite

import android.graphics.Color
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import vn.asiantech.way.R

/**
 * Item User UI
 * @author NgocTTN
 */
class ItemUserUI<T> : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View = ui.apply {
        linearLayout {
            // layout parent
            lparams(matchParent, wrapContent)
            backgroundColor = Color.WHITE
            gravity = Gravity.CENTER_VERTICAL
            val paddingTopOrBottom = dimen(R.dimen.intvite_screen_bottom_or_top_padding)
            bottomPadding = paddingTopOrBottom
            topPadding = paddingTopOrBottom

            // Circle Image
            circleImage {
                id = R.id.item_user_img_avatar
                lparams(dip(48), dip(48))
            }

            // Text view name
            textView {
                id = R.id.item_user_tv_name
                textColor = Color.BLACK
                textSize = resources.getDimension(R.dimen.text_size_normal)
            }.lparams(dip(0), wrapContent) {
                leftMargin = dip(15)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    marginStart = dip(15)
                }
                weight = 1f
            }

            // Text view invite
            textView {
                id = R.id.item_user_tv_invite
                backgroundColor = ActivityCompat.getColor(context!!, R.color.colorPrimaryDark)
                maxLines = 1
                topPadding = dip(5)
                bottomPadding = dip(5)
                rightPadding = dip(10)
                leftPadding = dip(10)
                text = resources.getText(R.string.item_user_invite)
                textColor = ContextCompat.getColor(context!!, R.color.colorWhite)
            }.lparams(dip(wrapContent), dip(wrapContent)) {
                leftMargin = dip(10)
                rightMargin = dip(0)
            }
        }
    }.view

    private inline fun ViewManager.circleImage(init: CircleImageView.() -> Unit):
            CircleImageView = ankoView({ CircleImageView(it) }, 0, init)

}

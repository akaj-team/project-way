package vn.asiantech.way.ui.group.invite

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
class InviteItemUserUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View = ui.apply {

        val sizeImgAvatar = dimen(R.dimen.item_user_img_avatar_width_or_height)
        val leftRightMarginOrPadding = dimen(R.dimen.invite_screen_padding)

        linearLayout {

            // Layout parent
            lparams(matchParent, wrapContent) {
                backgroundResource = R.color.colorWhite
                verticalPadding = leftRightMarginOrPadding
            }

            // Circle Image
            circleImage {
                id = R.id.item_user_img_avatar
            }.lparams(sizeImgAvatar, sizeImgAvatar) {
                horizontalMargin = leftRightMarginOrPadding
            }

            // Text view name
            textView {
                id = R.id.item_user_tv_name
                textColor = ActivityCompat.getColor(context, R.color.colorBlack)
                textSize = px2dip(dimen(R.dimen.invite_screen_tv_user_name_text_size))
                gravity = Gravity.CENTER_VERTICAL
                verticalPadding = leftRightMarginOrPadding
            }.lparams(dip(0), wrapContent) {
                weight = 1f
            }

            // Text view invite
            textView(R.string.item_user_invite) {
                id = R.id.item_user_tv_invite
                backgroundResource = R.color.colorPrimaryDark
                maxLines = 1
                textColor = ContextCompat.getColor(context, R.color.colorWhite)
                verticalPadding = dimen(R.dimen.item_user_bottom_or_top_padding)
                horizontalPadding = dimen(R.dimen.item_user_left_or_right_padding)
            }.lparams(dip(wrapContent), dip(wrapContent)) {
                horizontalMargin = leftRightMarginOrPadding
            }
        }
    }.view

    private inline fun ViewManager.circleImage(init: CircleImageView.() -> Unit):
            CircleImageView = ankoView({ CircleImageView(it) }, 0, init)

}

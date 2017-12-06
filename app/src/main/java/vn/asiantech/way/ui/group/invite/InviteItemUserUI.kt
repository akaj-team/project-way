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
class InviteItemUserUI<T> : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View = ui.apply {
        linearLayout {
            // layout parent
            lparams(matchParent, wrapContent)
            backgroundColor = ActivityCompat.getColor(context!!, R.color.colorWhite)
            gravity = Gravity.CENTER_VERTICAL
            val paddingTopOrBottom = dimen(R.dimen.invite_screen_padding)
            bottomPadding = paddingTopOrBottom
            topPadding = paddingTopOrBottom

            // Circle Image
            val sizeImgAvatar = dimen(R.dimen.item_user_img_avatar_width_or_height)
            val leftRightMargin = dimen(R.dimen.invite_screen_padding)
            circleImage {
                id = R.id.item_user_img_avatar
            }.lparams(sizeImgAvatar, sizeImgAvatar) {
                leftMargin = leftRightMargin
                rightMargin = leftRightMargin
            }

            // Text view name
            textView {
                id = R.id.item_user_tv_name
                textColor = ActivityCompat.getColor(context!!, R.color.colorBlack)
                textSize = 20f
            }.lparams(dip(0), wrapContent) {
                leftMargin = dimen(R.dimen.item_user_left_right_margin)
                weight = 1f
            }

            // Text view invite
            textView {
                id = R.id.item_user_tv_invite
                backgroundColor = ActivityCompat.getColor(context!!, R.color.colorPrimaryDark)
                maxLines = 1
                topPadding = dimen(R.dimen.item_user_bottom_or_top_padding)
                bottomPadding = dimen(R.dimen.item_user_bottom_or_top_padding)
                rightPadding = dimen(R.dimen.item_user_left_or_right_padding)
                leftPadding = dimen(R.dimen.item_user_left_or_right_padding)
                text = resources.getText(R.string.item_user_invite)
                textColor = ContextCompat.getColor(context!!, R.color.colorWhite)
            }.lparams(dip(wrapContent), dip(wrapContent)) {
                leftMargin = dimen(R.dimen.invite_screen_padding)
                rightMargin = dimen(R.dimen.invite_screen_padding)
            }
        }
    }.view

    private inline fun ViewManager.circleImage(init: CircleImageView.() -> Unit):
            CircleImageView = ankoView({ CircleImageView(it) }, 0, init)

}

package vn.asiantech.way.ui.custom

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import org.jetbrains.anko.*
import vn.asiantech.way.R

/**
 * FloatingButtonHorizontalUI.
 *
 * @author at-ToanNguyen
 */
class FloatingButtonHorizontalUI : AnkoComponent<ViewGroup> {
    companion object {
        private const val ID_BTN_SEARCH = 1001
        private const val ID_BTN_GROUP = 1002
        private const val ID_BTN_CALENDAR = 1003
        private const val ID_BTN_PROFILE = 1004
        private const val ID_BTN_SHARE = 1005
    }

    internal lateinit var rlSearch: RelativeLayout
    internal lateinit var imgBtnSearch: ImageButton
    internal lateinit var tvSearchTitle: TextView
    internal lateinit var rlGroup: RelativeLayout
    internal lateinit var imgBtnGroup: ImageButton
    internal lateinit var tvGroupTitle: TextView
    internal lateinit var rlCalendar: RelativeLayout
    internal lateinit var imgBtnCalendar: ImageButton
    internal lateinit var tvCalendarTitle: TextView
    internal lateinit var rlProfile: RelativeLayout
    internal lateinit var imgBtnProfile: ImageButton
    internal lateinit var tvProfileTitle: TextView
    internal lateinit var rlShare: RelativeLayout
    internal lateinit var imgBtnShare: ImageButton
    internal lateinit var tvShareTitle: TextView
    internal lateinit var imgBtnMenu: ImageButton
    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        verticalLayout {
            lparams(wrapContent, wrapContent)
            relativeLayout {
                imageButton {
                    id = ID_BTN_SEARCH
                    imageResource = R.drawable.ic_search
                    backgroundResource = R.drawable.custom_bg_item_search_button
                }.lparams(dip(50), dip(50)) {
                    alignParentRight()
                    rightMargin = dip(5)
                }
                textView(R.string.custom_floating_menu_search_title) {
                    leftPadding = dip(10)
                    rightPadding = dip(10)
                    textSize = px2dip(dimen(R.dimen.custom_menu_text_size))
                    backgroundResource = R.drawable.custom_bg_item_menu_title
                }.lparams(wrapContent, dip(30)) {
                    centerVertically()
                    rightMargin = 10
                    leftOf(ID_BTN_SEARCH)
                    gravity = Gravity.CENTER
                }
            }.lparams(dip(150), wrapContent) {
                visibility = View.VISIBLE
            }
            relativeLayout {
                imageButton {
                    id = ID_BTN_GROUP
                    imageResource = R.drawable.ic_group_white_24dp
                    backgroundResource = R.drawable.custom_bg_item_search_button
                }.lparams(dip(50), dip(50)) {
                    alignParentRight()
                    rightMargin = dip(5)
                }
                textView(R.string.custom_floating_menu_group_title) {
                    leftPadding = dip(10)
                    rightPadding = dip(10)
                    textSize = px2dip(dimen(R.dimen.custom_menu_text_size))
                    backgroundResource = R.drawable.custom_bg_item_menu_title
                }.lparams(wrapContent, dip(30)) {
                    centerVertically()
                    rightMargin = 10
                    leftOf(ID_BTN_GROUP)
                    gravity = Gravity.CENTER
                }
            }.lparams(dip(150), wrapContent) {
                visibility = View.VISIBLE
            }
            relativeLayout {
                imageButton {
                    id = ID_BTN_CALENDAR
                    backgroundResource = R.drawable.custom_bg_item_calendar_button
                    imageResource = R.drawable.ic_calendar
                }.lparams(dip(50), dip(50)) {
                    alignParentRight()
                    rightMargin = dip(5)
                }
                textView(R.string.custom_floating_menu_calendar_title) {
                    leftPadding = dip(10)
                    rightPadding = dip(10)
                    textSize = px2dip(dimen(R.dimen.custom_menu_text_size))
                    backgroundResource = R.drawable.custom_bg_item_menu_title
                }.lparams(wrapContent, dip(30)) {
                    centerVertically()
                    rightMargin = 10
                    leftOf(ID_BTN_CALENDAR)
                    gravity = Gravity.CENTER
                }
            }.lparams(dip(150), wrapContent) {
                visibility = View.VISIBLE
            }
            relativeLayout {
                imageButton {
                    id = ID_BTN_PROFILE
                    imageResource = R.drawable.ic_profile
                    backgroundResource = R.drawable.custom_bg_item_profile_button
                }.lparams(dip(50), dip(50)) {
                    alignParentRight()
                    rightMargin = dip(5)
                }
                textView(R.string.custom_floating_menu_search_title) {
                    leftPadding = dip(10)
                    rightPadding = dip(10)
                    textSize = px2dip(dimen(R.dimen.custom_menu_text_size))
                    backgroundResource = R.drawable.custom_bg_item_menu_title
                }.lparams(wrapContent, dip(30)) {
                    centerVertically()
                    rightMargin = 10
                    leftOf(ID_BTN_PROFILE)
                    gravity = Gravity.CENTER
                }
            }.lparams(dip(150), wrapContent) {
                visibility = View.VISIBLE
            }
            relativeLayout {
                imageButton {
                    id = ID_BTN_SHARE
                    imageResource = R.drawable.ic_share
                    backgroundResource = R.drawable.custom_bg_item_share_button
                }.lparams(dip(50), dip(50)) {
                    alignParentRight()
                    rightMargin = dip(5)
                }
                textView(R.string.custom_floating_menu_search_title) {
                    leftPadding = dip(10)
                    rightPadding = dip(10)
                    textSize = px2dip(dimen(R.dimen.custom_menu_text_size))
                    backgroundResource = R.drawable.custom_bg_item_menu_title
                }.lparams(wrapContent, dip(30)) {
                    centerVertically()
                    rightMargin = 10
                    leftOf(ID_BTN_SHARE)
                    gravity = Gravity.CENTER
                }
            }.lparams(dip(150), wrapContent) {
                visibility = View.VISIBLE
            }
            imageButton {
                imageResource = R.drawable.ic_menu
                backgroundResource = R.drawable.custom_menu_button
            }.lparams(dip(60), dip(60)) {
                gravity = Gravity.END
                bottomMargin = dip(10)
                topMargin = dip(10)
                padding = 10
            }

        }
    }
}

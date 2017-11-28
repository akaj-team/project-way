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
        private const val ID_BUTTON_SEARCH = 9931
        private const val ID_BUTTON_GROUP = 9932
        private const val ID_BUTTON_CALENDAR = 9933
        private const val ID_BUTTON_PROFILE = 9934
        private const val ID_BUTTON_SHARE = 9935
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
    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        verticalLayout {
            rlSearch = relativeLayout {
                imgBtnSearch = imageButton {
                    id = ID_BUTTON_SEARCH
                    imageResource = R.drawable.ic_search
                }.lparams(dip(50), dip(50)) {
                    alignParentRight()
                    rightMargin = dip(5)
                    backgroundResource = R.drawable.custom_bg_item_search_button
                }
                tvSearchTitle = textView(R.string.custom_floating_menu_search_title) {
                    leftPadding = dip(10)
                    rightPadding = dip(10)
                    textSize = px2dip(dimen(R.dimen.custom_menu_text_size))
                }.lparams(wrapContent, dip(30)) {
                    centerVertically()
                    rightMargin = 10
                    leftOf(ID_BUTTON_SEARCH)
                    gravity = Gravity.CENTER
                    backgroundResource = R.drawable.custom_bg_item_menu_title
                }
            }.lparams(dip(150), wrapContent) {
                visibility = View.INVISIBLE
            }
            rlGroup = relativeLayout {
                imgBtnGroup = imageButton {
                    id = ID_BUTTON_GROUP
                    imageResource = R.drawable.ic_group_white_24dp
                }.lparams(dip(50), dip(50)) {
                    alignParentRight()
                    rightMargin = dip(5)
                    backgroundResource = R.drawable.custom_bg_item_search_button
                }
                tvGroupTitle = textView(R.string.custom_floating_menu_search_title) {
                    leftPadding = dip(10)
                    rightPadding = dip(10)
                    textSize = px2dip(dimen(R.dimen.custom_menu_text_size))
                }.lparams(wrapContent, dip(30)) {
                    centerVertically()
                    rightMargin = 10
                    leftOf(ID_BUTTON_GROUP)
                    gravity = Gravity.CENTER
                    backgroundResource = R.drawable.custom_bg_item_menu_title
                }
            }.lparams(dip(150), wrapContent) {
                visibility = View.INVISIBLE
            }
            rlCalendar = relativeLayout {
                imgBtnCalendar = imageButton {
                    id = ID_BUTTON_CALENDAR
                    imageResource = R.drawable.ic_calendar
                }.lparams(dip(50), dip(50)) {
                    alignParentRight()
                    rightMargin = dip(5)
                    backgroundResource = R.drawable.custom_bg_item_search_button
                }
                tvCalendarTitle = textView(R.string.custom_floating_menu_search_title) {
                    leftPadding = dip(10)
                    rightPadding = dip(10)
                    textSize = px2dip(dimen(R.dimen.custom_menu_text_size))
                }.lparams(wrapContent, dip(30)) {
                    centerVertically()
                    rightMargin = 10
                    leftOf(ID_BUTTON_CALENDAR)
                    gravity = Gravity.CENTER
                    backgroundResource = R.drawable.custom_bg_item_menu_title
                }
            }.lparams(dip(150), wrapContent) {
                visibility = View.INVISIBLE
            }
            rlProfile = relativeLayout {
                imgBtnProfile = imageButton {
                    id = ID_BUTTON_PROFILE
                    imageResource = R.drawable.ic_profile
                }.lparams(dip(50), dip(50)) {
                    alignParentRight()
                    rightMargin = dip(5)
                    backgroundResource = R.drawable.custom_bg_item_search_button
                }
                tvProfileTitle = textView(R.string.custom_floating_menu_search_title) {
                    leftPadding = dip(10)
                    rightPadding = dip(10)
                    textSize = px2dip(dimen(R.dimen.custom_menu_text_size))
                }.lparams(wrapContent, dip(30)) {
                    centerVertically()
                    rightMargin = 10
                    leftOf(ID_BUTTON_PROFILE)
                    gravity = Gravity.CENTER
                    backgroundResource = R.drawable.custom_bg_item_menu_title
                }
            }.lparams(dip(150), wrapContent) {
                visibility = View.INVISIBLE
            }
            rlShare = relativeLayout {
                imgBtnShare = imageButton {
                    id = ID_BUTTON_SHARE
                    imageResource = R.drawable.ic_share
                }.lparams(dip(50), dip(50)) {
                    alignParentRight()
                    rightMargin = dip(5)
                    backgroundResource = R.drawable.custom_bg_item_search_button
                }
                tvShareTitle = textView(R.string.custom_floating_menu_search_title) {
                    leftPadding = dip(10)
                    rightPadding = dip(10)
                    textSize = px2dip(dimen(R.dimen.custom_menu_text_size))
                }.lparams(wrapContent, dip(30)) {
                    centerVertically()
                    rightMargin = 10
                    leftOf(ID_BUTTON_SHARE)
                    gravity = Gravity.CENTER
                    backgroundResource = R.drawable.custom_bg_item_menu_title
                }
            }.lparams(dip(150), wrapContent) {
                visibility = View.INVISIBLE
            }


        }
    }
}

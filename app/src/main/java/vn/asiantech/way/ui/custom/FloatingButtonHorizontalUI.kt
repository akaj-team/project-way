package vn.asiantech.way.ui.custom

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.*
import vn.asiantech.way.R

/**
 * FloatingButtonHorizontalUI.
 *
 * @author at-ToanNguyen
 */
class FloatingButtonHorizontalUI : AnkoComponent<ViewGroup> {
    companion object {
        private const val ID_BUTTONSEARCH = 9931
    }

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        verticalLayout {
            relativeLayout {
                imageButton {
                    id = ID_BUTTONSEARCH
                    imageResource = R.drawable.ic_search
                }.lparams(dip(50), dip(50)) {
                    alignParentRight()
                    rightMargin = dip(5)
                    backgroundResource = R.drawable.custom_bg_item_search_button
                }
                textView(R.string.custom_floating_menu_search_title) {
                    leftPadding = dip(10)
                    rightPadding = dip(10)
                    textSize = px2dip(dimen(R.dimen.custom_menu_text_size))
                }.lparams(wrapContent, dip(30)) {
                    centerVertically()
                    rightMargin = 10
                    leftOf(ID_BUTTONSEARCH)
                    gravity = Gravity.CENTER
                    backgroundResource = R.drawable.custom_bg_item_menu_title
                }
            }.lparams(dip(150), wrapContent) {
                visibility = View.INVISIBLE
            }
            relativeLayout {
                imageButton {
                    id = ID_BUTTONSEARCH
                    imageResource = R.drawable.ic_search
                }.lparams(dip(50), dip(50)) {
                    alignParentRight()
                    rightMargin = dip(5)
                    backgroundResource = R.drawable.custom_bg_item_search_button
                }
                textView(R.string.custom_floating_menu_search_title) {
                    leftPadding = dip(10)
                    rightPadding = dip(10)
                    textSize = px2dip(dimen(R.dimen.custom_menu_text_size))
                }.lparams(wrapContent, dip(30)) {
                    centerVertically()
                    rightMargin = 10
                    leftOf(ID_BUTTONSEARCH)
                    gravity = Gravity.CENTER
                    backgroundResource = R.drawable.custom_bg_item_menu_title
                }
            }.lparams(dip(150), wrapContent) {
                visibility = View.INVISIBLE
            }
            relativeLayout {
                imageButton {
                    id = ID_BUTTONSEARCH
                    imageResource = R.drawable.ic_search
                }.lparams(dip(50), dip(50)) {
                    alignParentRight()
                    rightMargin = dip(5)
                    backgroundResource = R.drawable.custom_bg_item_search_button
                }
                textView(R.string.custom_floating_menu_search_title) {
                    leftPadding = dip(10)
                    rightPadding = dip(10)
                    textSize = px2dip(dimen(R.dimen.custom_menu_text_size))
                }.lparams(wrapContent, dip(30)) {
                    centerVertically()
                    rightMargin = 10
                    leftOf(ID_BUTTONSEARCH)
                    gravity = Gravity.CENTER
                    backgroundResource = R.drawable.custom_bg_item_menu_title
                }
            }.lparams(dip(150), wrapContent) {
                visibility = View.INVISIBLE
            }
            relativeLayout {
                imageButton {
                    id = ID_BUTTONSEARCH
                    imageResource = R.drawable.ic_search
                }.lparams(dip(50), dip(50)) {
                    alignParentRight()
                    rightMargin = dip(5)
                    backgroundResource = R.drawable.custom_bg_item_search_button
                }
                textView(R.string.custom_floating_menu_search_title) {
                    leftPadding = dip(10)
                    rightPadding = dip(10)
                    textSize = px2dip(dimen(R.dimen.custom_menu_text_size))
                }.lparams(wrapContent, dip(30)) {
                    centerVertically()
                    rightMargin = 10
                    leftOf(ID_BUTTONSEARCH)
                    gravity = Gravity.CENTER
                    backgroundResource = R.drawable.custom_bg_item_menu_title
                }
            }.lparams(dip(150), wrapContent) {
                visibility = View.INVISIBLE
            }
            relativeLayout {
                imageButton {
                    id = ID_BUTTONSEARCH
                    imageResource = R.drawable.ic_search
                }.lparams(dip(50), dip(50)) {
                    alignParentRight()
                    rightMargin = dip(5)
                    backgroundResource = R.drawable.custom_bg_item_search_button
                }
                textView(R.string.custom_floating_menu_search_title) {
                    leftPadding = dip(10)
                    rightPadding = dip(10)
                    textSize = px2dip(dimen(R.dimen.custom_menu_text_size))
                }.lparams(wrapContent, dip(30)) {
                    centerVertically()
                    rightMargin = 10
                    leftOf(ID_BUTTONSEARCH)
                    gravity = Gravity.CENTER
                    backgroundResource = R.drawable.custom_bg_item_menu_title
                }
            }.lparams(dip(150), wrapContent) {
                visibility = View.INVISIBLE
            }


        }
    }
}

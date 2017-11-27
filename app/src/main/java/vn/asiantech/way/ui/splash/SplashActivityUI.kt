package vn.asiantech.way.ui.splash

import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import org.jetbrains.anko.*
import vn.asiantech.way.R

/**
 * SplashActivityUI.
 *
 * @author at-ToanNguyen
 */
class SplashActivityUI : AnkoComponent<SplashActivity> {
    override fun createView(ui: AnkoContext<SplashActivity>): View = with(ui) {
        relativeLayout {
            frameLayout {
                lparams(matchParent, matchParent)
                imageView {
                    backgroundResource = R.drawable.bg_map
                }.lparams(matchParent, matchParent)
                imageView {
                    backgroundResource = R.drawable.bg_map
                }.lparams(matchParent, matchParent)
            }
            verticalLayout {
                gravity = Gravity.CENTER_HORIZONTAL
                imageView {
                    backgroundResource = R.drawable.ic_location_round
                }.lparams(dip(250), dip(250)) {
                    topMargin = dip(30)
                }
                textView(R.string.splash_name_application) {
                    textSize = px2dip(dimen(R.dimen.splash_size_tv_app_name))
                    typeface = Typeface.DEFAULT_BOLD
                    textColor = Color.BLACK
                }.lparams(wrapContent, wrapContent) {
                    topMargin = dip(30)
                }
                textView(R.string.splash_description_app) {
                    textColor = Color.BLACK
                    textSize = px2dip(dimen(R.dimen.splash_tv_size_app_description))
                }.lparams(wrapContent, wrapContent) {
                    topMargin = dip(40)
                    leftMargin = dimen(R.dimen.splash_tv_description_margin)
                    rightMargin = dimen(R.dimen.splash_tv_description_margin)
                    gravity = Gravity.CENTER
                }
                progressBar {
                    visibility = View.GONE
                }.lparams(wrapContent, wrapContent) {
                    bottomMargin = dimen(R.dimen.splash_progressBar_margin_bottom)
                }
                button(R.string.splash_button_enable_location) {
                    backgroundResource = R.drawable.common_bg_button_enable_location
                    leftPadding = dimen(R.dimen.splash_buttonEnableButton_padding)
                    rightPadding = dimen(R.dimen.splash_buttonEnableButton_padding)
                    textColor = ContextCompat.getColor(context, R.color.colorWhite)
                }.lparams(wrapContent, wrapContent) {
                    topMargin = dip(30)
                    bottomMargin = dimen(R.dimen.splash_progressBar_margin_bottom)
                }
            }
        }
    }
}

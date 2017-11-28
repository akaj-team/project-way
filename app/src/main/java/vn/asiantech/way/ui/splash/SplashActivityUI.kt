package vn.asiantech.way.ui.splash

import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import org.jetbrains.anko.*
import vn.asiantech.way.R
import vn.asiantech.way.utils.ScreenUtil

/**
 * SplashActivityUI.
 *
 * @author at-ToanNguyen
 */
class SplashActivityUI : AnkoComponent<SplashActivity> {
    override fun createView(ui: AnkoContext<SplashActivity>): View = with(ui) {
        scrollView {
            relativeLayout {
                frameLayout {
                    lparams(ScreenUtil.getWidthScreen(context), ScreenUtil.getHeightScreen(context))
                    imageView {
                        backgroundResource = R.drawable.bg_map
                    }.lparams(ScreenUtil.getWidthScreen(context), ScreenUtil.getHeightScreen(context))
                    imageView {
                        backgroundResource = R.drawable.bg_map
                    }.lparams(ScreenUtil.getWidthScreen(context), ScreenUtil.getHeightScreen(context))
                }
                verticalLayout {
                    gravity = Gravity.CENTER_HORIZONTAL
                    imageView {
                        backgroundResource = R.drawable.ic_location_round
                    }.lparams(dimen(R.dimen.splash_img_circle_with_high)
                            , dimen(R.dimen.splash_img_circle_with_high)) {
                        topMargin = dimen(R.dimen.splash_img_location_margin_top)
                    }
                    textView(R.string.splash_name_application) {
                        textSize = px2dip(dimen(R.dimen.splash_size_tv_app_name))
                        typeface = Typeface.DEFAULT_BOLD
                        textColor = Color.BLACK
                    }.lparams(wrapContent, wrapContent) {
                        topMargin = dimen(R.dimen.splash_tv_app_name_margin_top)
                    }
                    textView(R.string.splash_description_app) {
                        textColor = Color.BLACK
                        textSize = px2dip(dimen(R.dimen.splash_tv_size_app_description))
                    }.lparams(wrapContent, wrapContent) {
                        topMargin = dimen(R.dimen.splash_tv_description_margin_top)
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
                        topMargin = dimen(R.dimen.splash_btn_enable_location_margin_top)
                        bottomMargin = dimen(R.dimen.splash_progressBar_margin_bottom)
                    }
                }
            }
        }
    }
}

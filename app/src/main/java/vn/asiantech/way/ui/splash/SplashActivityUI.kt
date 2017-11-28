package vn.asiantech.way.ui.splash

import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import org.jetbrains.anko.*
import vn.asiantech.way.R

/**
 * SplashActivityUI.
 *
 * @author at-ToanNguyen
 */
class SplashActivityUI : AnkoComponent<SplashActivity> {
    companion object {
        private const val ID_IMAGE_LOCATION = 9910
        private const val ID_TV_NAME_APPLICATION = 9911
    }

    internal lateinit var imgFrontBackground: ImageView
    internal lateinit var imgBehindBackground: ImageView
    internal lateinit var imgCircle: ImageView
    internal lateinit var tvAppDescription: TextView
    internal lateinit var progressBar: ProgressBar
    internal lateinit var btnEnableLocation: Button
    override fun createView(ui: AnkoContext<SplashActivity>): View = with(ui) {
        scrollView {
            relativeLayout {
                lparams(matchParent, matchParent)
                frameLayout {
                    lparams(matchParent, matchParent)
                    imgFrontBackground = imageView {
                        backgroundResource = R.drawable.bg_map
                    }.lparams(matchParent, matchParent)
                    imgBehindBackground = imageView {
                        backgroundResource = R.drawable.bg_map
                    }.lparams(matchParent, matchParent)
                }
                relativeLayout {
                    lparams(matchParent, matchParent)
                    imgCircle = imageView {
                        id = ID_IMAGE_LOCATION
                        backgroundResource = R.drawable.ic_location_round
                    }.lparams(dimen(R.dimen.splash_img_circle_with_high), dimen(R.dimen.splash_img_circle_with_high)) {
                        centerHorizontally()
                        topMargin = dimen(R.dimen.splash_img_location_margin_top)
                    }
                    textView(R.string.splash_name_application)
                    {
                        id = ID_TV_NAME_APPLICATION
                        gravity = Gravity.CENTER
                        textSize = px2dip(dimen(R.dimen.splash_size_tv_app_name))
                        typeface = Typeface.DEFAULT_BOLD
                        textColor = Color.BLACK
                    }.lparams(matchParent, wrapContent) {
                        below(ID_IMAGE_LOCATION)
                        topMargin = dimen(R.dimen.splash_tv_app_name_margin_top)
                    }
                    tvAppDescription = textView(R.string.splash_description_app) {
                        gravity = Gravity.CENTER
                        textColor = Color.BLACK
                        textSize = px2dip(dimen(R.dimen.splash_tv_size_app_description))
                    }.lparams(matchParent, wrapContent) {
                        below(ID_TV_NAME_APPLICATION)
                        topMargin = dimen(R.dimen.splash_tv_description_margin_top)
                        leftMargin = dimen(R.dimen.splash_tv_description_margin)
                        rightMargin = dimen(R.dimen.splash_tv_description_margin)
                    }
                    progressBar = progressBar {
                        visibility = View.GONE
                    }.lparams(wrapContent, wrapContent) {
                        centerHorizontally()
                        alignParentBottom()
                        bottomMargin = dimen(R.dimen.splash_progressBar_margin_bottom)
                    }
                    btnEnableLocation = button(R.string.splash_button_enable_location) {
                        backgroundResource = R.drawable.common_bg_button_enable_location
                        leftPadding = dimen(R.dimen.splash_buttonEnableButton_padding)
                        rightPadding = dimen(R.dimen.splash_buttonEnableButton_padding)
                        textColor = ContextCompat.getColor(context, R.color.colorWhite)
                    }.lparams(wrapContent, wrapContent) {
                        alignParentBottom()
                        centerHorizontally()
                        bottomMargin = dimen(R.dimen.splash_progressBar_margin_bottom)
                    }
                }
            }
        }
    }
}

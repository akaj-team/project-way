package vn.asiantech.way.ui.register

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.anko.*
import vn.asiantech.way.R

/**
 * Copyright © 2017 AsianTech inc.
 * Created by doan.bien@asiantech.vn on 12/26/17.
 */
class CountryItemUI : AnkoComponent<ViewGroup> {
    internal lateinit var imgFlag: ImageView
    internal lateinit var tvTel: TextView

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        linearLayout {
            lparams(matchParent, wrapContent)
            padding = dimen(R.dimen.register_screen_avatar_margin)
            gravity = Gravity.START
            imgFlag = imageView {
                id = R.id.country_adapter_img_flag
            }.lparams(dimen(R.dimen.register_screen_img_flag_width),
                    dimen(R.dimen.margin_xxhigh)) {
                gravity = Gravity.CENTER_VERTICAL
            }
            tvTel = textView {
                id = R.id.country_adapter_tv_tel
                gravity = Gravity.CENTER_VERTICAL
            }.lparams(wrapContent, dimen(R.dimen.margin_xxhigh)) {
                leftMargin = dimen(R.dimen.register_screen_avatar_margin)
            }
        }
    }
}

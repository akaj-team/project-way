package vn.asiantech.way.ui.search

import android.graphics.Color
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
class LocationItemUI : AnkoComponent<ViewGroup> {
    internal lateinit var imgLocationIcon: ImageView
    internal lateinit var tvLocationName: TextView
    internal lateinit var tvLocationAddress: TextView

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        relativeLayout {
            lparams(matchParent, wrapContent)
            backgroundColor = Color.WHITE
            view {
                id = R.id.location_adapter_ui_view_break_line
                backgroundResource = R.color.colorSearchScreenBackground
            }.lparams(matchParent, dimen(R.dimen.break_line_view_height)) {
                bottomMargin = dimen(R.dimen.break_line_top_bot_margin)
                topMargin = dimen(R.dimen.break_line_top_bot_margin)
                leftMargin = dimen(R.dimen.break_line_left_margin)
            }

            imgLocationIcon = imageView {
                id = R.id.location_adapter_ui_img_location_icon
            }.lparams {
                margin = dimen(R.dimen.default_padding_margin)
                below(R.id.location_adapter_ui_view_break_line)
            }

            tvLocationName = textView {
                id = R.id.location_adapter_ui_tv_location_name
                singleLine = true
                textSizeDimen = R.dimen.search_screen_text_size
            }.lparams {
                below(R.id.location_adapter_ui_view_break_line)
                leftMargin = dimen(R.dimen.default_padding_margin)
                rightOf(R.id.location_adapter_ui_img_location_icon)
            }

            tvLocationAddress = textView {
                id = R.id.location_adapter_ui_tv_location_format_address
                singleLine = true
                textSizeDimen = R.dimen.search_screen_text_size
            }.lparams {
                below(R.id.location_adapter_ui_tv_location_name)
                leftMargin = dimen(R.dimen.default_padding_margin)
                rightOf(R.id.location_adapter_ui_img_location_icon)
            }
        }
    }
}

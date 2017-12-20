package vn.asiantech.way.ui.test

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import com.makeramen.roundedimageview.RoundedImageView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 19/12/2017
 */
class RecentSearchItemUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        val view = with(ui) {
            linearLayout {
                lparams(matchParent, wrapContent) {
                    verticalMargin = dimen(R.dimen.text_search_item_vertical_margin)
                }

                roundedImage(ctx) {
                    id = R.id.text_search_recent_item_img_icon
                    borderColor = ContextCompat.getColor(ctx, R.color.colorGray)
                    setCornerRadiusDimen(R.dimen.text_search_item_vertical_margin)
                    setBorderWidth(R.dimen.text_search_item_boder_width)
                }.lparams(dimen(R.dimen.text_search_item_icon_width), dimen(R.dimen.text_search_item_icon_height))

                verticalLayout {

                    textView {
                        id = R.id.text_search_recent_item_tv_name
                        textSize = 14f
                        textColorResource = R.color.colorBlack
                        typeface = Typeface.DEFAULT_BOLD
                    }.lparams {
                        topMargin = dimen(R.dimen.text_search_padding)
                    }

                    textView {
                        id = R.id.text_search_recent_item_tv_description
                        textSize = 12f
                    }

                    view().lparams(matchParent, 0) {
                        weight = 1f
                    }

                    view {
                        backgroundResource = R.color.colorGray
                    }.lparams(matchParent, dimen(R.dimen.text_search_item_break_line))
                }.lparams(matchParent, matchParent){
                    leftMargin = dimen(R.dimen.text_search_padding)
                }
            }
        }
        view.tag = RecentSearchAdapter.RecentSearchItemViewHolder(view)
        return view
    }

    private fun ViewManager.roundedImage(context: Context, init: RoundedImageView.() -> Unit): RoundedImageView {
        return ankoView({
            RoundedImageView(context)
        }, init = init, theme = 0)
    }
}

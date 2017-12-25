package vn.asiantech.way.ui.test.recent

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.TextView
import com.makeramen.roundedimageview.RoundedImageView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 19/12/2017
 */
class PopularSearchItemUI : AnkoComponent<ViewGroup> {

    internal lateinit var imgPopularIcon: RoundedImageView
    internal lateinit var tvPopularName: TextView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        linearLayout {
            lparams(matchParent, wrapContent) {
                verticalMargin = dimen(R.dimen.text_search_item_vertical_margin)
            }

            imgPopularIcon = roundedImage(ctx) {
                borderColor = ContextCompat.getColor(ctx, R.color.colorGray)
                setCornerRadiusDimen(R.dimen.text_search_item_vertical_margin)
                setBorderWidth(R.dimen.text_search_item_boder_width)
            }.lparams(dimen(R.dimen.text_search_item_icon_width), dimen(R.dimen.text_search_item_icon_height))

            verticalLayout {

                view().lparams(matchParent, 0) {
                    weight = 1f
                }

                tvPopularName = textView {
                    textSize = 14f
                    textColorResource = R.color.colorBlack
                    typeface = Typeface.DEFAULT_BOLD
                }

                view().lparams(matchParent, 0) {
                    weight = 1f
                }

                view {
                    backgroundResource = R.color.colorGray
                }.lparams(matchParent, dimen(R.dimen.text_search_item_break_line))
            }.lparams(matchParent, matchParent) {
                leftMargin = dimen(R.dimen.text_search_padding)
            }
        }
    }

    private fun ViewManager.roundedImage(context: Context, init: RoundedImageView.() -> Unit): RoundedImageView {
        return ankoView({
            RoundedImageView(context)
        }, init = init, theme = 0)
    }
}

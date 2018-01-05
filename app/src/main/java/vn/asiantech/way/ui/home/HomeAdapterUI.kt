package vn.asiantech.way.ui.home

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import at.blogc.android.views.ExpandableTextView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 26/12/2017.
 */
class HomeAdapterUI : AnkoComponent<ViewGroup> {
    internal lateinit var tvTime: TextView
    internal lateinit var tvStatus: TextView
    internal lateinit var imgArrowDown: ImageView
    internal lateinit var expTvDescription: ExpandableTextView
    internal lateinit var imgPoint: ImageView
    internal lateinit var llItemLocation: LinearLayout

    override fun createView(ui: AnkoContext<ViewGroup>) = ui.apply {
        llItemLocation = linearLayout {
            id = R.id.home_adapter_ll_location
            gravity = Gravity.CENTER_VERTICAL
            lparams(matchParent, wrapContent) {
                verticalMargin = dimen(R.dimen.home_screen_linearLayout_margin)
                padding = dimen(R.dimen.home_screen_linearLayout_padding)
            }

            tvTime = textView {
                id = R.id.home_adapter_tv_time
            }

            relativeLayout {
                view {
                    backgroundColor = Color.BLACK
                }.lparams(dip(1), matchParent) {
                    centerHorizontally()
                }

                imgPoint = imageView {
                    id = R.id.home_adapter_img_point
                }.lparams(
                        dimen(R.dimen.home_screen_view_margin),
                        dimen(R.dimen.home_screen_view_margin)
                ) {
                    centerVertically()
                }

            }.lparams(wrapContent, matchParent) {
                leftMargin = dimen(R.dimen.home_screen_view_margin)
            }

            imageView(R.drawable.ic_stop).lparams(
                    dimen(R.dimen.home_screen_imgStatus_dimension),
                    dimen(R.dimen.home_screen_imgStatus_dimension)
            ) {
                leftMargin = dimen(R.dimen.home_screen_view_margin)
            }

            relativeLayout {
                lparams(matchParent, wrapContent)

                tvStatus = textView {
                    id = R.id.home_adapter_tv_status
                    textSize = px2dip(dimen(R.dimen.home_screen_tvStatus_size))
                    textColor = Color.BLACK
                    typeface = Typeface.DEFAULT_BOLD
                }

                expTvDescription = expandableTextView {
                    id = R.id.home_adapter_tv_expandable_description
                    maxLines = 1
                }.lparams(matchParent, wrapContent) {
                    alignParentLeft()
                    below(R.id.home_adapter_tv_status)
                    rightMargin = dimen(R.dimen.home_screen_expTv_marginRight)
                    topMargin = dimen(R.dimen.home_screen_view_margin)
                }

                imgArrowDown = imageView(R.drawable.ic_keyboard_arrow_right_black_18dp) {
                    visibility = View.GONE
                    id = R.id.home_adapter_img_arrow_down
                }.lparams {
                    topOf(R.id.home_adapter_tv_expandable_description)
                    alignEnd(R.id.home_adapter_tv_expandable_description)
                }
            }
        }
    }.view

    /**
     * Function to custom expandableTextView
     */
    private inline fun ViewManager.expandableTextView(init: ExpandableTextView.() -> Unit)
            : ExpandableTextView = ankoView({ ExpandableTextView(it) }, theme = 0, init = init)
}

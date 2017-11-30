package vn.asiantech.way.ui.share

import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.anko.*
import vn.asiantech.way.R
import vn.asiantech.way.extension.bottomCard
import vn.asiantech.way.extension.rippleView

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 27/11/2017.
 */
class ShareActivityUI : AnkoComponent<ShareActivity> {
    companion object {
        const val MARGIN_VALUE_VERY_SMALL = 5
        const val MARGIN_VALUE_SMALL = 10
        const val MARGIN_VALUE_MEDIUM = 15
        const val MARGIN_VALUE_LARGE = 20
        const val MARGIN_VALUE_VERY_LARGE = 50
    }

    internal lateinit var frMapView: FrameLayout
    internal lateinit var tvTitle: TextView
    internal lateinit var tvLocation: TextView
    internal lateinit var imgEdit: ImageView
    internal lateinit var imgCurrentLocation: ImageView
    internal lateinit var imgChooseMarker: ImageView

    override fun createView(ui: AnkoContext<ShareActivity>) = with(ui) {
        relativeLayout {
            lparams(matchParent, matchParent)
            relativeLayout() {
                lparams(matchParent, matchParent)
                relativeLayout {
                    frMapView = frameLayout {
                        id = R.id.share_activity_map
                    }.lparams(matchParent, matchParent)

                    rippleView {
                        id = R.id.share_activity_btn_back
                        imageView(R.drawable.ic_back_icon_button) {
                        }.lparams(wrapContent, wrapContent)
                    }.lparams(wrapContent, wrapContent) {
                        margin = dimen(R.dimen.margin_base)
                    }

                    relativeLayout {
                        backgroundColor = ContextCompat.getColor(context, R.color.colorWhite)

                        tvTitle = textView(R.string.going_somewhere) {
                            id = R.id.share_activity_tv_title
                            textSize = px2dip(dimen(R.dimen.text_small))
                            leftPadding = dip(MARGIN_VALUE_LARGE)
                            topPadding = dip(MARGIN_VALUE_LARGE)
                            bottomPadding = dip(MARGIN_VALUE_VERY_SMALL)
                        }.lparams(matchParent, wrapContent)

                        tvLocation = textView(R.string.add_a_destination) {
                            textSize = px2dip(dimen(R.dimen.text_medium))
                            leftPadding = dip(MARGIN_VALUE_LARGE)
                            rightPadding = dip(MARGIN_VALUE_VERY_LARGE)
                            bottomPadding = dip(MARGIN_VALUE_SMALL)
                        }.lparams(matchParent, wrapContent) {
                            below(R.id.share_activity_tv_title)
                        }

                        imgEdit = imageView(R.drawable.ic_edit_location) {
                            leftPadding = dip(MARGIN_VALUE_MEDIUM)
                            topPadding = dip(MARGIN_VALUE_MEDIUM)
                            rightPadding = dip(MARGIN_VALUE_LARGE)
                        }.lparams(wrapContent, wrapContent) {
                            alignParentRight()
                        }
                    }.lparams(matchParent, wrapContent) {
                        rightOf(R.id.share_activity_btn_back)
                    }

                    imgCurrentLocation = imageView(R.drawable.ic_ht_reset_button) {
                        scaleType = ImageView.ScaleType.FIT_XY
                        visibility = View.GONE
                    }.lparams(dimen(R.dimen.set_bounds_icon_size), dimen(R.dimen.set_bounds_icon_size)) {
                        alignParentEnd()
                        alignParentRight()
                        alignWithParent = true
                        margin = dip(MARGIN_VALUE_SMALL)
                    }

                    imgChooseMarker = imageView(R.drawable.select_expected_place) {
                        visibility = View.GONE
                        padding = dip(MARGIN_VALUE_SMALL)
                    }.lparams(wrapContent, wrapContent) {
                        centerInParent()
                    }
                }.lparams(matchParent, matchParent) {
                    backgroundColor = ContextCompat.getColor(context, R.color.colorWhite)
                }

                imgChooseMarker = imageView(R.drawable.select_expected_place) {
                }.lparams(wrapContent, wrapContent) {
                    centerInParent()
                    bottomMargin = dip(MARGIN_VALUE_SMALL)
                }
            }

            bottomCard {}
        }
    }
}

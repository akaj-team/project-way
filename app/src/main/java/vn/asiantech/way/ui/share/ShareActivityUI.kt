package vn.asiantech.way.ui.share

import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.hypertrack.lib.internal.consumer.view.RippleView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.R
import vn.asiantech.way.extension.rippleView
import vn.asiantech.way.ui.custom.BottomButtonCard
import vn.asiantech.way.ui.custom.TrackingProgressInfo
import vn.asiantech.way.ui.custom.bottomCard
import vn.asiantech.way.ui.custom.trackingProgressInfo

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 27/11/2017.
 */
class ShareActivityUI : AnkoComponent<ShareActivity> {

    internal lateinit var trackingInfo: TrackingProgressInfo
    internal lateinit var bottomCard: BottomButtonCard
    internal lateinit var rlSearchLocation: RelativeLayout
    internal lateinit var frMapView: FrameLayout
    internal lateinit var btnBack: RippleView
    internal lateinit var tvTitle: TextView
    internal lateinit var tvLocation: TextView
    internal lateinit var imgEdit: ImageView
    internal lateinit var imgCurrentLocation: ImageView
    internal lateinit var imgChooseMarker: ImageView
    internal lateinit var imgPickLocation: ImageView

    override fun createView(ui: AnkoContext<ShareActivity>) = with(ui) {
        relativeLayout {
            lparams(matchParent, matchParent)
            relativeLayout {
                lparams(matchParent, matchParent)
                relativeLayout {
                    frMapView = frameLayout {
                        id = R.id.share_activity_map
                    }.lparams(matchParent, matchParent)

                    btnBack = rippleView {

                        onClick {
                            owner.eventButtonBackClicked()
                        }

                        id = R.id.share_activity_btn_back
                        imageView(R.drawable.ic_back_icon_button) { }
                    }.lparams {
                        margin = dimen(R.dimen.margin_base)
                    }

                    rlSearchLocation = relativeLayout {
                        backgroundColor = ContextCompat.getColor(ctx, R.color.colorWhite)

                        onClick {
                            owner.eventRlSearchLocationClicked()
                        }

                        tvTitle = textView(R.string.going_somewhere) {
                            id = R.id.share_activity_tv_title
                            textSize = px2dip(dimen(R.dimen.text_small))
                            leftPadding = dimen(R.dimen.share_margin_value_large)
                            topPadding = dimen(R.dimen.share_margin_value_large)
                            bottomPadding = dimen(R.dimen.share_margin_value_very_small)
                        }.lparams(matchParent, wrapContent)

                        tvLocation = textView(R.string.add_a_destination) {
                            textSize = px2dip(dimen(R.dimen.text_medium))
                            leftPadding = dimen(R.dimen.share_margin_value_large)
                            rightPadding = dimen(R.dimen.share_margin_value_very_large)
                            bottomPadding = dimen(R.dimen.share_margin_value_small)
                        }.lparams(matchParent, wrapContent) {
                            below(R.id.share_activity_tv_title)
                        }

                        imgEdit = imageView(R.drawable.ic_edit_location) {
                            leftPadding = dimen(R.dimen.share_margin_value_medium)
                            topPadding = dimen(R.dimen.share_margin_value_medium)
                            rightPadding = dimen(R.dimen.share_margin_value_large)
                        }.lparams {
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
                        margin = dimen(R.dimen.share_margin_value_small)
                    }

                    imgChooseMarker = imageView(R.drawable.select_expected_place) {
                        visibility = View.GONE
                        padding = dimen(R.dimen.share_margin_value_small)
                    }.lparams {
                        centerInParent()
                    }
                }.lparams(matchParent, matchParent) {
                    backgroundColor = ContextCompat.getColor(ctx, R.color.colorWhite)
                }

                imgPickLocation = imageView(R.drawable.select_expected_place) {
                }.lparams {
                    centerInParent()
                    bottomMargin = dimen(R.dimen.share_margin_value_small)
                }
            }

            bottomCard = bottomCard {
                visibility = View.GONE
                onBottomCardListener = object : BottomButtonCard.OnBottomCardListener {
                    override fun onBottomCardItemClick(action: String) {
                        when (action) {
                            BottomButtonCard.BottomCardActionType.CLOSE.name -> {
                                bottomCard.hideBottomCardLayout()
                                trackingInfo.showTrackingProgress()
                            }

                            BottomButtonCard.BottomCardActionType.ACTION.name -> {
                                owner.eventActionButtonClicked()
                            }

                            BottomButtonCard.BottomCardActionType.COPY.name -> {
                                owner.eventCopyLinkToClipboard()
                            }
                        }
                    }
                }
            }

            trackingInfo = trackingProgressInfo {
                onTrackingInfoListener = object : TrackingProgressInfo.OnTrackingProgressListener {
                    override fun onTrackingProgressItemClick(action: String) {
                        when (action) {
                            TrackingProgressInfo.TrackingActionType.STOP.name -> {
                                owner.eventConfirmStopDialog()
                            }
                            TrackingProgressInfo.TrackingActionType.SHARE.name -> {
                                bottomCard.showBottomCardLayout()
                                trackingInfo.hideTrackingProgress()
                            }
                            TrackingProgressInfo.TrackingActionType.CALL.name -> {
                                // No-Op
                            }
                            TrackingProgressInfo.TrackingActionType.SUMMARY.name -> {
                                owner.eventShowDialog()
                            }
                        }
                    }
                }
            }
        }
    }
}

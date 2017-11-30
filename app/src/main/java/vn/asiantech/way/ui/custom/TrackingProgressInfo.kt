package vn.asiantech.way.ui.custom

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.view.ViewManager
import android.widget.*
import com.hypertrack.lib.internal.consumer.view.CircularSeekBar
import org.jetbrains.anko.*
import vn.asiantech.way.R
import vn.asiantech.way.extension.circularSeekBar

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 30/11/2017.
 */
class TrackingProgressInfo(context: Context) : RelativeLayout(context) {
    companion object {
        const val LAYOUT_HEIGHT = 50
        const val IMAGE_SIZE = 15
    }

    internal lateinit var llTrackingProgress: LinearLayout
    internal lateinit var llTrackingInfo: LinearLayout
    internal lateinit var llActionTracking: LinearLayout
    internal lateinit var llDetailArrived: LinearLayout
    internal lateinit var rlActionDetail: RelativeLayout
    internal lateinit var rlCollapse: RelativeLayout
    internal lateinit var rlExpandedInfo: RelativeLayout
    internal lateinit var imgBtnResetPosition: ImageView
    internal lateinit var imgBtnCall: ImageView
    internal lateinit var imgArrow: ImageView
    internal lateinit var imgStartPoint: ImageView
    internal lateinit var imgEndPoint: ImageView
    internal lateinit var imgArrowRightStartItem: ImageView
    internal lateinit var imgArrowRightEndItem: ImageView
    internal lateinit var imgArrowDropDownStartItem: ImageView
    internal lateinit var imgArrowDropDownEndItem: ImageView
    internal lateinit var circleProgressBar: CircularSeekBar
    internal lateinit var tvActionStatus: TextView
    internal lateinit var tvTime: TextView
    internal lateinit var tvDistance: TextView
    internal lateinit var tvElapsedTime: TextView
    internal lateinit var tvDistanceTravelled: TextView
    internal lateinit var tvSpeed: TextView
    internal lateinit var tvBattery: TextView
    internal lateinit var tvLineStartEnd: TextView
    internal lateinit var tvStartTime: TextView
    internal lateinit var tvStartAddress: TextView
    internal lateinit var tvEndTime: TextView
    internal lateinit var tvEndAddress: TextView
    internal lateinit var btnShowSummary: Button
    internal lateinit var btnStop: Button
    internal lateinit var btnShare: Button

    init {
        AnkoContext.createDelegate(this).apply {
            relativeLayout {
                lparams(matchParent, wrapContent)

                imgBtnResetPosition = imageView(R.drawable.ic_ht_reset_button) {
                    scaleType = ImageView.ScaleType.FIT_CENTER
                }.lparams(dip(48), dip(48)) {
                    alignParentEnd()
                    alignParentRight()
                    above(R.id.tracking_progress_info_ll_tracking_progress)
                    rightMargin = 10
                }

                llTrackingProgress = verticalLayout {
                    id = R.id.tracking_progress_info_ll_tracking_progress
                    topPadding = dimen(R.dimen.padding_high)

                    verticalLayout {
                        lparams(matchParent, wrapContent) {
                            topMargin = dip(15)
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }

                        backgroundColor = ContextCompat.getColor(context, R.color.colorWhite)
                        padding = dip(20)

                        rlActionDetail = relativeLayout {
                            lparams(matchParent, wrapContent)

                            circleProgressBar = circularSeekBar {
                                lparams(dimen(R.dimen.seekBar_width), wrapContent) {
                                    alignParentLeft()
                                    alignParentStart()
                                    centerVertically()
                                    margin = dimen(R.dimen.margin_low)
                                    rotation = 90f
                                }

                                id = R.id.tracking_progress_info_circle_progress_bar
                                circleColor = ContextCompat.getColor(context, R.color.progressRestColor)
                                circleProgressColor = ContextCompat.getColor(context, R.color.tracking_experience)
                                max = 100
                                progress = 0
                                isLockEnabled = true
                            }

                            imgBtnCall = imageView(R.drawable.ic_phone_icon) {
                                padding = 30
                            }.lparams(dimen(R.dimen.phone_icon_size), dimen(R.dimen.phone_icon_size)) {
                                centerVertically()
                                alignStart(R.id.tracking_progress_info_circle_progress_bar)
                                bottomOf(circleProgressBar)
                                margin = dimen(R.dimen.margin_very_low)
                            }

                            rlCollapse = relativeLayout {
                                lparams(wrapContent, wrapContent) {
                                    centerVertically()
                                    rightOf(R.id.tracking_progress_info_circle_progress_bar)
                                }
                                //                                visibility = View.GONE

                                tvActionStatus = textView(R.string.leaving) {
                                    id = R.id.tracking_progress_info_tv_action_status
                                    maxLines = 1
                                    textColor = ContextCompat.getColor(context, R.color.colorBlack)
                                    textSize = px2dip(dimen(R.dimen.text_medium))
                                }.lparams(matchParent, wrapContent) {
                                    bottomMargin = dimen(R.dimen.margin_base)
                                    leftMargin = dip(10)
                                    gravity = Gravity.CENTER
                                }

                                imgArrow = imageView(R.drawable.ic_keyboard_arrow_right_black_18dp) {
                                }.lparams(wrapContent, wrapContent) {
                                    alignParentEnd()
                                    alignParentRight()
                                    bottomOf(R.id.tracking_progress_info_tv_action_status)
                                    rightMargin = dimen(R.dimen.margin_high)
                                    topMargin = dip(4)
                                }

                                linearLayout {
                                    lparams(wrapContent, wrapContent) {
                                        leftMargin = dip(10)
                                    }
                                    gravity = Gravity.CENTER_VERTICAL

                                    tvTime = textView(resources.getString(R.string.mask)) {
                                        textColor = ContextCompat.getColor(context, R.color.tracking_experience)
                                        textSize = px2dip(dimen(R.dimen.text_base))
                                    }.lparams(matchParent, wrapContent)

                                    tvDistance = textView(resources.getString(R.string.mask)) {
                                        gravity = Gravity.CENTER_VERTICAL
                                        textSize = px2dip(dimen(R.dimen.text_base))
                                        textColor = ContextCompat.getColor(context, R.color.gray)
                                    }.lparams(wrapContent, wrapContent) {
                                        leftMargin = dimen(R.dimen.margin_base)
                                    }
                                }.lparams {
                                    below(tvActionStatus)
                                }
                            }

                            rlExpandedInfo = relativeLayout {
                                lparams(matchParent, wrapContent) {
                                    leftMargin = dip(15)
                                }
                                backgroundColor = ContextCompat.getColor(context, R.color.colorWhite)
                                visibility = View.GONE

                                view {
                                    backgroundColor = ContextCompat.getColor(context, R.color.divider_light)
                                }.lparams(matchParent, dip(1))

                                llTrackingInfo = linearLayout {
                                    lparams(matchParent, dip(50)) {
                                        bottomMargin = dimen(R.dimen.margin_medium)
                                        topMargin = dimen(R.dimen.margin_medium)
                                    }
                                    id = R.id.tracking_progress_info_ll_tracking_info
                                    isBaselineAligned = false

                                    val llElapseTime = llItemTrackingInfo(R.id.tracking_progress_info_tv_elapse_time,
                                            resources.getString(R.string.mask),
                                            resources.getString(R.string.elapse))
                                            .lparams(0, matchParent, 1f)
                                    val llDistanceTravelled = llItemTrackingInfo(R.id.tracking_progress_info_tv_distance_travelled,
                                            resources.getString(R.string.mask),
                                            resources.getString(R.string.travelled))
                                            .lparams(0, matchParent, 1f)
                                    val llSpeed = llItemTrackingInfo(R.id.tracking_progress_info_tv_speed,
                                            resources.getString(R.string.mask),
                                            resources.getString(R.string.speed))
                                            .lparams(0, matchParent, 1f)
                                    verticalLayout {
                                        tvBattery = textViewTrackingInfo(resources.getString(R
                                                .string.mask),
                                                R.id.tracking_progress_info_tv_battery)
                                                .lparams(matchParent, wrapContent)
                                        imageView(R.drawable.ic_battery_icon) {}
                                                .lparams(wrapContent, wrapContent) {
                                                    gravity = Gravity.CENTER
                                                }
                                    }.lparams(0, matchParent, 1f)
                                    tvElapsedTime = llElapseTime.find(R.id.tracking_progress_info_tv_elapse_time)
                                    tvDistanceTravelled = llDistanceTravelled.find(R.id.tracking_progress_info_tv_distance_travelled)
                                    tvSpeed = llSpeed.find(R.id.tracking_progress_info_tv_speed)
                                }

                                llDetailArrived = linearLayout {
                                    visibility = View.GONE
                                    lparams(matchParent, wrapContent) {
                                        gravity = Gravity.CENTER
                                    }
                                    isBaselineAligned = false

                                    relativeLayout {
                                        lparams(matchParent, matchParent)

                                        imgStartPoint = imageView(R.drawable
                                                .ic_ht_source_place_marker) {
                                            id = R.id.tracking_progress_info_img_start_point
                                        }.lparams(wrapContent, wrapContent) {
                                            alignParentLeft()
                                            leftMargin = dip(30)
                                        }

                                        tvLineStartEnd = textView {
                                            backgroundColor = ContextCompat.getColor(context, R.color.colorBlack)
                                        }.lparams(dip(2), wrapContent) {
                                            alignStart(R.id.tracking_progress_info_img_start_point)
                                            below(R.id.tracking_progress_info_img_start_point)
                                            topOf(R.id.tracking_progress_info_img_end_point)
                                            bottomMargin = dip(-4.5f)
                                            leftMargin = dip(10.5f)
                                            topMargin = dip(-5.5f)
                                        }

                                        imgEndPoint = imageView(R.drawable
                                                .ic_ht_expected_place_marker) {
                                            id = R.id.tracking_progress_info_img_end_point
                                        }
                                                .lparams(wrapContent, wrapContent) {
                                                    alignStart(R.id.tracking_progress_info_img_start_point)
                                                    bottomOf(R.id.tracking_progress_info_tv_end_time)
                                                }

                                        tvStartTime = textView("3:08 CH, th 10, 2017") {
                                            id = R.id.tracking_progress_info_tv_start_time
                                        }
                                                .lparams(wrapContent, wrapContent) {
                                                    bottomMargin = dip(10)
                                                    leftMargin = dip(20)
                                                    leftOf(R.id.tracking_progress_info_img_arrow_right_start_item)
                                                    rightOf(R.id.tracking_progress_info_img_start_point)
                                                }

                                        tvStartAddress = textView("An Hai Bac An Hai Bac An Hai " +
                                                "Bac An Hai " +
                                                "Bac") {
                                            id = R.id.tracking_progress_info_tv_start_address
                                            textColor = ContextCompat.getColor(context, R.color.colorBlack)
                                        }.lparams(wrapContent, wrapContent) {
                                            bottomMargin = dip(20)
                                            alignStart(R.id.tracking_progress_info_tv_start_time)
                                            below(R.id.tracking_progress_info_tv_start_time)
                                        }

                                        imgArrowRightStartItem = imageView(R.drawable.ic_keyboard_arrow_right_black_18dp) {
                                            id = R.id.tracking_progress_info_img_arrow_right_start_item
                                        }.lparams(wrapContent, wrapContent) {
                                            alignParentRight()
                                            bottomOf(R.id.tracking_progress_info_tv_start_time)
                                            rightMargin = dip(20)
                                        }

                                        imgArrowRightEndItem = imageView(R.drawable.ic_keyboard_arrow_right_black_18dp) {
                                            id = R.id.tracking_progress_info_img_arrow_right_end_item
                                        }.lparams(wrapContent, wrapContent) {
                                            alignParentRight()
                                            alignParentEnd()
                                            bottomOf(R.id.tracking_progress_info_tv_end_time)
                                            rightMargin = dip(20)
                                        }

                                        imgArrowDropDownStartItem = imageView(R.drawable.ic_keyboard_arrow_down_black_18dp) {
                                            visibility = View.GONE
                                        }.lparams(wrapContent, wrapContent) {
                                            alignParentRight()
                                            bottomOf(R.id.tracking_progress_info_tv_start_time)
                                            rightMargin = dip(20)
                                        }

                                        imgArrowDropDownEndItem = imageView(R.drawable.ic_keyboard_arrow_down_black_18dp) {
                                            visibility = View.GONE
                                        }.lparams(wrapContent, wrapContent) {
                                            alignParentRight()
                                            bottomOf(R.id.tracking_progress_info_tv_end_time)
                                            rightMargin = dip(20)
                                        }

                                        tvEndTime = textView("3:08 CH, th 10, 2017") {
                                            id = R.id.tracking_progress_info_tv_end_time
                                        }.lparams(wrapContent, wrapContent) {
                                            bottomMargin = dip(10)
                                            alignStart(R.id.tracking_progress_info_tv_start_address)
                                            below(R.id.tracking_progress_info_tv_start_address)
                                            leftOf(R.id.tracking_progress_info_img_arrow_right_end_item)
                                        }

                                        tvEndAddress = textView("An Hai Bac An Hai Bac An Hai " +
                                                "Bac An Hai " + "Bac") {
                                            textColor = ContextCompat.getColor(context, R.color.colorBlack)
                                        }.lparams(wrapContent, wrapContent) {
                                            bottomMargin = dip(30)
                                            alignStart(R.id.tracking_progress_info_tv_end_time)
                                            below(R.id.tracking_progress_info_tv_end_time)
                                        }
                                    }
                                }.lparams {
                                    below(R.id.tracking_progress_info_ll_tracking_info)
                                }
                            }
                        }
                    }

                    llActionTracking = linearLayout {
                        visibility = View.GONE
                        lparams(matchParent, dimen(R.dimen.ripple_height))
                        backgroundColor = ContextCompat.getColor(context, R.color.colorWhite)

                        btnStop = button(R.string.stop) {
                            gravity = Gravity.CENTER
                            backgroundColor = ContextCompat.getColor(context, R.color.colorWhite)
                        }.lparams(matchParent, matchParent) { weight = 1f }


                        view {
                            backgroundColor = ContextCompat.getColor(context, R.color.divider_light)
                        }.lparams(dip(2), matchParent)

                        btnShare = button(R.string.share) {
                            gravity = Gravity.CENTER
                            backgroundColor = ContextCompat.getColor(context, R.color.colorWhite)
                        }.lparams(matchParent, matchParent) { weight = 1f }
                    }

                    btnShowSummary = button(R.string.arrived_show_summary) {
                        visibility = View.GONE
                        gravity = Gravity.CENTER
                        backgroundColor = ContextCompat.getColor(context, R.color.colorWhite)
                    }.lparams(matchParent, matchParent) { weight = 1f }

                }.lparams(matchParent, wrapContent) {
                    alignParentBottom()
                }
            }
        }
    }

    private fun ViewManager.llItemTrackingInfo(tvId: Int, value: String,
                                               description: String) = verticalLayout {
        textViewTrackingInfo(value, tvId)
                .lparams(matchParent, wrapContent)
        textViewTrackingInfoDescription(description)
                .lparams(matchParent, wrapContent) {
                    topMargin = dip(4)
                }
    }

    private fun ViewManager.textViewTrackingInfo(value: String, tvId: Int) = textView(value) {
        textColor = ContextCompat.getColor(context, R.color.colorBlack)
        id = tvId
        gravity = Gravity.CENTER

    }

    private fun ViewManager.textViewTrackingInfoDescription(description: String) = textView(description) {
        textColor = ContextCompat.getColor(context, R.color.gray)
        gravity = Gravity.CENTER
    }

}

//private fun initiateView() {
//    rlCollapse.setOnClickListener {
//        if (rlExpandedInfo.visibility == View.GONE) {
//            rlExpandedInfo.visibility = View.VISIBLE
//            imgArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_18dp)
//        } else {
//            rlExpandedInfo.visibility = View.GONE
//            imgArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_18dp)
//        }
//    }
//    rippleTrackingToggle.setOnRippleCompleteListener {
//        if (trackingProgressClick != null) {
//            trackingProgressClick?.onStopButtonClick()
//        }
//    }
//    rippleShareLink.setOnClickListener {
//        if (trackingProgressClick != null) {
//            trackingProgressClick?.onShareButtonClick()
//        }
//    }
//    imgBtnCall.setOnClickListener {
//        if (trackingProgressClick != null) {
//            trackingProgressClick?.onCallButtonClick()
//        }
//    }
//}

//internal fun showTrackingProgress() {
//    llTrackingProgress.visibility = View.VISIBLE
//    AnimationUtils.expand(this, AnimationUtils.DURATION_DEFAULT_VALUE_ANIMATION)
//}
//
//internal fun hideTrackingProgress() {
//    AnimationUtils.collapse(this, AnimationUtils.DURATION_DEFAULT_VALUE_ANIMATION)
//}
//
//internal fun showTrackingInfor() {
//    llDetailArrived.visibility = View.VISIBLE
//}
//
//internal fun hideTrackingInfor() {
//    llDetailArrived.visibility = View.VISIBLE
//}
//
//internal fun showArrivedInfor() {
//    llActionTracking.visibility = View.GONE
//    llTrackingInfo.visibility = View.GONE
//    btnShowSummary.visibility = View.VISIBLE
//}
//
//internal fun hideArrivedInfor() {
//    llDetailArrived.visibility = View.VISIBLE
//}
//
///**
// * Interface create fun onClickListener for BottomButtonCard
// */
//interface TrackingProgressClick {
//    /**
//     * Button stop link click listener
//     */
//    fun onStopButtonClick()
//
//    /**
//     * Button share link click listener
//     */
//    fun onShareButtonClick()
//
//    /**
//     * Button call link click listener
//     */
//    fun onCallButtonClick()
//}


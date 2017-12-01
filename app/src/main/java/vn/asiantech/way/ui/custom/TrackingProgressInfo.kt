package vn.asiantech.way.ui.custom

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.view.ViewManager
import android.widget.*
import com.hypertrack.lib.internal.consumer.utils.AnimationUtils
import com.hypertrack.lib.internal.consumer.view.CircularSeekBar
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.R
import vn.asiantech.way.extension.circularSeekBar

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 30/11/2017.
 */
class TrackingProgressInfo(context: Context) : RelativeLayout(context) {
    companion object {
        const val ROTATION_VALUE = 90f
        const val LAYOUT_WEIGHT = 1f
        const val PROGRESS_BAR_MAX = 100
    }

    internal lateinit var llTrackingProgress: LinearLayout
    internal lateinit var llTrackingInfo: LinearLayout
    internal lateinit var llActionTracking: LinearLayout
    internal lateinit var llDetailArrived: LinearLayout
    internal lateinit var rlActionDetail: RelativeLayout
    internal lateinit var rlCollapse: RelativeLayout
    internal lateinit var rlExpandedInfo: RelativeLayout
    internal lateinit var imgResetPosition: ImageView
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

    val onStopButtonClick: () -> Unit = {}
    val onShareButtonClick: () -> Unit = {}
    val onShowSummaryButtonClick: () -> Unit = {}
    val onCallButtonClick: () -> Unit = {}

    init {
        AnkoContext.createDelegate(this).apply {
            relativeLayout {
                lparams(matchParent, wrapContent)

                imgResetPosition = imageView(R.drawable.ic_ht_reset_button) {
                    scaleType = ImageView.ScaleType.FIT_CENTER
                }.lparams(dimen(R.dimen.track_button_size), dimen(R.dimen.track_button_size)) {
                    alignParentEnd()
                    alignParentRight()
                    above(R.id.tracking_progress_info_ll_tracking_progress)
                    rightMargin = dimen(R.dimen.track_margin_value_very_low)
                }

                llTrackingProgress = verticalLayout {
                    id = R.id.tracking_progress_info_ll_tracking_progress
                    topPadding = dimen(R.dimen.padding_high)

                    verticalLayout {
                        lparams(matchParent, wrapContent) {
                            topMargin = dimen(R.dimen.track_margin_value_low)
                            leftMargin = dimen(R.dimen.track_margin_value_low)
                            rightMargin = dimen(R.dimen.track_margin_value_low)
                        }

                        padding = dimen(R.dimen.track_margin_value_medium)

                        rlActionDetail = relativeLayout {
                            lparams(matchParent, wrapContent)
                            backgroundResource = R.color.colorWhite

                            circleProgressBar = circularSeekBar {
                                lparams(dimen(R.dimen.seekBar_width), wrapContent) {
                                    alignParentLeft()
                                    alignParentStart()
                                    margin = dimen(R.dimen.margin_low)
                                    rotation = ROTATION_VALUE
                                }

                                id = R.id.tracking_progress_info_circle_progress_bar
                                circleColor = ContextCompat.getColor(context, R.color.progressRestColor)
                                circleProgressColor = ContextCompat.getColor(context, R.color.tracking_experience)
                                max = PROGRESS_BAR_MAX
                                progress = 0
                                isLockEnabled = true
                            }

                            imgBtnCall = imageView(R.drawable.ic_phone_icon) {
                                onClick {
                                    onCallButtonClick
                                }
                                padding = dimen(R.dimen.track_margin_value_high)
                            }.lparams(dimen(R.dimen.phone_icon_size), dimen(R.dimen.phone_icon_size)) {
                                alignStart(R.id.tracking_progress_info_circle_progress_bar)
                                margin = dimen(R.dimen.track_btn_call_margin)
                            }

                            rlCollapse = relativeLayout {
                                id = R.id.tracking_progress_info_rl_collapse
                                lparams {
                                    rightOf(R.id.tracking_progress_info_circle_progress_bar)
                                }
                                onClick {
                                    if (rlExpandedInfo.visibility == View.GONE) {
                                        rlExpandedInfo.visibility = View.VISIBLE
                                        imgArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_18dp)
                                    } else {
                                        rlExpandedInfo.visibility = View.GONE
                                        imgArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_18dp)
                                    }
                                }

                                tvActionStatus = textView(R.string.leaving) {
                                    id = R.id.tracking_progress_info_tv_action_status
                                    maxLines = 1
                                    textColor = ContextCompat.getColor(context, R.color.colorBlack)
                                    textSize = px2dip(dimen(R.dimen.text_medium))
                                }.lparams(matchParent, wrapContent) {
                                    bottomMargin = dimen(R.dimen.margin_base)
                                    leftMargin = dimen(R.dimen.track_margin_value_very_low)
                                    gravity = Gravity.CENTER
                                }

                                imgArrow = imageView(R.drawable.ic_keyboard_arrow_right_black_18dp) {
                                }.lparams {
                                    alignParentEnd()
                                    alignParentRight()
                                    bottomOf(R.id.tracking_progress_info_tv_action_status)
                                    rightMargin = dimen(R.dimen.margin_high)
                                    topMargin = dimen(R.dimen.track_top_margin)
                                }

                                linearLayout {
                                    lparams {
                                        leftMargin = dimen(R.dimen.track_margin_value_very_low)
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
                                    }.lparams {
                                        leftMargin = dimen(R.dimen.margin_base)
                                    }
                                }.lparams {
                                    alignStart(R.id.tracking_progress_info_tv_action_status)
                                    below(tvActionStatus)
                                }
                            }

                            rlExpandedInfo = relativeLayout {
                                lparams(matchParent, wrapContent) {
                                    leftMargin = dimen(R.dimen.track_margin_value_low)
                                    rightMargin = dimen(R.dimen.track_margin_value_low)
                                    below(R.id.tracking_progress_info_rl_collapse)
                                }
                                backgroundResource = R.color.colorWhite
                                visibility = View.GONE

                                view {
                                    backgroundResource = R.color.divider_light
                                }.lparams(matchParent, dip(2))

                                llTrackingInfo = linearLayout {
                                    lparams(matchParent, dimen(R.dimen.track_layout_height)) {
                                        bottomMargin = dimen(R.dimen.margin_medium)
                                        topMargin = dimen(R.dimen.margin_medium)
                                    }
                                    id = R.id.tracking_progress_info_ll_tracking_info
                                    isBaselineAligned = false

                                    val llElapseTime = llItemTrackingInfo(R.id.tracking_progress_info_tv_elapse_time,
                                            resources.getString(R.string.mask),
                                            resources.getString(R.string.elapse))
                                            .lparams(0, matchParent, LAYOUT_WEIGHT)
                                    val llDistanceTravelled = llItemTrackingInfo(
                                            R.id.tracking_progress_info_tv_distance_travelled,
                                            resources.getString(R.string.mask),
                                            resources.getString(R.string.travelled))
                                            .lparams(0, matchParent, LAYOUT_WEIGHT)
                                    val llSpeed = llItemTrackingInfo(R.id.tracking_progress_info_tv_speed,
                                            resources.getString(R.string.mask),
                                            resources.getString(R.string.speed))
                                            .lparams(0, matchParent, LAYOUT_WEIGHT)
                                    verticalLayout {
                                        lparams(0, matchParent, LAYOUT_WEIGHT)
                                        tvBattery = textViewTrackingInfo(resources.getString(R
                                                .string.mask),
                                                R.id.tracking_progress_info_tv_battery)
                                                .lparams(matchParent, wrapContent)
                                        imageView(R.drawable.ic_battery_icon) {}
                                                .lparams {
                                                    gravity = Gravity.CENTER
                                                }
                                    }
                                    tvElapsedTime = llElapseTime.find(R.id.tracking_progress_info_tv_elapse_time)
                                    tvDistanceTravelled = llDistanceTravelled.find(
                                            R.id.tracking_progress_info_tv_distance_travelled)
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
                                        }.lparams {
                                            alignParentLeft()
                                            leftMargin = dimen(R.dimen.track_margin_value_high)
                                        }

                                        tvLineStartEnd = textView {
                                            backgroundResource = R.color.colorBlack
                                        }.lparams(dip(2), wrapContent) {
                                            alignStart(R.id.tracking_progress_info_img_start_point)
                                            below(R.id.tracking_progress_info_img_start_point)
                                            topOf(R.id.tracking_progress_info_img_end_point)
                                            bottomMargin = dimen(R.dimen.track_tv_bottom_margin)
                                            leftMargin = dimen(R.dimen.track_tv_left_margin)
                                            topMargin = dimen(R.dimen.track_tv_top_margin)
                                        }

                                        imgEndPoint = imageView(R.drawable
                                                .ic_ht_expected_place_marker) {
                                            id = R.id.tracking_progress_info_img_end_point
                                        }.lparams {
                                            alignStart(R.id.tracking_progress_info_img_start_point)
                                            bottomOf(R.id.tracking_progress_info_tv_start_address)
                                        }

                                        tvStartTime = textView(R.string.tv_start_time) {
                                            id = R.id.tracking_progress_info_tv_start_time
                                        }.lparams {
                                            bottomMargin = dimen(R.dimen.track_margin_value_very_low)
                                            leftMargin = dimen(R.dimen.track_margin_value_medium)
                                            leftOf(R.id.tracking_progress_info_img_arrow_right_start_item)
                                            rightOf(R.id.tracking_progress_info_img_start_point)
                                        }

                                        tvStartAddress = textView(R.string.tv_start_address) {
                                            visibility = View.GONE
                                            id = R.id.tracking_progress_info_tv_start_address
                                            textColor = ContextCompat.getColor(context, R.color.colorBlack)
                                        }.lparams {
                                            bottomMargin = dimen(R.dimen.track_margin_value_medium)
                                            alignStart(R.id.tracking_progress_info_tv_start_time)
                                            below(R.id.tracking_progress_info_tv_start_time)
                                        }

                                        imgArrowRightStartItem = imageView(
                                                R.drawable.ic_keyboard_arrow_right_black_18dp) {
                                            id = R.id.tracking_progress_info_img_arrow_right_start_item
                                        }.lparams {
                                            alignParentRight()
                                            topOf(R.id.tracking_progress_info_tv_start_address)
                                            rightMargin = dimen(R.dimen.track_margin_value_medium)
                                            bottomMargin = dimen(R.dimen.track_margin_value_very_low)
                                        }

                                        imgArrowRightEndItem = imageView(
                                                R.drawable.ic_keyboard_arrow_right_black_18dp) {
                                            id = R.id.tracking_progress_info_img_arrow_right_end_item
                                        }.lparams {
                                            alignParentRight()
                                            alignParentEnd()
                                            bottomOf(R.id.tracking_progress_info_tv_start_address)
                                            rightMargin = dimen(R.dimen.track_margin_value_medium)
                                            bottomMargin = dimen(R.dimen.track_margin_value_very_low)
                                        }

                                        imgArrowDropDownStartItem = imageView(
                                                R.drawable.ic_keyboard_arrow_down_black_18dp) {
                                            visibility = View.GONE
                                        }.lparams {
                                            alignParentRight()
                                            topOf(R.id.tracking_progress_info_tv_start_address)
                                            rightMargin = dimen(R.dimen.track_margin_value_medium)
                                            bottomMargin = dimen(R.dimen.track_margin_value_very_low)
                                        }

                                        imgArrowDropDownEndItem = imageView(
                                                R.drawable.ic_keyboard_arrow_down_black_18dp) {
                                            visibility = View.GONE
                                        }.lparams {
                                            alignParentRight()
                                            bottomOf(R.id.tracking_progress_info_tv_start_address)
                                            rightMargin = dimen(R.dimen.track_margin_value_medium)
                                            bottomMargin = dimen(R.dimen.track_margin_value_very_low)
                                        }

                                        tvEndTime = textView(R.string.tv_end_time) {
                                            id = R.id.tracking_progress_info_tv_end_time
                                        }.lparams {
                                            bottomMargin = dimen(R.dimen.track_margin_value_very_low)
                                            alignStart(R.id.tracking_progress_info_tv_start_address)
                                            below(R.id.tracking_progress_info_tv_start_address)
                                            leftOf(R.id.tracking_progress_info_img_arrow_right_end_item)
                                        }

                                        tvEndAddress = textView(R.string.tv_end_address) {
                                            visibility = View.GONE
                                            id = R.id.tracking_progress_info_tv_end_address
                                            textColor = ContextCompat.getColor(context, R.color.colorBlack)
                                        }.lparams {
                                            bottomMargin = dimen(R.dimen.track_margin_value_high)
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
                        lparams(matchParent, dimen(R.dimen.ripple_height))
                        backgroundResource = R.color.colorWhite

                        btnStop = button(R.string.stop) {
                            gravity = Gravity.CENTER
                            backgroundResource = R.color.colorWhite
                            onClick {
                                onStopButtonClick
                            }

                        }.lparams(matchParent, matchParent) { weight = LAYOUT_WEIGHT }

                        view {
                            backgroundColor = ContextCompat.getColor(context, R.color.divider_light)
                        }.lparams(dip(2), matchParent)

                        btnShare = button(R.string.share) {
                            onClick {
                                onShareButtonClick
                            }
                            gravity = Gravity.CENTER
                            backgroundColor = ContextCompat.getColor(context, R.color.colorWhite)
                        }.lparams(matchParent, matchParent) { weight = LAYOUT_WEIGHT }
                    }

                    btnShowSummary = button(R.string.arrived_show_summary) {
                        onClick {
                            onShowSummaryButtonClick
                        }
                        visibility = View.GONE
                        gravity = Gravity.CENTER
                        backgroundResource = R.color.colorWhite
                    }.lparams(matchParent, matchParent) { weight = LAYOUT_WEIGHT }
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
                    topMargin = dimen(R.dimen.track_top_margin)
                }
    }

    private fun ViewManager.textViewTrackingInfo(value: String, tvId: Int) = textView(value) {
        textColor = ContextCompat.getColor(context, R.color.colorBlack)
        id = tvId
        gravity = Gravity.CENTER
    }

    private fun ViewManager.textViewTrackingInfoDescription(description: String) =
            textView(description) {
                textColor = ContextCompat.getColor(context, R.color.gray)
                gravity = Gravity.CENTER
            }

    internal fun showTrackingProgress() {
        llTrackingProgress.visibility = View.VISIBLE
        AnimationUtils.expand(this, AnimationUtils.DURATION_DEFAULT_VALUE_ANIMATION)
    }

    internal fun hideTrackingProgress() {
        AnimationUtils.collapse(this, AnimationUtils.DURATION_DEFAULT_VALUE_ANIMATION)
    }

    internal fun showTrackingInfor(): TrackingProgressInfo {
        llDetailArrived.visibility = View.VISIBLE
        return this
    }

    internal fun hideTrackingInfor(): TrackingProgressInfo {
        llDetailArrived.visibility = View.VISIBLE
        return this
    }

    internal fun showArrivedInfor(): TrackingProgressInfo {
        llActionTracking.visibility = View.GONE
        llTrackingInfo.visibility = View.GONE
        btnShowSummary.visibility = View.VISIBLE
        return this
    }

    internal fun hideArrivedInfor(): TrackingProgressInfo {
        llDetailArrived.visibility = View.VISIBLE
        return this
    }
}

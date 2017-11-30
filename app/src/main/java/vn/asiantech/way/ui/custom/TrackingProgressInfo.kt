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
        const val ID_LL_TRACKING_PROGRESS = 1001
        const val ID_LL_TRACKING_INFO = 1002
        const val ID_CIRCLE_PROGRESS_BAR = 1003
        const val ID_TV_ACTION_STATUS = 1004
        const val ID_TV_ELAPSE_TIME = 1005
        const val ID_TV_DISTANCE_TRAVELLED = 1006
        const val ID_TV_SPEED = 1007
        const val ID_TV_BATTERY = 1008
        const val ID_TV_START_TIME = 1009
        const val ID_TV_END_TIME = 1010
        const val ID_TV_START_ADDRESS = 1011
        const val ID_IMG_START_POINT = 1012
        const val ID_IMG_END_POINT = 1013
        const val ID_IMG_ARROW_RIGHT_START_ITEM = 1014
        const val ID_IMG_ARROW_RIGHT_END_ITEM = 1015
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
                    above(ID_LL_TRACKING_PROGRESS)
                    rightMargin = 10
                }

                llTrackingProgress = verticalLayout {
                    id = ID_LL_TRACKING_PROGRESS
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

                                id = ID_CIRCLE_PROGRESS_BAR
                                circleColor = ContextCompat.getColor(context, R.color.progressRestColor)
                                circleProgressColor = ContextCompat.getColor(context, R.color.tracking_experience)
                                max = 100
                                progress = 0

                            }

                            imgBtnCall = imageView(R.drawable.ic_phone_icon) {}
                                    .lparams(dimen(R.dimen.phone_icon_size), dimen(R.dimen.phone_icon_size)) {
                                        centerVertically()
                                        margin = dimen(R.dimen.margin_low)
                                        alignStart(ID_CIRCLE_PROGRESS_BAR)
                                        alignParentTop()
                                    }

                            rlCollapse = relativeLayout {
                                lparams(wrapContent, wrapContent) {
                                    centerVertically()
                                    rightOf(ID_CIRCLE_PROGRESS_BAR)
                                }
                                //                                visibility = View.GONE

                                tvActionStatus = textView(R.string.leaving) {
                                    id = ID_TV_ACTION_STATUS
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
                                    bottomOf(ID_TV_ACTION_STATUS)
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
                                    id = ID_LL_TRACKING_INFO
                                    isBaselineAligned = false

                                    val llElapseTime = llItemTrackingInfo(ID_TV_ELAPSE_TIME,
                                            resources.getString(R.string.mask),
                                            resources.getString(R.string.elapse))
                                            .lparams(0, matchParent, 1f)
                                    val llDistanceTravelled = llItemTrackingInfo(ID_TV_DISTANCE_TRAVELLED,
                                            resources.getString(R.string.mask),
                                            resources.getString(R.string.travelled))
                                            .lparams(0, matchParent, 1f)
                                    val llSpeed = llItemTrackingInfo(ID_TV_SPEED,
                                            resources.getString(R.string.mask),
                                            resources.getString(R.string.speed))
                                            .lparams(0, matchParent, 1f)
                                    verticalLayout {
                                        tvBattery = textViewTrackingInfo(resources.getString(R
                                                .string.mask),
                                                ID_TV_BATTERY)
                                                .lparams(matchParent, wrapContent)
                                        imageView(R.drawable.ic_battery_icon) {}
                                                .lparams(wrapContent, wrapContent) {
                                                    gravity = Gravity.CENTER
                                                }
                                    }.lparams(0, matchParent, 1f)
                                    tvElapsedTime = llElapseTime.find(ID_TV_ELAPSE_TIME)
                                    tvDistanceTravelled = llDistanceTravelled.find(ID_TV_DISTANCE_TRAVELLED)
                                    tvSpeed = llSpeed.find(ID_TV_SPEED)
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
                                            id = ID_IMG_START_POINT
                                        }.lparams(wrapContent, wrapContent) {
                                            alignParentLeft()
                                            leftMargin = dip(30)
                                        }

                                        tvLineStartEnd = textView {
                                            backgroundColor = ContextCompat.getColor(context, R.color.colorBlack)
                                        }.lparams(dip(2), wrapContent) {
                                            alignStart(ID_IMG_START_POINT)
                                            below(ID_IMG_START_POINT)
                                            topOf(ID_IMG_END_POINT)
                                            bottomMargin = dip(-4.5f)
                                            leftMargin = dip(10.5f)
                                            topMargin = dip(-5.5f)
                                        }

                                        imgEndPoint = imageView(R.drawable
                                                .ic_ht_expected_place_marker) {
                                            id = ID_IMG_END_POINT
                                        }
                                                .lparams(wrapContent, wrapContent) {
                                                    alignStart(ID_IMG_START_POINT)
                                                    bottomOf(ID_TV_END_TIME)
                                                }

                                        tvStartTime = textView("3:08 CH, th 10, 2017") {
                                            id = ID_TV_START_TIME
                                        }
                                                .lparams(wrapContent, wrapContent) {
                                                    bottomMargin = dip(10)
                                                    leftMargin = dip(20)
                                                    leftOf(ID_IMG_ARROW_RIGHT_START_ITEM)
                                                    rightOf(ID_IMG_START_POINT)
                                                }

                                        tvStartAddress = textView("An Hai Bac An Hai Bac An Hai " +
                                                "Bac An Hai " +
                                                "Bac") {
                                            id = ID_TV_START_ADDRESS
                                            textColor = ContextCompat.getColor(context, R.color.colorBlack)
                                        }.lparams(wrapContent, wrapContent) {
                                            bottomMargin = dip(20)
                                            alignStart(ID_TV_START_TIME)
                                            below(ID_TV_START_TIME)
                                        }

                                        imgArrowRightStartItem = imageView(R.drawable.ic_keyboard_arrow_right_black_18dp) {
                                            id = ID_IMG_ARROW_RIGHT_START_ITEM
                                        }.lparams(wrapContent, wrapContent) {
                                            alignParentRight()
                                            bottomOf(ID_TV_START_TIME)
                                            rightMargin = dip(20)
                                        }

                                        imgArrowRightEndItem = imageView(R.drawable.ic_keyboard_arrow_right_black_18dp) {
                                            id = ID_IMG_ARROW_RIGHT_END_ITEM
                                        }.lparams(wrapContent, wrapContent) {
                                            alignParentRight()
                                            alignParentEnd()
                                            bottomOf(ID_TV_END_TIME)
                                            rightMargin = dip(20)
                                        }

                                        imgArrowDropDownStartItem = imageView(R.drawable.ic_keyboard_arrow_down_black_18dp) {
                                            visibility = View.GONE
                                        }.lparams(wrapContent, wrapContent) {
                                            alignParentRight()
                                            bottomOf(ID_TV_START_TIME)
                                            rightMargin = dip(20)
                                        }

                                        imgArrowDropDownEndItem = imageView(R.drawable.ic_keyboard_arrow_down_black_18dp) {
                                            visibility = View.GONE
                                        }.lparams(wrapContent, wrapContent) {
                                            alignParentRight()
                                            bottomOf(ID_TV_END_TIME)
                                            rightMargin = dip(20)
                                        }

                                        tvEndTime = textView("3:08 CH, th 10, 2017") {
                                            id = ID_TV_END_TIME
                                        }.lparams(wrapContent, wrapContent) {
                                            bottomMargin = dip(10)
                                            alignStart(ID_TV_START_ADDRESS)
                                            below(ID_TV_START_ADDRESS)
                                            leftOf(ID_IMG_ARROW_RIGHT_END_ITEM)
                                        }

                                        tvEndAddress = textView("An Hai Bac An Hai Bac An Hai " +
                                                "Bac An Hai " + "Bac") {
                                            textColor = ContextCompat.getColor(context, R.color.colorBlack)
                                        }.lparams(wrapContent, wrapContent) {
                                            bottomMargin = dip(30)
                                            alignStart(ID_TV_END_TIME)
                                            below(ID_TV_END_TIME)
                                        }
                                    }
                                }.lparams {
                                    below(ID_LL_TRACKING_INFO)
                                }
                            }

                        }
                    }

                    llActionTracking = linearLayout {
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


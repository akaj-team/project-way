package vn.asiantech.way.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.hypertrack.lib.internal.consumer.utils.AnimationUtils
import kotlinx.android.synthetic.main.tracking_progress_view.view.*
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 06/11/2017.
 */
class TrackingProgressInfo @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
        RelativeLayout(context, attrs) {

    var trackingProgressClick: TrackingProgressClick? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.tracking_progress_view, this, true)
        initiateView()
    }

    private fun initiateView() {
        rlCollapse.setOnClickListener {
            if (rlExpandedInfo.visibility == View.GONE) {
                rlExpandedInfo.visibility = View.VISIBLE
                imgArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_18dp)
            } else {
                rlExpandedInfo.visibility = View.GONE
                imgArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_18dp)
            }
        }
        rippleTrackingToggle.setOnRippleCompleteListener {
            if (trackingProgressClick != null) {
                trackingProgressClick?.onStopButtonClick()
            }
        }
        rippleShareLink.setOnClickListener {
            if (trackingProgressClick != null) {
                trackingProgressClick?.onShareButtonClick()
            }
        }
        imgBtnCall.setOnClickListener {
            if (trackingProgressClick != null) {
                trackingProgressClick?.onCallButtonClick()
            }
        }
    }

    internal fun showTrackingProgress() {
        llTrackingProgress.visibility = View.VISIBLE
        AnimationUtils.expand(this, AnimationUtils.DURATION_DEFAULT_VALUE_ANIMATION)
    }

    internal fun hideTrackingProgress() {
        AnimationUtils.collapse(this, AnimationUtils.DURATION_DEFAULT_VALUE_ANIMATION)
    }

    internal fun showTrackingInfor() {
        llDetailArrived.visibility = View.VISIBLE
    }

    internal fun hideTrackingInfor() {
        llDetailArrived.visibility = View.VISIBLE
    }

    internal fun showArrivedInfor() {
        llActionTracking.visibility = View.GONE
        llTrackingInfo.visibility = View.GONE
        btnShowSummary.visibility = View.VISIBLE
    }

    internal fun hideArrivedInfor() {
        llDetailArrived.visibility = View.VISIBLE
    }

    /**
     * Interface create fun onClickListener for BottomButtonCard
     */
    interface TrackingProgressClick {
        /**
         * Button stop link click listener
         */
        fun onStopButtonClick()

        /**
         * Button share link click listener
         */
        fun onShareButtonClick()

        /**
         * Button call link click listener
         */
        fun onCallButtonClick()
    }
}

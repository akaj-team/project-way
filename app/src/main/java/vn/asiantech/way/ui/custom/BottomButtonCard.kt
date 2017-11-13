package vn.asiantech.way.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.hypertrack.lib.internal.common.util.HTTextUtils
import com.hypertrack.lib.internal.consumer.utils.AnimationUtils
import kotlinx.android.synthetic.main.bottom_button_card_view.view.*
import vn.asiantech.way.R

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 27/09/2017.
 */
class BottomButtonCard @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
        RelativeLayout(context, attrs) {

    var buttonListener: ButtonListener? = null
    var actionType: ActionType

    // TODO: Will use in future
//    val isActionTypeConfirmLocation: Boolean
//        get() = actionType == ActionType.CONFIRM_LOCATION
//
//    val isActionTypeStartTracking: Boolean
//        get() = actionType == ActionType.START_TRACKING
//
//    val isActionTypeShareTrackingLink: Boolean
//        get() = actionType == ActionType.SHARE_TRACKING_URL
//
//    val isActionTypeShareBackLocation: Boolean
//        get() = actionType == ActionType.SHARE_BACK_LOCATION
    init {
        LayoutInflater.from(context).inflate(R.layout.bottom_button_card_view, this, true)
        initiateView()
        actionType = ActionType.START_TRACKING
    }

    /**
     * Enum define for item type
     */
    enum class ActionType {
        START_TRACKING,
        CONFIRM_LOCATION,
        SHARE_TRACKING_URL,
        SHARE_BACK_LOCATION
    }

    private fun initiateView() {
        btnClose.setOnRippleCompleteListener {
            if (buttonListener != null) {
                buttonListener?.onCloseButtonClick()
            }
        }

        btnSharing.setOnRippleCompleteListener {
            if (buttonListener != null) {
                buttonListener?.onActionButtonClick()
            }
        }

        tvCopyLink.setOnClickListener {
            if (buttonListener != null) {
                buttonListener?.onCopyButtonClick()
                tvCopyLink?.isEnabled = false
                tvCopyLink?.text = context.getString(R.string.share_textview_text_copied)
            }
        }
    }

    internal fun setTitleText(title: String) {
        tvTitle.text = title
        tvTitle.visibility = View.VISIBLE
    }

    internal fun setDescriptionText(description: String) {
        if (HTTextUtils.isEmpty(description)) {
            tvDescription.text = ""
            tvDescription.visibility = View.GONE
        } else {
            tvDescription.text = description
            tvDescription.visibility = View.VISIBLE
        }
    }

    internal fun hideCloseButton() {
        btnClose.visibility = View.GONE
    }

    internal fun hideTvTitle() {
        tvTitle.visibility = View.GONE
    }

    internal fun hideTvDescription() {
        tvDescription.visibility = View.GONE
    }

    internal fun showCloseButton() {
        btnClose.visibility = View.VISIBLE
    }

    internal fun setShareButtonText(actionText: String) {
        tvStartShare.visibility = View.VISIBLE
        tvStartShare.text = actionText
    }

    internal fun showBottomCardLayout() {
        hideProgress()
        btnSharing.visibility = View.VISIBLE
        AnimationUtils.expand(this, AnimationUtils.DURATION_DEFAULT_VALUE_ANIMATION)
    }

    internal fun hideBottomCardLayout() {
        hideProgress()
        AnimationUtils.collapse(this, AnimationUtils.DURATION_DEFAULT_VALUE_ANIMATION)
    }

    internal fun startProgress() {
        tvStartShare.visibility = View.GONE
        imgLoader.visibility = View.VISIBLE
        val rotationAnim = android.view.animation.AnimationUtils.loadAnimation(context,
                R.anim.rotate)
        rotationAnim.fillAfter = true
        imgLoader.startAnimation(rotationAnim)
    }
    // TODO: Will use in future
//    fun hideActionButton() {
//        AnimationUtils.collapse(btnSharing)
//    }

    internal fun showActionButton() {
        AnimationUtils.expand(btnSharing)
    }

    internal fun hideProgress() {
        imgLoader.visibility = View.GONE
        imgLoader.clearAnimation()
    }

    internal fun showTrackingURLLayout() {
        AnimationUtils.expand(rlLinkShare)
    }
    // TODO: Will use in future
//    fun hideTrackingURLLayout() {
//        rlLinkShare.visibility = View.GONE
//    }

    internal fun showTitle() {
        tvTitle.visibility = View.VISIBLE
    }

    /**
     * Interface create fun onClickListener for BottomButtonCard
     */
    interface ButtonListener {
        /**
         * Button close click listener
         */
        fun onCloseButtonClick()

        /**
         * Button share click listener
         */
        fun onActionButtonClick()

        /**
         * Button copy link click listener
         */
        fun onCopyButtonClick()
    }
}

package vn.asiantech.way.ui.share

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.hypertrack.lib.internal.common.util.HTTextUtils
import com.hypertrack.lib.internal.consumer.utils.AnimationUtils
import kotlinx.android.synthetic.main.bottom_button_card_view.view.*
import vn.asiantech.way.R

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 27/09/2017.
 */
class BottomButtonCard @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(mContext, attrs) {
    var buttonListener: ButtonListener? = null
    var actionType: ActionType

    val isActionTypeConfirmLocation: Boolean
        get() = actionType == ActionType.CONFIRM_LOCATION

    val isActionTypeStartTracking: Boolean
        get() = actionType == ActionType.START_TRACKING

    val isActionTypeShareTrackingLink: Boolean
        get() = actionType == ActionType.SHARE_TRACKING_URL

    val isActionTypeShareBackLocation: Boolean
        get() = actionType == ActionType.SHARE_BACK_LOCATION

    enum class ActionType {
        START_TRACKING,
        CONFIRM_LOCATION,
        SHARE_TRACKING_URL,
        SHARE_BACK_LOCATION
    }

    init {
        LayoutInflater.from(mContext).inflate(R.layout.bottom_button_card_view, this, true)
        initiateView()
        actionType = ActionType.START_TRACKING
    }

    private fun initiateView() {
        btnClose.setOnRippleCompleteListener {
            if (buttonListener != null) {
                buttonListener!!.OnCloseButtonClick()
            }
        }

        btnSharing.setOnRippleCompleteListener {
            if (buttonListener != null) {
                buttonListener!!.OnActionButtonClick()
            }
            Log.d("TTTTTT", "null")
        }

        tvCopyLink.setOnClickListener {
            if (buttonListener != null) {
                buttonListener!!.OnCopyButtonClick()
                tvCopyLink?.isEnabled = false
                tvCopyLink?.text = "Copied"
            }
        }
    }

    internal fun setTitleText(title: String) {
        this.tvTitle.text = title
        tvTitle.visibility = View.VISIBLE
    }

    fun setDescriptionText(description: String) {
        if (HTTextUtils.isEmpty(description)) {
            tvDescription.text = ""
            tvDescription.visibility = View.GONE
        } else {
            tvDescription.text = description
            tvDescription.visibility = View.VISIBLE
        }
    }

    fun setErrorText(text: String) {
        tvDescription.error = text
    }

    fun showCloseButton() {
        btnClose.visibility = View.VISIBLE
    }

    fun hideCloseButton() {
        btnClose.visibility = View.GONE
    }

    fun setShareButtonText(actionText: String) {
        tvStartShare.visibility = View.VISIBLE
        tvStartShare.text = actionText
    }

    fun showBottomCardLayout() {
        hideProgress()
        btnSharing.visibility = View.VISIBLE
        AnimationUtils.expand(this, AnimationUtils.DURATION_DEFAULT_VALUE_ANIMATION)
    }

    fun hideBottomCardLayout() {
        hideProgress()
        AnimationUtils.collapse(this, AnimationUtils.DURATION_DEFAULT_VALUE_ANIMATION, rlLinkShare)
    }

    fun startProgress() {
        tvStartShare.visibility = View.GONE
        imgLoader.visibility = View.VISIBLE
        val rotationAnim = android.view.animation.AnimationUtils.loadAnimation(context,
                R.anim.rotate)
        rotationAnim.fillAfter = true
        imgLoader.startAnimation(rotationAnim)
    }

    fun hideActionButton() {
        AnimationUtils.collapse(btnSharing)
    }

    fun showActionButton() {
        AnimationUtils.expand(btnSharing)
    }

    fun hideProgress() {
        imgLoader.visibility = View.GONE
        imgLoader.clearAnimation()
    }

    fun showTrackingURLLayout() {
        AnimationUtils.expand(rlLinkShare)
    }

    fun hideTrackingURLLayout() {
        rlLinkShare.visibility = View.GONE
    }

    fun hideTitle() {
        tvTitle.visibility = View.GONE
    }

    fun showTitle() {
        tvTitle.visibility = View.VISIBLE
    }

    fun setTrackingURL(URL: String) {
        tvURL.text = URL
    }

    interface ButtonListener {
        fun OnCloseButtonClick()

        fun OnActionButtonClick()

        fun OnCopyButtonClick()
    }
}

package vn.asiantech.way.ui.share

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
class BottomButtonCard @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr) {
    var type: ActionType
    var btnListener: ButtonListener? = null

    enum class ActionType {
        START_TRACKING,
        CONFIRM_LOCATION,
        SHARE_TRACKING_URL,
        SHARE_BACK_LOCATION
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.bottom_button_card_view, this, true)
        type = ActionType.START_TRACKING
    }

    fun setOnClickView() {
        btnClose.setOnRippleCompleteListener {
            if (btnListener != null) {
                btnListener?.OnCloseButtonClick()
            }
        }
        btnSharing.setOnRippleCompleteListener {
            if (btnListener != null) {
                btnListener?.OnActionButtonClick()
            }
        }
        tvCopyLink.setOnClickListener {
            if (btnListener != null) {
                btnListener?.OnCopyButtonClick()
                tvCopyLink.isEnabled = false
                tvCopyLink.text = "Copied"
            }
        }
    }

    fun setTitleText(title: String) {
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

    fun showCloseButton() {
        btnClose.visibility = View.VISIBLE
    }

    fun hideCloseButton() {
        btnClose.visibility = View.GONE
    }

    fun setShareButtonText(shareText: String) {
        tvStartShare.visibility = View.VISIBLE
        tvStartShare.text = shareText
    }

    fun showBottomCardLayout() {
        hideProgress()
        tvStartShare.visibility = View.VISIBLE
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

    fun hideProgress() {
        imgLoader.visibility = View.GONE
        imgLoader.clearAnimation()
    }

    fun hideSharingButton() {
        AnimationUtils.collapse(btnSharing)
    }

    fun showSharingButton() {
        AnimationUtils.expand(btnSharing)
    }

    fun showTrackingURLLayout() {
        AnimationUtils.expand(rlLinkShare)
    }

    fun hideTrackingURLLayout() {
        rlLinkShare.visibility = View.GONE
    }

    fun isActionTypeConfirmLocation(): Boolean = type.equals(ActionType.CONFIRM_LOCATION)

    fun isActionTypeShareTrackingLink(): Boolean = type.equals(ActionType.SHARE_TRACKING_URL)


    fun hideTitle() {
        tvTitle.visibility = View.GONE
    }

    fun showTitle() {
        tvTitle.visibility = View.VISIBLE
    }

    fun setTextLink(URL: String) {
        tvURL.text = URL
    }

    interface ButtonListener {
        fun OnCloseButtonClick()

        fun OnActionButtonClick()

        fun OnCopyButtonClick()
    }
}

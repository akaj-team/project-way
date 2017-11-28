package vn.asiantech.way.ui.custom

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.hypertrack.lib.internal.common.util.HTTextUtils
import com.hypertrack.lib.internal.consumer.utils.AnimationUtils
import com.hypertrack.lib.internal.consumer.view.RippleView
import org.jetbrains.anko.*
import vn.asiantech.way.R
import vn.asiantech.way.extension.rippleView

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 27/09/2017.
 */
class BottomButtonCard(context: Context) :
        RelativeLayout(context) {

    companion object {
        const val ID_RL_BOTTOM_CARD = 1001
        const val ID_BTN_CLOSE = 1002
        const val ID_TV_TITLE = 1003
        const val ID_TV_DESCRIPTION = 1004
        const val ID_BTN_SHARING = 1005
        const val LAYOUT_HEIGHT = 50
        const val IMAGE_SIZE = 15
    }

    internal lateinit var rlBottomCard: RelativeLayout
    internal lateinit var rlLinkShare: RelativeLayout
    internal lateinit var btnClose: RippleView
    internal lateinit var btnSharing: RippleView
    internal lateinit var tvTitle: TextView
    internal lateinit var tvDescription: TextView
    internal lateinit var tvStartShare: TextView
    internal lateinit var tvURL: TextView
    internal lateinit var tvCopyLink: TextView
    internal lateinit var imgLoader: ImageView

    var onBottomCardListener: OnBottomCardListener? = null
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
        actionType = ActionType.START_TRACKING

        AnkoContext.createDelegate(this).apply {
            rlBottomCard = relativeLayout {
                id = ID_RL_BOTTOM_CARD
                backgroundResource = R.color.colorPink
                bottomPadding = dimen(R.dimen.padding_medium)
                topPadding = dimen(R.dimen.padding_high)
                lparams(matchParent, wrapContent) {
                    alignParentBottom()
                }
                btnClose = rippleView {
                    id = ID_BTN_CLOSE
                    bottomPadding = dimen(R.dimen.padding_medium)
                    leftPadding = dimen(R.dimen.padding_medium)
                    rightPadding = dimen(R.dimen.padding_medium)
                    imageView {
                        imageResource = R.drawable.ic_navigation_close
                    }.lparams(dip(IMAGE_SIZE), dip(IMAGE_SIZE))

                }.lparams(wrapContent, wrapContent) {
                    alignParentRight()
                }

                tvTitle = textView(R.string.bottom_button_card_title_text) {
                    id = ID_TV_TITLE
                    textColor = ContextCompat.getColor(context, R.color.colorWhite)
                    textSize = px2dip(dimen(R.dimen.text_large))
                    typeface = Typeface.DEFAULT_BOLD
                }.lparams(wrapContent, wrapContent) {
                    centerHorizontally()
                    topMargin = dimen(R.dimen.margin_low)
                }

                tvDescription = textView(R.string.bottom_button_card_description_text) {
                    id = ID_TV_DESCRIPTION
                    textSize = px2dip(dimen(R.dimen.text_medium))
                }.lparams(wrapContent, wrapContent) {
                    centerHorizontally()
                    below(ID_TV_TITLE)
                    leftMargin = dimen(R.dimen.margin_huge)
                    rightMargin = dimen(R.dimen.margin_huge)
                    topMargin = dimen(R.dimen.margin_base)
                }

                btnSharing = rippleView {
                    id = ID_BTN_SHARING
                    backgroundResource = R.drawable.custom_bg_button_share
                    tvStartShare = textView(R.string.bottom_button_card_view_text_start_share) {
                        textSize = px2dip(dimen(R.dimen.text_large))
                    }.lparams(wrapContent, wrapContent) {
                        centerInParent()
                    }

                    imgLoader = imageView(R.drawable.ic_live_location_loading) {
                        visibility = View.GONE
                    }.lparams(wrapContent, wrapContent) {
                        centerInParent()
                    }
                }.lparams(matchParent, dip(LAYOUT_HEIGHT)) {
                    centerHorizontally()
                    below(ID_TV_DESCRIPTION)
                    bottomMargin = dimen(R.dimen.margin_low)
                    leftMargin = dimen(R.dimen.margin_huge)
                    rightMargin = dimen(R.dimen.margin_huge)
                    topMargin = dimen(R.dimen.margin_high)
                }

                rlLinkShare = relativeLayout {
                    backgroundResource = R.drawable.custom_bg_link_share_sheet
                    padding = dimen(R.dimen.padding_very_low)

                    tvURL = textView(R.string.bottom_button_card_link_text) {
                        textColor = ContextCompat.getColor(context, R.color.colorWhite)
                        setTextIsSelectable(true)
                    }.lparams(wrapContent, wrapContent) {
                        centerVertically()
                        visibility = View.GONE
                        leftMargin = dimen(R.dimen.margin_xxhigh)
                    }

                    tvCopyLink = textView(R.string.bottom_button_card_text_copy_link) {
                        textColor = ContextCompat.getColor(context, R.color.black)
                        backgroundResource = R.drawable.custom_bg_button_copy
                        padding = dimen(R.dimen.padding_base)
                    }.lparams(wrapContent, wrapContent) {
                        alignParentRight()
                        margin = dimen(R.dimen.margin_low)
                    }
                }.lparams(matchParent, dip(LAYOUT_HEIGHT)) {
                    below(ID_BTN_SHARING)
                    leftMargin = dimen(R.dimen.margin_medium)
                    rightMargin = dimen(R.dimen.margin_medium)
                    topMargin = dimen(R.dimen.margin_medium)
                }
            }
        }

        initiateView()

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
            if (onBottomCardListener != null) {
                onBottomCardListener?.onCloseButtonClick()
            }
        }

        btnSharing.setOnRippleCompleteListener {
            if (onBottomCardListener != null) {
                onBottomCardListener?.onActionButtonClick()
            }
        }

        tvCopyLink.setOnClickListener {
            if (onBottomCardListener != null) {
                onBottomCardListener?.onCopyButtonClick()
                tvCopyLink?.isEnabled = false
                tvCopyLink?.text = context.getString(R.string.share_textview_text_copied)
            }
        }
//        rlCollapse.setOnClickListener {
//            if (rlExpandedInfo.visibility == View.GONE) {
//                rlExpandedInfo.visibility = View.VISIBLE
//                imgArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_18dp)
//            } else {
//                rlExpandedInfo.visibility = View.GONE
//                imgArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_18dp)
//            }
//        }
//        rippleTrackingToggle.setOnRippleCompleteListener {
//            if (onBottomCardListener != null) {
//                onBottomCardListener?.onStopButtonClick()
//            }
//        }
//        rippleShareLink.setOnClickListener {
//            if (onBottomCardListener != null) {
//                onBottomCardListener?.onShareButtonClick()
//            }
//        }
//        imgBtnCall.setOnClickListener {
//            if (onBottomCardListener != null) {
//                onBottomCardListener?.onCallButtonClick()
//            }
//        }
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

    fun hideBottomCardLayout() {
        hideProgress()
        AnimationUtils.collapse(this, AnimationUtils.DURATION_DEFAULT_VALUE_ANIMATION, rlBottomCard)
    }

//    fun showTrackingProgress() {
//        llTrackingProgress.visibility = View.VISIBLE
//    }
//
//    fun hideTrackingProgress() {
//        llTrackingProgress.visibility = View.GONE
//    }
    // TODO: Will use in future
//
//    fun startProgress() {
//        tvStartShare.visibility = View.GONE
//        imgLoader.visibility = View.VISIBLE
//        val rotationAnim = android.view.animation.AnimationUtils.loadAnimation(context,
//                R.anim.rotate)
//        rotationAnim.fillAfter = true
//        imgLoader.startAnimation(rotationAnim)
//    }
    // TODO:Will use in future

    //    fun hideBottomCardLayout() {
//        hideProgress()
//        AnimationUtils.collapse(this, AnimationUtils.DURATION_DEFAULT_VALUE_ANIMATION, rlLinkShare)
//    }
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
    interface OnBottomCardListener {
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

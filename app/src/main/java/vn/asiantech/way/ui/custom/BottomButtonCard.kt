package vn.asiantech.way.ui.custom

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.hypertrack.lib.internal.common.util.HTTextUtils
import com.hypertrack.lib.internal.consumer.utils.AnimationUtils
import com.hypertrack.lib.internal.consumer.view.RippleView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.R
import vn.asiantech.way.extension.rippleView

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 27/09/2017.
 */
class BottomButtonCard(context: Context) :
        RelativeLayout(context) {

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

    var actionType: ActionType

    val onCloseButtonClick: () -> Unit = {}
    val onActionButtonClick: () -> Unit = {}
    val onCopyButtonClick: () -> Unit = {}

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
                id = R.id.bottom_button_card_rl_bottom_card
                backgroundResource = R.color.colorPink
                bottomPadding = dimen(R.dimen.padding_medium)
                topPadding = dimen(R.dimen.padding_high)
                lparams(matchParent, wrapContent) {
                    alignParentBottom()
                }
                btnClose = rippleView {
                    id = R.id.bottom_button_card_btn_close
                    bottomPadding = dimen(R.dimen.padding_medium)
                    leftPadding = dimen(R.dimen.padding_medium)
                    rightPadding = dimen(R.dimen.padding_medium)

                    setOnRippleCompleteListener {
                        onCloseButtonClick
                    }

                    imageView {
                        imageResource = R.drawable.ic_navigation_close
                    }.lparams(dimen(R.dimen.image_size), dimen(R.dimen.image_size))

                }.lparams(wrapContent, wrapContent) {
                    alignParentRight()
                }

                tvTitle = textView(R.string.bottom_button_card_title_text) {
                    id = R.id.bottom_button_card_tv_title
                    textColor = ContextCompat.getColor(context, R.color.colorWhite)
                    textSize = px2dip(dimen(R.dimen.text_large))
                    typeface = Typeface.DEFAULT_BOLD
                }.lparams(wrapContent, wrapContent) {
                    centerHorizontally()
                    topMargin = dimen(R.dimen.margin_low)
                }

                tvDescription = textView(R.string.bottom_button_card_description_text) {
                    id = R.id.bottom_button_card_tv_description
                    gravity = Gravity.CENTER
                    textSize = px2dip(dimen(R.dimen.text_medium))
                }.lparams(matchParent, wrapContent) {
                    centerHorizontally()
                    below(R.id.bottom_button_card_tv_title)
                    leftMargin = dimen(R.dimen.margin_huge)
                    rightMargin = dimen(R.dimen.margin_huge)
                    topMargin = dimen(R.dimen.margin_base)
                }

                btnSharing = rippleView {
                    id = R.id.bottom_button_card_btn_sharing
                    backgroundResource = R.drawable.custom_bg_button_share

                    setOnRippleCompleteListener {
                        onActionButtonClick
                    }

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
                }.lparams(matchParent, dimen(R.dimen.layout_height)) {
                    centerHorizontally()
                    below(R.id.bottom_button_card_tv_description)
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

                        onClick {
                            onCopyButtonClick
                            tvCopyLink.isEnabled = false
                            tvCopyLink.text = context.getString(R.string.share_textview_text_copied)

                        }

                    }.lparams(wrapContent, wrapContent) {
                        alignParentRight()
                        margin = dimen(R.dimen.margin_low)
                    }
                }.lparams(matchParent, dimen(R.dimen.layout_height)) {
                    below(R.id.bottom_button_card_btn_sharing)
                    leftMargin = dimen(R.dimen.margin_medium)
                    rightMargin = dimen(R.dimen.margin_medium)
                    topMargin = dimen(R.dimen.margin_medium)
                }
            }
        }
    }

    /*
     * Enum define for item type
     */
    enum class ActionType {
        START_TRACKING,
        CONFIRM_LOCATION,
        SHARE_TRACKING_URL,
        SHARE_BACK_LOCATION
    }

    internal fun setTitleText(title: String): BottomButtonCard {
        tvTitle.text = title
        tvTitle.visibility = View.VISIBLE
        return this
    }

    internal fun setDescriptionText(description: String): BottomButtonCard {
        if (HTTextUtils.isEmpty(description)) {
            tvDescription.text = ""
            tvDescription.visibility = View.GONE
        } else {
            tvDescription.text = description
            tvDescription.visibility = View.VISIBLE
        }
        return this
    }

    internal fun hideCloseButton(): BottomButtonCard {
        btnClose.visibility = View.GONE
        return this
    }

    internal fun hideTvTitle(): BottomButtonCard {
        tvTitle.visibility = View.GONE
        return this
    }

    internal fun hideTvDescription(): BottomButtonCard {
        tvDescription.visibility = View.GONE
        return this
    }

    internal fun showCloseButton(): BottomButtonCard {
        btnClose.visibility = View.VISIBLE
        return this
    }

    internal fun setShareButtonText(actionText: String): BottomButtonCard {
        tvStartShare.visibility = View.VISIBLE
        tvStartShare.text = actionText
        return this
    }

    internal fun showBottomCardLayout() {
        hideProgress()
        btnSharing.visibility = View.VISIBLE
        AnimationUtils.expand(this, AnimationUtils.DURATION_DEFAULT_VALUE_ANIMATION)
    }

    internal fun hideBottomCardLayout() {
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
    internal fun startProgress(): BottomButtonCard {
        tvStartShare.visibility = View.GONE
        imgLoader.visibility = View.VISIBLE
        val rotationAnim = android.view.animation.AnimationUtils.loadAnimation(context,
                R.anim.rotate)
        rotationAnim.fillAfter = true
        imgLoader.startAnimation(rotationAnim)
        return this
    }
    // TODO: Will use in future
//    fun hideActionButton() {
//        AnimationUtils.collapse(btnSharing)
//    }

    internal fun showActionButton() {
        AnimationUtils.expand(btnSharing)
    }

    internal fun hideProgress(): BottomButtonCard {
        imgLoader.visibility = View.GONE
        imgLoader.clearAnimation()
        return this
    }

    internal fun showTrackingURLLayout() {
        AnimationUtils.expand(rlLinkShare)
    }
    // TODO: Will use in future
//    fun hideTrackingURLLayout() {
//        rlLinkShare.visibility = View.GONE
//    }

    internal fun showTitle(): BottomButtonCard {
        tvTitle.visibility = View.VISIBLE
        return this
    }
}

package vn.asiantech.way.ui.custom

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.view.ViewManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.hypertrack.lib.internal.common.util.HTTextUtils
import com.hypertrack.lib.internal.consumer.utils.AnimationUtils
import com.hypertrack.lib.internal.consumer.view.RippleView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
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

    var onBottomCardListener: OnBottomCardListener? = null

    init {

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
                    visibility = View.GONE
                    id = R.id.bottom_button_card_btn_close
                    bottomPadding = dimen(R.dimen.padding_medium)
                    horizontalPadding = dimen(R.dimen.padding_medium)

                    setOnRippleCompleteListener {
                        onBottomCardListener?.onBottomCardItemClick(BottomCardActionType.CLOSE.name)
                    }

                    imageView {
                        imageResource = R.drawable.ic_navigation_close
                    }.lparams(dimen(R.dimen.image_size), dimen(R.dimen.image_size))

                }.lparams {
                    alignParentRight()
                }

                tvTitle = textView(R.string.share_textview_text_look_good) {
                    id = R.id.bottom_button_card_tv_title
                    textColor = ContextCompat.getColor(ctx, R.color.colorWhite)
                    textSize = px2dip(dimen(R.dimen.text_large))
                    typeface = Typeface.DEFAULT_BOLD
                }.lparams {
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
                    horizontalMargin = dimen(R.dimen.margin_huge)
                    topMargin = dimen(R.dimen.margin_base)
                }

                btnSharing = rippleView {
                    id = R.id.bottom_button_card_btn_sharing
                    backgroundResource = R.drawable.custom_bg_button_share

                    setOnRippleCompleteListener {
                        onBottomCardListener?.onBottomCardItemClick(BottomCardActionType.ACTION.name)
                    }

                    tvStartShare = textView(R.string.bottom_button_card_view_text_start_share) {
                        textSize = px2dip(dimen(R.dimen.text_large))
                    }.lparams {
                        centerInParent()
                    }

                    imgLoader = imageView(R.drawable.ic_live_location_loading) {
                        visibility = View.GONE
                    }.lparams {
                        centerInParent()
                    }
                }.lparams(matchParent, dimen(R.dimen.layout_height)) {
                    centerHorizontally()
                    below(R.id.bottom_button_card_tv_description)
                    horizontalMargin = dimen(R.dimen.margin_huge)
                    bottomMargin = dimen(R.dimen.margin_low)
                    topMargin = dimen(R.dimen.margin_high)
                }

                rlLinkShare = relativeLayout {
                    backgroundResource = R.drawable.custom_bg_link_share_sheet
                    padding = dimen(R.dimen.padding_very_low)

                    tvURL = textView(R.string.bottom_button_card_link_text) {
                        textColor = ContextCompat.getColor(ctx, R.color.colorWhite)
                        setTextIsSelectable(true)
                    }.lparams {
                        centerVertically()
                        visibility = View.GONE
                        leftMargin = dimen(R.dimen.margin_xxhigh)
                    }

                    tvCopyLink = textView(R.string.bottom_button_card_text_copy_link) {
                        textColor = ContextCompat.getColor(ctx, R.color.black)
                        backgroundResource = R.drawable.custom_bg_button_copy
                        padding = dimen(R.dimen.padding_base)

                        onClick {
                            onBottomCardListener?.onBottomCardItemClick(BottomCardActionType.COPY.name)
                            tvCopyLink.isEnabled = false
                            tvCopyLink.text = ctx.getString(R.string.share_textview_text_copied)
                        }

                    }.lparams {
                        alignParentRight()
                        margin = dimen(R.dimen.margin_low)
                    }
                }.lparams(matchParent, dimen(R.dimen.layout_height)) {
                    below(R.id.bottom_button_card_btn_sharing)
                    horizontalMargin = dimen(R.dimen.margin_medium)
                    topMargin = dimen(R.dimen.margin_medium)
                }
            }
        }
    }

    /**
     * Enum define for action type
     */
    enum class BottomCardActionType {
        CLOSE, COPY, ACTION
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
        AnimationUtils.collapse(this, AnimationUtils.DURATION_DEFAULT_VALUE_ANIMATION)
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

    /**
     * Interface create fun onClickListener for BottomButtonCard
     */
    interface OnBottomCardListener {
        /**
         * Bottom card item click listener
         */
        fun onBottomCardItemClick(action: String)
    }
}

internal fun ViewManager.bottomCard() = bottomCard {}

internal fun ViewManager.bottomCard(init: BottomButtonCard.() -> Unit): BottomButtonCard {
    return ankoView({ BottomButtonCard(it) }, 0, init)
}

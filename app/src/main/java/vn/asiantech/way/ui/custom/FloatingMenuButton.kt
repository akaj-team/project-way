package vn.asiantech.way.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.R

@SuppressLint("ViewConstructor")
/**
 * FloatingMenuButton.
 *
 * @author at-ToanNguyen
 */
class FloatingMenuButton(private var onMenuClickListener: OnMenuClickListener,
                         context: Context, attrs: AttributeSet? = null) :
        LinearLayout(context, attrs) {
    internal lateinit var imgBtnSearch: ImageButton
    internal lateinit var imgBtnGroup: ImageButton
    internal lateinit var imgBtnCalendar: ImageButton
    internal lateinit var imgBtnProfile: ImageButton
    internal lateinit var imgBtnShare: ImageButton
    private lateinit var rlSearch: RelativeLayout
    private lateinit var rlGroup: RelativeLayout
    private lateinit var rlCalendar: RelativeLayout
    private lateinit var rlProfile: RelativeLayout
    private lateinit var rlShare: RelativeLayout
    private lateinit var imgBtnMenu: ImageButton
    private lateinit var frOverlay: FrameLayout
    private var isExpand = false


    init {
        AnkoContext.createDelegate(this).apply {
            relativeLayout {
                lparams(matchParent, matchParent)
                frOverlay = frameLayout {
                    lparams(matchParent, matchParent)
                    visibility = View.GONE
                    backgroundResource = R.color.colorOverlay
                    onClick {
                        if (isExpand) {
                            collapseMenu()
                            isExpand = false
                            setGoneOverLay()
                        }
                    }
                }
                verticalLayout {
                    gravity = Gravity.END
                    gravity = Gravity.BOTTOM
                    lparams(matchParent, matchParent)
                    rlSearch = itemFloatingButton(R.id.floating_btn_menu_img_btn_search,
                            R.drawable.custom_bg_item_search_button,
                            R.drawable.ic_search, R.string.custom_floating_menu_search_title).lparams()
                    imgBtnSearch = rlSearch.find(R.id.floating_btn_menu_img_btn_search)
                    imgBtnSearch.onClick {
                        setGoneOverLay()
                        visibilityAllChildView(View.INVISIBLE)
                        onMenuClickListener.eventItemMenuClicked(imgBtnSearch)
                    }
                    rlGroup = itemFloatingButton(R.id.floating_btn_menu_img_btn_group,
                            R.drawable.custom_bg_item_group_button,
                            R.drawable.ic_group_white_24dp, R.string.custom_floating_menu_group_title).lparams()
                    imgBtnGroup = rlGroup.find(R.id.floating_btn_menu_img_btn_group)
                    imgBtnGroup.onClick {
                        setGoneOverLay()
                        visibilityAllChildView(View.INVISIBLE)
                        onMenuClickListener.eventItemMenuClicked(imgBtnGroup)
                    }
                    rlCalendar = itemFloatingButton(R.id.floating_btn_menu_img_btn_calendar,
                            R.drawable.custom_bg_item_calendar_button,
                            R.drawable.ic_calendar, R.string.custom_floating_menu_calendar_title).lparams()
                    imgBtnCalendar = rlCalendar.find(R.id.floating_btn_menu_img_btn_calendar)
                    imgBtnCalendar.onClick {
                        setGoneOverLay()
                        visibilityAllChildView(View.INVISIBLE)
                        onMenuClickListener.eventItemMenuClicked(imgBtnCalendar)
                    }
                    rlProfile = itemFloatingButton(R.id.floating_btn_menu_img_btn_profile,
                            R.drawable.custom_bg_item_profile_button,
                            R.drawable.ic_profile, R.string.custom_floating_menu_profile_title).lparams()
                    imgBtnProfile = rlProfile.find(R.id.floating_btn_menu_img_btn_profile)
                    imgBtnProfile.onClick {
                        setGoneOverLay()
                        visibilityAllChildView(View.INVISIBLE)
                        onMenuClickListener.eventItemMenuClicked(imgBtnProfile)
                    }
                    rlShare = itemFloatingButton(R.id.floating_btn_menu_img_btn_share,
                            R.drawable.custom_bg_item_share_button,
                            R.drawable.ic_share, R.string.custom_floating_menu_share_title).lparams()
                    imgBtnShare = rlShare.find(R.id.floating_btn_menu_img_btn_share)
                    imgBtnShare.onClick {
                        setGoneOverLay()
                        visibilityAllChildView(View.INVISIBLE)
                        onMenuClickListener.eventItemMenuClicked(imgBtnShare)
                    }
                    imgBtnMenu = imageButton {
                        imageResource = R.drawable.ic_menu
                        backgroundResource = R.drawable.custom_menu_button
                        onClick {
                            onMenuClick()
                            frOverlay.visibility = if (isExpand) View.VISIBLE else View.GONE
                        }
                    }.lparams(dimen(R.dimen.width_height_image_button_menu),
                            dimen(R.dimen.width_height_image_button_menu)) {
                        gravity = Gravity.END
                        bottomMargin = dimen(R.dimen.top_bot_margin_image_button_menu)
                        topMargin = dimen(R.dimen.top_bot_margin_image_button_menu)
                        padding = dimen(R.dimen.padding_floating)
                    }
                }.applyRecursively { view: View ->
                    when (view) {
                        is RelativeLayout -> {
                            view.gravity = Gravity.END
                            view.visibility = View.INVISIBLE
                        }
                        is TextView -> view.gravity = Gravity.CENTER
                    }
                }
            }
        }
    }

    /**
     * Collapse menu
     */
    private fun collapseMenu() {
        val animInvisible: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_invisible)
        startAnimationFab(animInvisible)
        visibilityAllChildView(View.INVISIBLE)
    }

    private fun ViewManager.itemFloatingButton(icId: Int, icBg: Int, icSrc: Int, title: Int) = relativeLayout {
        lparams(dimen(R.dimen.width_relative_layout), wrapContent)
        imageButton {
            id = icId
            imageResource = icSrc
            backgroundResource = icBg
        }.lparams(dimen(R.dimen.width_height_image_button), dimen(R.dimen.width_height_image_button)) {
            alignParentRight()
            rightMargin = dimen(R.dimen.margin_right_image_button)
        }
        textView(title) {
            leftPadding = dimen(R.dimen.padding_floating)
            rightPadding = dimen(R.dimen.padding_floating)
            textSize = px2dip(dimen(R.dimen.custom_menu_text_size))
            backgroundResource = R.drawable.custom_bg_item_menu_title
        }.lparams(wrapContent, dimen(R.dimen.height_text_view)) {
            centerVertically()
            rightMargin = dimen(R.dimen.margin_right_text_view)
            leftOf(icId)
            gravity = Gravity.CENTER
        }
    }

    private fun onMenuClick() {
        val anim: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_rotate)
        val animVisible: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_visible)
        val animInvisible: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_invisible)
        imgBtnMenu.startAnimation(anim)
        isExpand = if (checkItemViewVisibility()) {
            startAnimationFab(animVisible)
            visibilityAllChildView(View.VISIBLE)
            true
        } else {
            startAnimationFab(animInvisible)
            false
        }
        animInvisible.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
                // No-op
            }

            override fun onAnimationEnd(p0: Animation?) {
                visibilityAllChildView(View.INVISIBLE)
            }

            override fun onAnimationStart(p0: Animation?) {
                // No-op
            }
        })
    }

    private fun startAnimationFab(animation: Animation) {
        rlShare.startAnimation(animation)
        rlProfile.startAnimation(animation)
        rlCalendar.startAnimation(animation)
        rlSearch.startAnimation(animation)
        rlGroup.startAnimation(animation)
    }

    private fun visibilityAllChildView(visibilityState: Int) {
        rlShare.visibility = visibilityState
        rlProfile.visibility = visibilityState
        rlCalendar.visibility = visibilityState
        rlSearch.visibility = visibilityState
        rlGroup.visibility = visibilityState
    }

    private fun checkItemViewVisibility(): Boolean {
        return rlShare.visibility == View.INVISIBLE ||
                rlProfile.visibility == View.INVISIBLE ||
                rlCalendar.visibility == View.INVISIBLE ||
                rlSearch.visibility == View.INVISIBLE ||
                rlGroup.visibility == View.INVISIBLE
    }

    private fun setGoneOverLay() {
        frOverlay.visibility = View.GONE
    }


    /**
     * Interface menu click listener
     */
    interface OnMenuClickListener {
        /**
         * Event when Item button menu Click
         */
        fun eventItemMenuClicked(view: View)
    }
}

/**
 * Init custom view
 */
inline fun ViewManager.floatingButton(onMenuClickListener: FloatingMenuButton.OnMenuClickListener,
                                      init: FloatingMenuButton.() -> Unit):
        FloatingMenuButton = ankoView({ FloatingMenuButton(onMenuClickListener, it, null) },
        theme = 0, init = init)

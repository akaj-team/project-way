package vn.asiantech.way.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
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

    private lateinit var rlSearch: RelativeLayout
    private lateinit var imgBtnSearch: ImageButton
    private lateinit var rlGroup: RelativeLayout
    private lateinit var imgBtnGroup: ImageButton
    private lateinit var rlCalendar: RelativeLayout
    private lateinit var imgBtnCalendar: ImageButton
    private lateinit var rlProfile: RelativeLayout
    private lateinit var imgBtnProfile: ImageButton
    private lateinit var rlShare: RelativeLayout
    private lateinit var imgBtnShare: ImageButton
    private lateinit var imgBtnMenu: ImageButton

    init {
        AnkoContext.createDelegate(this).apply {
            verticalLayout {
                lparams(wrapContent, wrapContent)
                rlSearch = itemFloatingButton(R.id.floating_btn_menu_img_btn_search,
                        R.drawable.custom_bg_item_search_button,
                        R.drawable.ic_search, R.string.custom_floating_menu_search_title)
                imgBtnSearch = rlSearch.find(R.id.floating_btn_menu_img_btn_search)
                imgBtnSearch.onClick {
                    visibilityAllChildView(View.VISIBLE)
                    onMenuClickListener.onSearchClick()
                }
                rlGroup = itemFloatingButton(R.id.floating_btn_menu_img_btn_group,
                        R.drawable.custom_bg_item_group_button,
                        R.drawable.ic_group_white_24dp, R.string.custom_floating_menu_group_title)
                imgBtnGroup = rlGroup.find(R.id.floating_btn_menu_img_btn_group)
                imgBtnGroup.onClick {
                    visibilityAllChildView(View.VISIBLE)
                    onMenuClickListener.onGroupClick()
                }
                rlCalendar = itemFloatingButton(R.id.floating_btn_menu_img_btn_calendar,
                        R.drawable.custom_bg_item_calendar_button,
                        R.drawable.ic_calendar, R.string.custom_floating_menu_calendar_title)
                imgBtnCalendar = rlCalendar.find(R.id.floating_btn_menu_img_btn_calendar)
                imgBtnCalendar.onClick {
                    visibilityAllChildView(View.VISIBLE)
                    onMenuClickListener.onCalendarClick()
                }
                rlProfile = itemFloatingButton(R.id.floating_btn_menu_img_btn_profile,
                        R.drawable.custom_bg_item_profile_button,
                        R.drawable.ic_profile, R.string.custom_floating_menu_profile_title)
                imgBtnProfile = rlProfile.find(R.id.floating_btn_menu_img_btn_profile)
                imgBtnProfile.onClick {
                    visibilityAllChildView(View.VISIBLE)
                    onMenuClickListener.onProfileClick()
                }
                rlShare = itemFloatingButton(R.id.floating_btn_menu_img_btn_share,
                        R.drawable.custom_bg_item_share_button,
                        R.drawable.ic_share, R.string.custom_floating_menu_share_title)
                imgBtnShare = rlShare.find(R.id.floating_btn_menu_img_btn_share)
                imgBtnShare.onClick {
                    visibilityAllChildView(View.VISIBLE)
                    onMenuClickListener.onShareClick()
                }
                imgBtnMenu = imageButton {
                    imageResource = R.drawable.ic_menu
                    backgroundResource = R.drawable.custom_menu_button
                    onClick {
                        onMenuClick()
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
                }
            }
        }
    }

    /**
     * Collapse menu
     */
    fun collapseMenu() {
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
        if (rlShare.visibility == View.INVISIBLE) {
            startAnimationFab(animVisible)
            visibilityAllChildView(View.VISIBLE)
            onMenuClickListener.onMenuClick(true)
        } else {
            startAnimationFab(animInvisible)
            onMenuClickListener.onMenuClick(false)
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

    /**
     * Interface menu click listener
     */
    interface OnMenuClickListener {
        /**
         * Event when button menu clicked
         */
        fun onMenuClick(isShowMenu: Boolean)

        /**
         * Event when button share clicked
         */
        fun onShareClick()

        /**
         * Event when button profile clicked
         */
        fun onProfileClick()

        /**
         * Event when button calendar clicked
         */
        fun onCalendarClick()

        /**
         * Event when button search clicked
         */
        fun onSearchClick()

        /**
         * Event when button group clicked
         */
        fun onGroupClick()
    }
}

/**
 * Init custom view
 */
inline fun ViewManager.floatingButton(onMenuClickListener: FloatingMenuButton.OnMenuClickListener,
                                      init: FloatingMenuButton.() -> Unit):
        FloatingMenuButton = ankoView({ FloatingMenuButton(onMenuClickListener, it, null) },
        theme = 0, init = init)

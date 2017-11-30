package vn.asiantech.way.ui.custom

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
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.R

/**
 * Custom floating button menu
 * Created by haingoq on 29/09/2017.
 */
class FloatingButtonHorizontal(
        context: Context, attrs: AttributeSet? = null) :
        LinearLayout(context, attrs) {
    companion object {
        private const val ID_BTN_SEARCH = 1001
        private const val ID_BTN_GROUP = 1002
        private const val ID_BTN_CALENDAR = 1003
        private const val ID_BTN_PROFILE = 1004
        private const val ID_BTN_SHARE = 1005
    }

    private lateinit var rlSearch: RelativeLayout
    private lateinit var imgBtnSearch: ImageButton
    private lateinit var tvSearchTitle: TextView
    private lateinit var rlGroup: RelativeLayout
    private lateinit var imgBtnGroup: ImageButton
    private lateinit var tvGroupTitle: TextView
    private lateinit var rlCalendar: RelativeLayout
    private lateinit var imgBtnCalendar: ImageButton
    private lateinit var tvCalendarTitle: TextView
    private lateinit var rlProfile: RelativeLayout
    private lateinit var imgBtnProfile: ImageButton
    private lateinit var tvProfileTitle: TextView
    private lateinit var rlShare: RelativeLayout
    private lateinit var imgBtnShare: ImageButton
    private lateinit var tvShareTitle: TextView
    private lateinit var imgBtnMenu: ImageButton

    private var onMenuClickListener: OnMenuClickListener? = null

    init {
        AnkoContext.createDelegate(this).apply {
            verticalLayout {
                lparams(wrapContent, wrapContent)
                rlSearch = relativeLayout {
                    imgBtnSearch = imageButton {
                        id = ID_BTN_SEARCH
                        imageResource = R.drawable.ic_search
                        backgroundResource = R.drawable.custom_bg_item_search_button
                        onClick {
                            visibilityAllChildView(View.INVISIBLE)
                            onMenuClickListener?.onSearchClick()
                        }
                    }.lparams(dimen(R.dimen.width_height_image_button), dimen(R.dimen.width_height_image_button)) {
                        alignParentRight()
                        rightMargin = dimen(R.dimen.margin_right_image_button)
                    }
                    tvSearchTitle = textView(R.string.custom_floating_menu_search_title) {
                        backgroundResource = R.drawable.custom_bg_item_menu_title
                    }.lparams(wrapContent, dimen(R.dimen.height_text_view)) {
                        centerVertically()
                        rightMargin = dimen(R.dimen.margin_right_text_view)
                        leftOf(ID_BTN_SEARCH)
                        gravity = Gravity.CENTER
                    }
                }.lparams(dimen(R.dimen.width_relative_layout), wrapContent)
                rlGroup = relativeLayout {
                    imgBtnGroup = imageButton {
                        id = ID_BTN_GROUP
                        imageResource = R.drawable.ic_group_white_24dp
                        backgroundResource = R.drawable.custom_bg_item_search_button
                        onClick {
                            visibilityAllChildView(View.INVISIBLE)
                            onMenuClickListener?.onGroupClick()
                        }
                    }.lparams(dimen(R.dimen.width_height_image_button), dimen(R.dimen.width_height_image_button)) {
                        alignParentRight()
                        rightMargin = dimen(R.dimen.margin_right_image_button)
                    }
                    tvGroupTitle = textView(R.string.custom_floating_menu_group_title) {
                        backgroundResource = R.drawable.custom_bg_item_menu_title
                    }.lparams(wrapContent, dimen(R.dimen.height_text_view)) {
                        centerVertically()
                        rightMargin = dimen(R.dimen.margin_right_text_view)
                        leftOf(ID_BTN_GROUP)
                        gravity = Gravity.CENTER
                    }
                }.lparams(dimen(R.dimen.width_relative_layout), wrapContent)
                rlCalendar = relativeLayout {
                    imgBtnCalendar = imageButton {
                        id = ID_BTN_CALENDAR
                        backgroundResource = R.drawable.custom_bg_item_calendar_button
                        imageResource = R.drawable.ic_calendar
                        onClick {
                            visibilityAllChildView(View.INVISIBLE)
                            onMenuClickListener?.onCalendarClick()
                        }
                    }.lparams(dimen(R.dimen.width_height_image_button), dimen(R.dimen.width_height_image_button)) {
                        alignParentRight()
                        rightMargin = dimen(R.dimen.margin_right_image_button)
                    }
                    tvCalendarTitle = textView(R.string.custom_floating_menu_calendar_title) {

                        backgroundResource = R.drawable.custom_bg_item_menu_title
                    }.lparams(wrapContent, dimen(R.dimen.height_text_view)) {
                        centerVertically()
                        rightMargin = dimen(R.dimen.margin_right_text_view)
                        leftOf(ID_BTN_CALENDAR)
                        gravity = Gravity.CENTER
                    }
                }.lparams(dimen(R.dimen.width_relative_layout), wrapContent)
                rlProfile = relativeLayout {
                    imgBtnProfile = imageButton {
                        id = ID_BTN_PROFILE
                        imageResource = R.drawable.ic_profile
                        backgroundResource = R.drawable.custom_bg_item_profile_button
                        onClick {
                            visibilityAllChildView(View.INVISIBLE)
                            onMenuClickListener?.onProfileClick()
                        }
                    }.lparams(dimen(R.dimen.width_height_image_button), dimen(R.dimen.width_height_image_button)) {
                        alignParentRight()
                        rightMargin = dimen(R.dimen.margin_right_image_button)
                    }
                    tvProfileTitle = textView(R.string.custom_floating_menu_search_title) {
                        backgroundResource = R.drawable.custom_bg_item_menu_title
                    }.lparams(wrapContent, dimen(R.dimen.height_text_view)) {
                        centerVertically()
                        rightMargin = dimen(R.dimen.margin_right_text_view)
                        leftOf(ID_BTN_PROFILE)
                        gravity = Gravity.CENTER
                    }
                }.lparams(dimen(R.dimen.width_relative_layout), wrapContent)
                rlShare = relativeLayout {
                    imgBtnShare = imageButton {
                        id = ID_BTN_SHARE
                        imageResource = R.drawable.ic_share
                        backgroundResource = R.drawable.custom_bg_item_share_button
                        onClick {
                            visibilityAllChildView(View.INVISIBLE)
                            onMenuClickListener?.onShareClick()
                        }
                    }.lparams(dimen(R.dimen.width_height_image_button), dimen(R.dimen.width_height_image_button)) {
                        alignParentRight()
                        rightMargin = dimen(R.dimen.margin_right_image_button)
                    }
                    tvShareTitle = textView(R.string.custom_floating_menu_search_title) {
                        backgroundResource = R.drawable.custom_bg_item_menu_title
                    }.lparams(wrapContent, dimen(R.dimen.height_text_view)) {
                        centerVertically()
                        rightMargin = dimen(R.dimen.margin_right_text_view)
                        leftOf(ID_BTN_SHARE)
                        gravity = Gravity.CENTER
                    }
                }.lparams(dimen(R.dimen.width_relative_layout), wrapContent)
                imgBtnMenu = imageButton {
                    imageResource = R.drawable.ic_menu
                    backgroundResource = R.drawable.custom_menu_button
                    onClick {
                        onClickMenu()
                    }
                }.lparams(dimen(R.dimen.width_height_image_button_menu)
                        , dimen(R.dimen.width_height_image_button_menu)) {
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
                    is TextView -> {
                        view.leftPadding = dimen(R.dimen.padding_floating)
                        view.rightPadding = dimen(R.dimen.padding_floating)
                        view.textSize = px2dip(dimen(R.dimen.custom_menu_text_size))
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

    private fun onClickMenu() {
        val anim: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_rotate)
        val animVisible: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_visible)
        val animInvisible: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_invisible)
        imgBtnMenu.startAnimation(anim)
        if (rlShare.visibility == View.INVISIBLE) {
            startAnimationFab(animVisible)
            visibilityAllChildView(View.VISIBLE)
            onMenuClickListener?.onMenuClick(true)
        } else {
            startAnimationFab(animInvisible)
            onMenuClickListener?.onMenuClick(false)
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
     * Set listener for menu item
     * @param listener: OnMenuClickListener
     */
    internal fun setOnMenuItemClickListener(listener: OnMenuClickListener) {
        onMenuClickListener = listener
    }

    /**
     * Interface menu click listener
     */
    internal interface OnMenuClickListener {
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
inline fun ViewManager.floatingButton(init: FloatingButtonHorizontal.() -> Unit):
        FloatingButtonHorizontal = ankoView({ FloatingButtonHorizontal(it, null) }
        , theme = 0, init = init)

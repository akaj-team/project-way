package vn.asiantech.way.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.custom_floating_button_menu_horizontal.view.*
import vn.asiantech.way.R

/**
 * Custom floating button menu
 * Created by haingoq on 29/09/2017.
 */
class FloatingButtonHorizontal @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null) :
        LinearLayout(context, attrs), View.OnClickListener {
    init {
        inflate(context, R.layout.custom_floating_button_menu_horizontal, this)
        imgBtnMenu.setOnClickListener(this)
        imgBtnShare.setOnClickListener(this)
        imgBtnProfile.setOnClickListener(this)
        imgBtnCalendar.setOnClickListener(this)
        imgBtnSearch.setOnClickListener(this)
        imgBtnGroup.setOnClickListener(this)
    }

    private var mOnMenuClickListener: OnMenuClickListener? = null

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgBtnMenu -> {
                val anim: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_rotate)
                val animVisible: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_visible)
                val animInvisible: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_invisible)
                imgBtnMenu.startAnimation(anim)
                if (rlShare.visibility == View.INVISIBLE) {
                    startAnimationFab(animVisible)
                    visibilityAllChildView(View.VISIBLE)
                    mOnMenuClickListener?.onMenuClick(true)
                } else {
                    startAnimationFab(animInvisible)
                    mOnMenuClickListener?.onMenuClick(false)
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
            R.id.imgBtnShare -> {
                visibilityAllChildView(View.INVISIBLE)
                mOnMenuClickListener?.onShareClick()
            }
            R.id.imgBtnProfile -> {
                visibilityAllChildView(View.INVISIBLE)
                mOnMenuClickListener?.onProfileClick()
            }
            R.id.imgBtnCalendar -> {
                visibilityAllChildView(View.INVISIBLE)
                mOnMenuClickListener?.onCalendarClick()
            }
            R.id.imgBtnSearch -> {
                visibilityAllChildView(View.INVISIBLE)
                mOnMenuClickListener?.onSearchClick()
            }
            R.id.imgBtnGroup -> {
                visibilityAllChildView(View.INVISIBLE)
                mOnMenuClickListener?.onGroupClick()
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
        mOnMenuClickListener = listener
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

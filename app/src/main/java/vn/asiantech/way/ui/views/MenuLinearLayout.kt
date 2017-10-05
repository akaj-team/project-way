package vn.asiantech.way.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.custom_menu.view.*
import vn.asiantech.way.R

/**
 * Custom floating button menu
 * Created by haingoq on 29/09/2017.
 */
class MenuLinearLayout(context: Context, attrs: AttributeSet) :
        LinearLayout(context, attrs), View.OnClickListener {
    private var mOnMenuClickListener: OnMenuClickListener? = null

    init {
        inflate(context, R.layout.custom_menu, this)
        imgBtnMenu.setOnClickListener(this)
        imgBtnShare.setOnClickListener(this)
        imgBtnProfile.setOnClickListener(this)
        imgBtnCalendar.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgBtnMenu -> {
                val anim: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_rotate)
                val animVisible: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_visible)
                val animInvisible: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_invisible)
                imgBtnMenu.startAnimation(anim)
                startAnimationFab(animInvisible)
                if (rlShare.visibility == View.INVISIBLE) {
                    startAnimationFab(animVisible)
                    visibilityAllChildView(View.VISIBLE)
                }
                animInvisible.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(p0: Animation?) {
                    }

                    override fun onAnimationEnd(p0: Animation?) {
                        visibilityAllChildView(View.INVISIBLE)
                    }

                    override fun onAnimationStart(p0: Animation?) {
                    }
                })
            }
            R.id.imgBtnShare -> {
                tvShare.visibility = View.VISIBLE
                mOnMenuClickListener?.onShareClick()
            }
            R.id.imgBtnProfile -> {
                tvProfile.visibility = View.VISIBLE
                mOnMenuClickListener?.onProfileClick()
            }
            R.id.imgBtnCalendar -> {
                tvCalendar.visibility = View.VISIBLE
                mOnMenuClickListener?.onCalendarClick()
            }
        }
    }

    private fun startAnimationFab(animation: Animation) {
        rlShare.startAnimation(animation)
        rlProfile.startAnimation(animation)
        rlCalendar.startAnimation(animation)
    }

    private fun visibilityAllChildView(visibilityState: Int) {
        rlShare.visibility = visibilityState
        rlProfile.visibility = visibilityState
        rlCalendar.visibility = visibilityState
    }

    /**
     * Set listener for menu item
     * @param listener: OnMenuClickListener
     */
    fun setOnMenuItemClickListener(listener: OnMenuClickListener) {
        mOnMenuClickListener = listener
    }

    interface OnMenuClickListener {
        fun onShareClick()
        fun onProfileClick()
        fun onCalendarClick()
    }
}

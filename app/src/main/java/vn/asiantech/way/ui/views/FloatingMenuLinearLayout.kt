package vn.asiantech.way.ui.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
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
class FloatingMenuLinearLayout : LinearLayout, View.OnClickListener, View.OnTouchListener {
    constructor(context: Context) : super(context) {
        initView()
    }

    constructor (context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.fabMenu -> {
                val anim: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_rotate)
                val animVisible: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_visible)
                val animInvisible: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_invisible)
                fabMenu.startAnimation(anim)
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
            R.id.fabShare -> {
                // TODO Share location
            }
            R.id.fabProfile -> {
                // TODO Update profile
            }
            R.id.fabCalendar -> {
                // TODO Show calendar
            }
        }
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            when (view?.id) {
                R.id.fabShare -> tvShare.visibility = View.VISIBLE
                R.id.fabProfile -> tvProfile.visibility = View.VISIBLE
                R.id.fabCalendar -> tvCalendar.visibility = View.VISIBLE
            }
            return true
        } else if (event?.action == MotionEvent.ACTION_UP) {
            when (view?.id) {
                R.id.fabShare -> tvShare.visibility = View.GONE
                R.id.fabProfile -> tvProfile.visibility = View.GONE
                R.id.fabCalendar -> tvCalendar.visibility = View.GONE
            }
            return true
        }
        return false
    }

    private fun startAnimationFab(animation: Animation) {
        fabShare.startAnimation(animation)
        fabProfile.startAnimation(animation)
        fabCalendar.startAnimation(animation)
    }

    private fun initView() {
        inflate(context, R.layout.custom_menu, this)
        fabShare.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
        fabProfile.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
        fabCalendar.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
        fabMenu.setOnClickListener(this)
        fabShare.setOnClickListener(this)
        fabProfile.setOnClickListener(this)
        fabCalendar.setOnClickListener(this)
        fabShare.setOnTouchListener(this)
        fabProfile.setOnTouchListener(this)
        fabCalendar.setOnTouchListener(this)
    }

    private fun visibilityAllChildView(visibilityState: Int) {
        rlShare.visibility = visibilityState
        rlProfile.visibility = visibilityState
        rlCalendar.visibility = visibilityState
    }
}

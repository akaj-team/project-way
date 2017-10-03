package vn.asiantech.way.ui.view

import android.content.Context
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
class FloatingButtonMenuLinearLayout : LinearLayout, View.OnClickListener, View.OnTouchListener {
    constructor(context: Context) : super(context) {
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
        }
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        when (view?.id) {
            R.id.fabShare -> {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    tvShare.visibility = View.VISIBLE
                } else if (event?.action == MotionEvent.ACTION_UP) {
                    tvShare.visibility = View.GONE
                }
                return true
            }
            R.id.fabProfile -> {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    tvProfile.visibility = View.VISIBLE
                } else if (event?.action == MotionEvent.ACTION_UP) {
                    tvProfile.visibility = View.GONE
                }
                return true
            }
            R.id.fabCalendar -> {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    tvCalendar.visibility = View.VISIBLE
                } else if (event?.action == MotionEvent.ACTION_UP) {
                    tvCalendar.visibility = View.GONE
                }
                return true
            }
        }
        return false
    }

    private fun startAnimationFab(animation: Animation) {
        fabShare.startAnimation(animation)
        fabProfile.startAnimation(animation)
        fabCalendar.startAnimation(animation)
    }

    private fun initView() {
        View.inflate(context, R.layout.custom_menu, this)
        fabMenu.setOnClickListener(this)
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

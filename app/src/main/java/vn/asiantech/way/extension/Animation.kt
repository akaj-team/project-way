package vn.asiantech.way.extension

import android.view.animation.Animation

/**
 * Animation.
 *
 * @author at-ToanNguyen
 */
fun Animation.setAnimation(onAnimationEnd: (Animation?) -> Unit = {}) {
    setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(p0: Animation?) = Unit

        override fun onAnimationEnd(p0: Animation?) {
            onAnimationEnd.invoke(p0)
        }

        override fun onAnimationStart(p0: Animation?) = Unit
    })
}

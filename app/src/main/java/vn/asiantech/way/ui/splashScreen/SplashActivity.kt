package vn.asiantech.way.ui.splashScreen

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.LinearInterpolator
import kotlinx.android.synthetic.main.activity_splash.*
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseActivity

/**
* Created by atHangTran on 22/09/2017.
*/

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setAnimationForBackground()
    }

    private fun setAnimationForBackground() {
        val animator = ValueAnimator.ofFloat(0.0f, 1.0f)
        animator.repeatCount = ValueAnimator.INFINITE
        animator.interpolator = LinearInterpolator()
        animator.duration = 10000L
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Float
            val width = imgBackground1.getWidth()
            val translationX = width * progress
            imgBackground1.translationX = translationX
            imgBackground2.translationX = translationX - width
            animator.start()
            setAnimationForBackground()
        }
    }
}

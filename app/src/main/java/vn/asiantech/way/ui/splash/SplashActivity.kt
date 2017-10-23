package vn.asiantech.way.ui.splash

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import com.hypertrack.lib.HyperTrack
import com.hypertrack.lib.HyperTrackUtils
import kotlinx.android.synthetic.main.activity_splash.*
import vn.asiantech.way.BR
import vn.asiantech.way.R
import vn.asiantech.way.databinding.ActivitySplashBinding
import vn.asiantech.way.extension.toast
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.register.RegisterActivity
import javax.inject.Inject

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by atHangTran on 26/09/2017.
 */

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(), SplashNavigator {

    private var mSplashBinding: ActivitySplashBinding? = null

    @Inject lateinit var mSplashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSplashBinding = getViewDataBinding()
        mSplashViewModel.navigator
        setAnimationForBackground()
        setScaleForCircle()
    }

    private fun setAnimationForBackground() {
        val transAnim = ValueAnimator.ofFloat(0.0f, 1.0f)
        transAnim.repeatCount = ValueAnimator.INFINITE
        transAnim.interpolator = LinearInterpolator()
        transAnim.duration = 10000L
        transAnim.addUpdateListener { animation ->
            val progress = animation.animatedValue as Float
            val width = imgFrontBackground.width
            val translationX = width * progress
            imgFrontBackground.translationX = translationX
            imgBehindBackground.translationX = translationX - width
        }
        transAnim.start()
    }

    private fun setScaleForCircle() {
        val growAnim = ScaleAnimation(0.9f, 1.05f, 0.9f, 1.05f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
        val shrinkAnim = ScaleAnimation(1.05f, 0.9f, 1.05f, 0.9f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)

        growAnim.duration = 800
        shrinkAnim.duration = 800

        imgCircle.animation = growAnim
        growAnim.start()

        growAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                imgCircle.animation = shrinkAnim
                shrinkAnim.start()
            }
        })

        shrinkAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                imgCircle.animation = growAnim
                growAnim.start()
            }
        })
    }

    override fun switchRegisterScreen() {
        btnEnableLocation.setOnClickListener {
            if (HyperTrackUtils.isInternetConnected(this)) {
                if (HyperTrackUtils.isLocationEnabled(this)) {
                    progressBar.visibility = View.VISIBLE
                    btnEnableLocation.visibility = View.GONE
                    tvAppDescription.visibility = View.GONE
                    val intent = Intent(this, RegisterActivity::class.java)
                    intent.putExtra(RegisterActivity.INTENT_REGISTER, RegisterActivity.INTENT_CODE_SPLASH)
                    startActivity(intent)
                } else {
                    if (!HyperTrack.checkLocationPermission(this)) {
                        HyperTrack.requestPermissions(this)
                    }

                    if (!HyperTrack.checkLocationServices(this)) {
                        HyperTrack.requestLocationServices(this)
                    }
                }
            } else {
                toast(getString(R.string.splash_toast_turn_on_wifi))
            }
        }
    }

    override val bindingVariable: Int
        get() = BR.splashViewModel

    override val viewModel: SplashViewModel
        get() = mSplashViewModel

    override val layoutId: Int
        get() = R.layout.activity_splash
}

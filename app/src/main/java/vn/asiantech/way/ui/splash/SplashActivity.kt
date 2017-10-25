package vn.asiantech.way.ui.splash

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import com.hypertrack.lib.HyperTrack
import com.hypertrack.lib.HyperTrackUtils
import kotlinx.android.synthetic.main.activity_splash.*
import vn.asiantech.way.R
import vn.asiantech.way.extension.toast
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.home.HomeActivity
import vn.asiantech.way.ui.register.RegisterActivity

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by atHangTran on 26/09/2017.
 */
class SplashActivity : BaseActivity() {

    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mSharedPreferences = getSharedPreferences(RegisterActivity.SHARED_NAME, Context.MODE_PRIVATE)
        if (HyperTrackUtils.isInternetConnected(this)) {
            if (HyperTrackUtils.isLocationEnabled(this)) {
                if (mSharedPreferences.getBoolean(RegisterActivity.KEY_LOGIN, false)) {
                    startActivity(Intent(this, HomeActivity::class.java))
                }
            }
        } else {
            toast(getString(R.string.splash_toast_turn_on_wifi))
        }
        setAnimationForBackground()
        setScaleForCircle()
        requestPermission()
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

    private fun requestPermission() {
        btnEnableLocation.setOnClickListener {
            if (HyperTrackUtils.isInternetConnected(this)) {
                if (HyperTrackUtils.isLocationEnabled(this)) {
                    progressBar.visibility = View.VISIBLE
                    btnEnableLocation.visibility = View.GONE
                    tvAppDescription.visibility = View.GONE
                    if (mSharedPreferences.getBoolean(RegisterActivity.KEY_LOGIN, false)) {
                        startActivity(Intent(this, HomeActivity::class.java))
                    } else {
                        val intent = Intent(this, RegisterActivity::class.java)
                        intent.putExtra(RegisterActivity.INTENT_REGISTER, RegisterActivity.INTENT_CODE_SPLASH)
                        startActivity(intent)
                    }
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
}

package vn.asiantech.way.ui.splash

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import com.hypertrack.lib.HyperTrack
import com.hypertrack.lib.HyperTrackUtils
import org.jetbrains.anko.setContentView
import vn.asiantech.way.R
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.extension.toast
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.home.HomeActivity
import vn.asiantech.way.ui.register.RegisterActivity

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by atHangTran on 26/09/2017.
 */
class SplashActivity : BaseActivity() {
    companion object {
        private const val ANIMATION_DURATION = 10000L
    }

    private lateinit var ui: SplashActivityUI
    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = SplashActivityUI()
        ui.setContentView(this)
        splashViewModel = SplashViewModel(this)
        setAnimationForBackground()
        requestPermission()
        splashViewModel.setScaleForCircle()
    }

    override fun onBindViewModel() {
        splashViewModel.registerBroadcastReceiver(this)
        addDisposables(
                splashViewModel.checkPermissionSubject
                        .observeOnUiThread().subscribe(this::handleScreen),
                splashViewModel.setAnimation
                        .observeOnUiThread().subscribe(this::handleAnimation))
    }

    internal fun eventSetPermission() {
        if (!HyperTrackUtils.isWifiEnabled(this)) {
            toast(getString(R.string.splash_toast_turn_on_wifi))
        } else {
            if (!HyperTrackUtils.isLocationEnabled(this)) {
                if (!HyperTrack.checkLocationPermission(this)) {
                    HyperTrack.requestPermissions(this)
                }
                if (!HyperTrack.checkLocationServices(this)) {
                    HyperTrack.requestLocationServices(this)
                }
            } else {
                if (!HyperTrackUtils.isLocationPermissionAvailable(this)) {
                    if (!HyperTrack.checkLocationPermission(this)) {
                        HyperTrack.requestPermissions(this)
                    }
                }
            }
        }
    }

    private fun handleAnimationForBackground(pair: Pair<Float, Float>) {
        ui.imgFrontBackground.translationX = pair.first
        ui.imgBehindBackground.translationX = pair.second
    }

    private fun handleScreen(isTrue: Boolean) {
        ui.progressBar.visibility = View.VISIBLE
        ui.btnEnableLocation.visibility = View.GONE
        ui.tvAppDescription.visibility = View.GONE
        if (isTrue) {
            addDisposables(splashViewModel.loginStatus
                    .observeOnUiThread().subscribe(this::handleIntentFlow))
        }
    }

    private fun handleIntentFlow(isLogin: Boolean) {
        if (isLogin) {
            Handler().postDelayed({
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }, SplashViewModel.DELAY)
        } else {
            Handler().postDelayed({
                val intent = Intent(this, RegisterActivity::class.java)
                intent.putExtra(RegisterActivity.KEY_FROM_REGISTER, RegisterActivity.REQUEST_REGISTER)
                startActivity(intent)
                finish()
            }, SplashViewModel.DELAY)
        }
    }

    private fun setAnimationForBackground() {
        val transAnim = ValueAnimator.ofFloat(0.0f, 1.0f)
        transAnim.repeatCount = ValueAnimator.INFINITE
        transAnim.interpolator = LinearInterpolator()
        transAnim.duration = ANIMATION_DURATION
        transAnim.addUpdateListener { animation ->
            val width = ui.imgFrontBackground.width
            addDisposables(
                    splashViewModel.setAnimationForBackground(animation, width)
                            .observeOnUiThread().subscribe(this::handleAnimationForBackground))
        }
        transAnim.start()
    }

    private fun handleAnimation(animation: Animation) {
        ui.imgCircle.animation = animation
        animation.start()
    }

    private fun requestPermission() {
        addDisposables(splashViewModel.checkPermissionSubject
                .observeOnUiThread().subscribe(this::handleScreen))
    }

    override fun onDestroy() {
        unregisterReceiver(splashViewModel.mBroadcastReceiver)
        super.onDestroy()
    }
}

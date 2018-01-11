package vn.asiantech.way.ui.splash

import android.animation.ValueAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.hypertrack.lib.HyperTrackUtils
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.SingleSubject
import vn.asiantech.way.data.source.LocalRepository

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 10/01/2018.
 */
class SplashViewModel(private val assetRepository: LocalRepository) {

    companion object {
        const val DELAY = 3000L
        private const val ANIMATION_DURATION = 800L
    }

    lateinit var context: Context
    internal val checkPermissionSubject = BehaviorSubject.create<Boolean>()
    internal val loginStatus = BehaviorSubject.create<Boolean>()
    internal val setAnimation = BehaviorSubject.create<Animation>()

    constructor(context: Context) : this(LocalRepository(context)) {
        this.context = context
    }

    internal fun registerBroadcastReceiver(context: Context) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        intentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
        context.registerReceiver(mBroadcastReceiver, intentFilter)
    }

    internal val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if ((intent?.action == WifiManager.WIFI_STATE_CHANGED_ACTION ||
                    intent?.action == WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                startSwitchScreen()
            }

            if (intent?.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
                startSwitchScreen()
            }
        }
    }

    private fun startSwitchScreen() {
        if (HyperTrackUtils.isInternetConnected(context)) {
            if (HyperTrackUtils.isLocationEnabled(context)) {
                if (HyperTrackUtils.isLocationPermissionAvailable(context)) {
                    checkPermissionSubject.onNext(true)
                    if (assetRepository.getLoginStatus()) {
                        loginStatus.onNext(true)
                    } else {
                        loginStatus.onNext(false)
                    }
                }
            }
        }
    }

    internal fun setScaleForCircle() {
        val growAnim = ScaleAnimation(0.9f, 1.05f, 0.9f, 1.05f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
        val shrinkAnim = ScaleAnimation(1.05f, 0.9f, 1.05f, 0.9f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)

        growAnim.duration = ANIMATION_DURATION
        shrinkAnim.duration = ANIMATION_DURATION

        setAnimation.onNext(growAnim)

        growAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                setAnimation.onNext(shrinkAnim)
            }
        })

        shrinkAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                setAnimation.onNext(growAnim)
            }
        })
    }

    internal fun setAnimationForBackground(animation: ValueAnimator, imgWidth: Int):
            Single<Pair<Float, Float>> {
        val setBackgroundAnimation = SingleSubject.create<Pair<Float, Float>>()
        val progress = animation.animatedValue as Float
        val translationX = imgWidth * progress
        val result = Pair(translationX, translationX - imgWidth)
        setBackgroundAnimation.onSuccess(result)
        return setBackgroundAnimation
    }
}

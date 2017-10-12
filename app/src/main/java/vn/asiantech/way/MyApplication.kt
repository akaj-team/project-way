package vn.asiantech.way

import android.app.Activity
import android.app.Application
import com.hypertrack.lib.HyperTrack
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import vn.asiantech.way.di.component.DaggerAppComponent
import javax.inject.Inject

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 9/21/17.
 */

class MyApplication : Application(), HasActivityInjector {

    @Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize HyperTrack SDK
        HyperTrack.initialize(this.applicationContext, BuildConfig.HYPERTRACK_PK)
        HyperTrack.enableMockLocations(true)
        HyperTrack.disablePersistentNotification(true)

        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
    }
}

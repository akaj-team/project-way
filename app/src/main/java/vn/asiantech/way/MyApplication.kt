package vn.asiantech.way

import android.app.Application
import com.hypertrack.lib.HyperTrack

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 9/21/17
 */

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize HyperTrack SDK
        HyperTrack.initialize(this.applicationContext, BuildConfig.HYPERTRACK_PK)
        HyperTrack.enableMockLocations(true)
        HyperTrack.disablePersistentNotification(true)
    }
}

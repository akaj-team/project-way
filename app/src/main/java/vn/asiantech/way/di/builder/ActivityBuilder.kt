package vn.asiantech.way.di.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vn.asiantech.way.ui.splash.SplashActivity
import vn.asiantech.way.ui.splash.SplashActivityModule

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 9/28/17.
 */

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = arrayOf(SplashActivityModule::class))
    abstract fun bindSplashActivity(): SplashActivity
}

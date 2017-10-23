package vn.asiantech.way.ui.splash

import dagger.Module
import dagger.Provides
import vn.asiantech.way.utils.rx.SchedulerProvider

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 10/10/17.
 */

@Module
class SplashActivityModule {

    @Provides
    internal fun provideSplashViewModel(schedulerProvider: SchedulerProvider): SplashViewModel {
        return SplashViewModel(schedulerProvider)
    }

}

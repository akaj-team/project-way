package vn.asiantech.way.ui.splash

import vn.asiantech.way.ui.base.BaseViewModel
import vn.asiantech.way.utils.rx.SchedulerProvider

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 9/29/17.
 */

class SplashViewModel(schedulerProvider: SchedulerProvider) : BaseViewModel<SplashNavigator>(schedulerProvider) {

    fun onBtnEnableLocationClick() {
        setIsLoading(true)
        navigator?.switchRegisterScreen()
    }

}

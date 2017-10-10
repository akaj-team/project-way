package vn.asiantech.way.ui.splash

import vn.asiantech.way.ui.base.BaseViewModel

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 9/29/17.
 */

class SplashViewModel : BaseViewModel<SplashNavigator>() {

    fun onBtnEnableLocationClick() {
        setIsLoading(true)
        navigator?.switchRegisterScreen()
    }

}

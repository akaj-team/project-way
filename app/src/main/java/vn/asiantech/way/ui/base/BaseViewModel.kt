package vn.asiantech.way.ui.base

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import io.reactivex.disposables.CompositeDisposable

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 10/8/17.
 */

abstract class BaseViewModel<N> : ViewModel() {

    var navigator: N? = null
    val isLoading = ObservableBoolean(false)
    var compositeDisposable: CompositeDisposable? = null
        private set

    fun onViewCreated() {
        this.compositeDisposable = CompositeDisposable()
    }

    fun onDestroyView() {
        compositeDisposable?.dispose()
    }

    fun setIsLoading(isLoading: Boolean) {
        this.isLoading.set(isLoading)
    }
}

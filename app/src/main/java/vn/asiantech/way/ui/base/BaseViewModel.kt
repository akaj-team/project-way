package vn.asiantech.way.ui.base

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import io.reactivex.disposables.CompositeDisposable
import vn.asiantech.way.utils.rx.SchedulerProvider

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 10/8/17.
 */

abstract class BaseViewModel<N>(val schedulerProvider: SchedulerProvider) : ViewModel() {

    var navigator: N? = null
    private val isLoading = ObservableBoolean(false)
    private var compositeDisposable: CompositeDisposable? = null

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

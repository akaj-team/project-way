package vn.asiantech.way.ui.base

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 9/21/17.
 */
abstract class BaseFragment : Fragment() {
    private val subscription: CompositeDisposable = CompositeDisposable()

    override fun onPause() {
        super.onPause()
        subscription.clear()
    }

    override fun onResume() {
        super.onResume()
        onBindViewModel()
    }

    protected fun addDisposables(vararg ds: Disposable) {
        ds.forEach { subscription.add(it) }
    }

    internal fun sendBroadCast(action: String, bundle: Bundle? = null) {
        val intent = Intent(action)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        activity.sendBroadcast(intent)
    }

    /**
     * This function is used to define subscription
     */
    abstract fun onBindViewModel()
}

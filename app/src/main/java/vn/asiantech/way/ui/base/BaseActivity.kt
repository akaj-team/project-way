package vn.asiantech.way.ui.base

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 9/21/17.
 */
abstract class BaseActivity : AppCompatActivity() {
    companion object {
        const val SHARED_NAME = "shared"
        const val KEY_LOGIN = "login"
        const val KEY_LOCAL_USER = "local_user"
    }

    private val subscription: CompositeDisposable = CompositeDisposable()
    lateinit protected var prefs: SharedPreferences

    protected fun addDisposables(vararg ds: Disposable) {
        subscription.addAll(*ds)
    }

    override fun onPause() {
        super.onPause()
        subscription.clear()
    }

    override fun onResume() {
        super.onResume()
        onBindViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
    }

    abstract fun onBindViewModel()
}

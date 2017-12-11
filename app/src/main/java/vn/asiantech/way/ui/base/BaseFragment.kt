package vn.asiantech.way.ui.base

import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 9/21/17.
 */
abstract class BaseFragment : Fragment() {
    private val subscription: CompositeDisposable = CompositeDisposable()
    private val progressDialog by lazy {
        indeterminateProgressDialog(
                getString(R.string.progress_dialog_message),
                getString(R.string.progress_dialog_title)
        )
    }

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

    protected fun setProgressDialog(message: String, title: String) {
        progressDialog.setMessage(message)
        progressDialog.setTitle(title)
    }

    protected fun showProgressDialog() {
        progressDialog.show()
    }

    protected fun hideProgressDialog() {
        progressDialog.hide()
    }

    /**
     * This function is used to define subscription
     */
    abstract fun onBindViewModel()
}

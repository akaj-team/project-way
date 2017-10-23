package vn.asiantech.way.ui.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatActivity

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 10/23/17.
 */
abstract class CoreActivity<T : ViewDataBinding, out V : BaseViewModel<*>> : AppCompatActivity() {

    var mViewDataBinding: T? = null
        private set
    private var mViewModel: V? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        mViewModel?.onViewCreated()
    }

    private fun performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView<T>(this, layoutId)
        this.mViewModel = if (mViewModel == null) viewModel else mViewModel
        (mViewDataBinding as T).setVariable(bindingVariable, mViewModel)
        (mViewDataBinding as T).executePendingBindings()
    }

    override fun onDestroy() {
        mViewModel?.onDestroyView()
        super.onDestroy()
    }

    fun getViewDataBinding(): T {
        return mViewDataBinding as T
    }

    /**
     * Override for set view model

     * @return view model instance
     */
    abstract val viewModel: V

    /**
     * Override for set binding variable

     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int
}

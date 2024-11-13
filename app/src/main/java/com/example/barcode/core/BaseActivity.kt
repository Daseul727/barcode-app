package com.example.barcode.core

import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LifecycleOwner
import javax.inject.Inject

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel> : BaseLegacyActivity<T>() {

    fun getBindingVariable(): Int = BR.viewModel
    @Inject
    lateinit var mViewModel: V
    fun getViewModel(): V {
        return mViewModel
    }

    abstract fun getLifecycleOwner(): LifecycleOwner
    abstract fun registerObservers()
    abstract fun unregisterObservers()

    override fun onResume() {
        super.onResume()
        registerObservers()
    }

    override fun onPause() {
        super.onPause()
        unregisterObservers()
    }
}
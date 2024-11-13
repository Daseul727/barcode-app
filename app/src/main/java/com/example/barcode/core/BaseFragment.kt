package com.example.barcode.core

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LifecycleOwner
import javax.inject.Inject

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : BaseLegacyFragment<T>() {

    fun getBindingVariable(): Int = BR.viewModel

    // Fragment 에서 Activity 에 전달
    interface Callback {
        fun onFragmentAttached(fragment: BaseFragment<*,*>)
        fun onFragmentResume(fragment: BaseFragment<*,*>)
        fun onFragmentDetached(fragment: BaseFragment<*,*>)
    }

    @Inject
    lateinit var mViewModel: V
    fun getViewModel(): V {
        return mViewModel
    }

    abstract fun getLifecycleOwner(): LifecycleOwner
    abstract fun registerObservers()
    abstract fun unregisterObservers()

    lateinit var mContext: Context
    private var mActivity: BaseLegacyActivity<*>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = (context as? BaseLegacyActivity<*>)
    }

    override fun onDetach() {
        mActivity?.onFragmentDetached(this)
        mActivity = null
        super.onDetach()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = requireContext()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        registerObservers()
        mActivity?.onFragmentResume(this)
    }

}
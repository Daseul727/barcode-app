package com.example.barcode.ui

import androidx.lifecycle.LifecycleOwner
import com.example.barcode.core.BaseActivity
import com.example.barcode.core.BaseFragment
import com.example.barcode.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override fun getLifecycleOwner(): LifecycleOwner = this
    override fun registerObservers() {}
    override fun unregisterObservers() {}

    override fun getViewDataBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }


    override fun initView() {

    }

    override fun onFragmentAttached(fragment: BaseFragment<*, *>) {
        //
    }

    override fun onFragmentResume(fragment: BaseFragment<*, *>) {
        //
    }

    override fun onFragmentDetached(fragment: BaseFragment<*, *>) {
        //
    }
}
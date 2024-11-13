package com.example.barcode.core

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding

abstract class BaseLegacyActivity<T : ViewDataBinding> : AppCompatActivity(), BaseFragment.Callback {
    var mProgressDialog: Dialog? = null

    lateinit var binding: T
    abstract fun getViewDataBinding(): T
    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewDataBinding()
        initView()
    }

    private fun initViewDataBinding() {
        this.binding = getViewDataBinding()
        setContentView(binding.root)
    }

}
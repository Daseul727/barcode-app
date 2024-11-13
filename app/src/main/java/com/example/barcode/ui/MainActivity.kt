package com.example.barcode.ui

import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.example.barcode.core.BaseActivity
import com.example.barcode.core.BaseFragment
import com.example.barcode.core.extension.setSafeOnClickListener
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
        setQrViewBinding()
    }

    private fun setQrViewBinding() {
        binding.incScanInfo.barcodeEdt.setOnSearchListener { keyword ->
            doSearchBarcode(keyword)
        }

        binding.incScanInfo.barcodeEdt.setOnEditorActionListener { textView, action, _ ->
            if (action == EditorInfo.IME_ACTION_SEARCH) {
                doSearchBarcode(textView.text.toString())
            }
            true
        }

        binding.incScanInfo.btnQr.setSafeOnClickListener {
//            val fragment = BarcodeFragment()
//            fragment.menuItem = menuItem
//            addFragment(R.id.drawer_container, fragment, BarcodeFragment.TAG)
            setBarcodeScanning()
        }
    }

    private fun doSearchBarcode(keyword: String) {
        Toast.makeText(this,"keyword : $keyword", Toast.LENGTH_SHORT)
    }

    private fun setBarcodeScanning() {

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
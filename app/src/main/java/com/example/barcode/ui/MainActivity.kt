package com.example.barcode.ui

import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LifecycleOwner
import com.example.barcode.core.BaseActivity
import com.example.barcode.core.BaseFragment
import com.example.barcode.core.extension.setSafeOnClickListener
import com.example.barcode.databinding.ActivityMainBinding
import com.example.barcode.ui.base.BarcodeManager
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    // 사용자 생성 Seq 넘버
    private var tempSeqNo = ""

    enum class ScreenState { Scan, SeqScan, Info }

    private var screenState: ScreenState = ScreenState.Scan
        set(value) {
            when (value) { // 상태에 맞는 화면을 그려 준다.
                ScreenState.Scan -> setScanView()
                ScreenState.SeqScan -> setSeqScanView()
                ScreenState.Info -> setInfoView()
            }
            field = value
        }


    override fun getLifecycleOwner(): LifecycleOwner = this
    override fun registerObservers() {}
    override fun unregisterObservers() {}
    override fun onFragmentAttached(fragment: BaseFragment<*, *>) {}
    override fun onFragmentResume(fragment: BaseFragment<*, *>) {}
    override fun onFragmentDetached(fragment: BaseFragment<*, *>) {}
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
        BarcodeManager.with(this).onGmsBarcodeScanning(barLauncher, object : BarcodeManager.CallbackGms {
            override fun onGmsBarcode(txt: String) {
                scanResultLogic(txt)
            }
        })
    }

    // Zxing 통한 바코드 스캔
    private val barLauncher: ActivityResultLauncher<ScanOptions> = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        scanResultLogic(result.contents?:"")
    }
    private fun scanResultLogic(txt: String) {
        when (screenState) {
            ScreenState.Scan -> {
                doSearchBarcode(txt.trim())
            }
            ScreenState.SeqScan -> {
                tempSeqNo = txt.trim()
                screenState = ScreenState.Info
            }
            ScreenState.Info -> {
                //NONE
            }
        }
    }




    private fun setScanView() {

    }

    private fun setSeqScanView() {

    }

    private fun setInfoView() {

    }
}
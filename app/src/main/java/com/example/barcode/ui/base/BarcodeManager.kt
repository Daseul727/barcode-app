package com.example.barcode.ui.base

import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import com.google.mlkit.vision.barcode.common.Barcode
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanOptions
import com.orhanobut.logger.Logger

class BarcodeManager {
    companion object {
        private var instance: BarcodeManager? = null
        private lateinit var context: Context

        const val GMS_SCAN_FAIL = "GmsScanFail"

        fun with(_context: Context): BarcodeManager {
            return instance ?: synchronized(this) {
                instance ?: BarcodeManager().also {
                    context = _context
                    instance = it
                }
            }
        }
    }

    private var cbListener: ClipboardManager.OnPrimaryClipChangedListener ? = null

    private var onMessageCallback: Callback? = null
    interface Callback {
        fun onMessage(txt: String)
    }

    private var onGmsCallback: CallbackGms? = null
    interface CallbackGms {
        fun onGmsBarcode(txt: String)
    }



    fun onGmsBarcodeScanning(barLauncher: ActivityResultLauncher<ScanOptions>, callback: CallbackGms?) {
        this.onGmsCallback = callback

        val scanner = com.google.mlkit.vision.codescanner.GmsBarcodeScanning.getClient(context)
        scanner.startScan()
            .addOnSuccessListener { barcode: Barcode ->
                onGmsCallback?.onGmsBarcode(barcode.rawValue.toString())
                onGmsCallback = null
            }
            .addOnFailureListener { e: Exception ->
                // ML을 사용할 수 없는경우, zxing을 사용한다.
                onGmsCallback = null

                // zxing CaptureActivity 호출
                val options = ScanOptions()
                options.setBeepEnabled(true)
                options.setOrientationLocked(true)
                options.setCaptureActivity(CaptureActivity::class.java)
                barLauncher.launch(options)
            }
    }

    fun onScanData(callback: Callback?) {
        this.onMessageCallback = callback
        getClipBoardText()
    }


    private fun getClipBoardText() {
        val buildDevice = Build.DEVICE
        if (buildDevice.contains("emulator")) { // 에뮬레이터면...
            return
        }

        if ( cbListener != null) {
            return
        }
        try {
            val clipBoardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            cbListener = ClipboardManager.OnPrimaryClipChangedListener {
                val copiedString = clipBoardManager.primaryClip?.getItemAt(0)?.text?.toString()
                // Your code
                copiedString?.let {
                    onMessageCallback?.onMessage(it)
                }
            }

            clipBoardManager.addPrimaryClipChangedListener(cbListener)
        } catch (e: Exception) {
            Logger.e("Exception---> " + e.message)
        }
    }


}
package com.example.barcode.core.extension

import android.os.SystemClock
import android.view.View

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    try {
        val safeClickListener = OnOneOffClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

class OnOneOffClickListener(
    private var defaultInterval: Int = 1000,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0

    override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v!!)
    }
}
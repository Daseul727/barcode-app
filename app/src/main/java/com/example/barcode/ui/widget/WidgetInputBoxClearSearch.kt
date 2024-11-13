package com.example.barcode.ui.widget

import android.content.Context
import android.os.Build
import android.text.InputFilter
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.example.barcode.R

class WidgetInputBoxClearSearch : BaseInputBox {
    protected var mOnClearSearchListener: OnClearSearchListener? = null
    protected var mOnSearchListener: OnSearchListener? = null
    protected var btn_search: ImageView? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet?) :
            this(context, attrs, 0)

    constructor(context: Context) :
            this(context, null)

    override fun findLayoutRsId(): Int {
        return R.layout.widget_base_input_box_clear_search
    }

    override fun findView() {
        // ignore
    }

    override fun initView() {
        btn_search = findViewById(R.id.btn_search)
        btn_search?.setOnClickListener {
            edt_text?.let {
                mOnSearchListener?.onSearch(it.text.toString())
            }
        }
        btn_clear?.setOnClickListener {
            edt_text?.setText("")
            mOnClearSearchListener?.onClear()
        }
    }

    fun setSearchVisible(isVisible: Boolean) {
        btn_search?.isVisible = isVisible
    }

    fun setOnClearSearchListener(listener: OnClearSearchListener?) {
        mOnClearSearchListener = listener
    }

    fun setOnSearchListener(listener: OnSearchListener?) {
        mOnSearchListener = listener
    }

    fun interface OnClearSearchListener {
        fun onClear()
    }

    fun interface OnSearchListener {
        fun onSearch(keyword: String)
    }


    fun setFilers(inputFilter: Array<InputFilter>) {
        edt_text?.filters = inputFilter
    }
}
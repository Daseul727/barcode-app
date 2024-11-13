package com.example.barcode.ui.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.CountDownTimer
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.annotation.IntDef
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.barcode.R
import com.example.barcode.core.mvvm.Event
import com.example.barcode.utils.DateUtils

abstract class BaseInputBox : FrameLayout {

    val logTag = "TLOG"

    companion object {
        const val BG_TYPE_DEFAULT = 0
        const val BG_TYPE_HAS_FOCUS = 1
        const val BG_TYPE_HAS_ERROR = 2
    }

    @IntDef(
        EditorInfo.IME_ACTION_NEXT,
        EditorInfo.IME_ACTION_SEARCH,
        EditorInfo.IME_ACTION_DONE
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class InputBoxImeOptions

    @IntDef(
        InputType.TYPE_CLASS_TEXT,
        InputType.TYPE_CLASS_NUMBER,
        InputType.TYPE_TEXT_VARIATION_PASSWORD,
        InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
        InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS,
        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class InputBoxInputType

    @IntDef(
        BG_TYPE_DEFAULT,
        BG_TYPE_HAS_FOCUS,
        BG_TYPE_HAS_ERROR
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class InputBoxBgType {}

    constructor(context: Context) : super(context) {
        inflateView()
        initCommonView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        inflateView()
        getAttrs(attrs)
        initCommonView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        inflateView()
        getAttrs(attrs)
        initCommonView()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        inflateView()
        getAttrs(attrs, defStyleRes)
        initCommonView()
    }

    val inputTextWatcher = MutableLiveData<Event<String>>()

    protected fun getAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputBox)
        setTypeArray(typedArray)
    }

    protected fun getAttrs(attrs: AttributeSet?, defStyle: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputBox, defStyle, 0)
        setTypeArray(typedArray)
    }

    protected fun setTypeArray(typedArray: TypedArray) {

        setBoxBgOn(typedArray.getDrawable(R.styleable.InputBox_box_bg_on))
        setBoxBgOff(typedArray.getDrawable(R.styleable.InputBox_box_bg_off))
        setBoxBgError(typedArray.getDrawable(R.styleable.InputBox_box_bg_error))
        setTitle(typedArray.getString(R.styleable.InputBox_box_title))
        setText(typedArray.getString(R.styleable.InputBox_box_text))
        setTextColor(typedArray.getColor(R.styleable.InputBox_box_text_color, Color.BLACK))
        setTextSize(typedArray.getInteger(R.styleable.InputBox_box_text_size, 14))
        setTextMaxLength(typedArray.getInteger(R.styleable.InputBox_box_text_maxlength, 30)) // 인풋 최대치
        setTextHint(typedArray.getString(R.styleable.InputBox_box_text_hint))
        setTextHintColor(typedArray.getColor(R.styleable.InputBox_box_text_hint_color, Color.GRAY))
        isEnabled = typedArray.getBoolean(R.styleable.InputBox_box_editable, true)
        isBtnClearVisible = typedArray.getBoolean(R.styleable.InputBox_box_clear_visible, true)
        setHighlight(typedArray.getBoolean(R.styleable.InputBox_box_highlight, false))
        setImeOptions(
            typedArray.getInteger(
                R.styleable.InputBox_box_imeOptions,
                EditorInfo.IME_ACTION_NEXT
            )
        )
        setInputType(
            typedArray.getInteger(
                R.styleable.InputBox_box_inputType,
                EditorInfo.TYPE_CLASS_TEXT
            )
        )
        setPasswordEnable(typedArray.getBoolean(R.styleable.InputBox_box_password_enable, false))
        setPasswordVisible(typedArray.getBoolean(R.styleable.InputBox_box_password_visible, false))
        setVerticalOffset(
            typedArray.getInt(
                R.styleable.InputBox_box_vertical_offset,
                0
            )
        ) // autocomplet 위치 높이
        setFrontIcon(typedArray.getString(R.styleable.InputBox_box_front_icon)) // front icon ㅇㅣ름
        typedArray.recycle()
    }

    interface OnCountDownTimerListener {
        fun onFinish()
    }

    protected var layout_main: LinearLayout? = null
    protected var tv_title: TextView? = null
    protected var iv_highlight: ImageView? = null
    protected var edt_text: EditText? = null
    protected var btn_visible: ToggleButton? = null
    protected var btn_clear: ImageView? = null
    protected var tv_timer: TextView? = null

    protected var mOnFocusChangeListener: OnFocusChangeListener? = null
    protected var mOnEditorActionListener: TextView.OnEditorActionListener? = null
    protected var mOnCountDownTimerFinishListener: OnCountDownTimerListener? = null

    protected var countDownTimer: CountDownTimer? = null
    protected var timerCounting = false // 카운팅중인지 여부.

    protected var box_bg_on: Drawable? = null
    protected var box_bg_off: Drawable? = null
    protected var box_bg_error: Drawable? = null

    protected var isBtnClearVisible: Boolean = true
    protected var isBtnPasswordVisible: Boolean = false

    protected var vertical_offset: Int = 0  // autocomplet 리스트 위아래 위치 조절

    protected var icon_front: String? = null  // login box안에 이미지

    private fun inflateView() {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(
            if (findLayoutRsId() < 0) R.layout.widget_base_input_box else findLayoutRsId(),
            this,
            false
        )
        addView(view)
        findCommonView()
    }

    protected open fun findCommonView() {

        layout_main = findViewById(R.id.layout_main)
        tv_title = findViewById(R.id.tv_title)
        iv_highlight = findViewById(R.id.iv_highlight)
        edt_text = findViewById(R.id.edt_text)
        btn_visible = findViewById(R.id.btn_visible)
        btn_clear = findViewById(R.id.btn_clear)
        tv_timer = findViewById(R.id.tv_timer)
        findView()
    }

    abstract fun findLayoutRsId(): Int

    abstract fun findView()

    abstract fun initView()

    private fun initCommonView() {

        edt_text?.setOnEditorActionListener { v, actionId, event ->
            var result = if (actionId > EditorInfo.IME_ACTION_UNSPECIFIED) {
                mOnEditorActionListener?.onEditorAction(v, actionId, event) ?: false
            } else false
            return@setOnEditorActionListener result
        }

        // 기본 배경색
        setBackgroundType(BG_TYPE_DEFAULT)

        // focus 변화
        edt_text?.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            onBoxFocusChanged(v, hasFocus)
            mOnFocusChangeListener?.onFocusChange(v, hasFocus)
        }


        // 글자 없을때는 clear 버튼 안보이도록 처리
        updateClearButtonVisibility(edt_text?.text.toString())
        edt_text?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isBtnPasswordVisible) {
                    updatePasswordButtonVisibility(s?.toString())
                    if (isBtnClearVisible)
                        updateClearButtonVisibility(edt_text?.text.toString())
                } else {
                    updateClearButtonVisibility(s?.toString())
                }

                val str = s?.toString() ?: ""
                inputTextWatcher.value = Event(str)
            }
        })

        // 글씨 삭제
        btn_clear?.setOnClickListener { _ ->
            edt_text?.setText("")
        }

        initView()
    }

    fun setFrontIcon(drawableName: String?) {
        icon_front = drawableName
    }

    override fun setEnabled(enable: Boolean) {
        edt_text?.isEnabled = enable
        if (enable) {
            updateClearButtonVisibility(edt_text?.text.toString())
        } else {
            btn_clear?.visibility = View.GONE
        }
    }

    override fun clearFocus() {
        super.clearFocus()

        layout_main?.clearFocus()
        edt_text?.clearFocus()
    }

    fun requestFocusOnBox() {

        edt_text?.requestFocus()
    }

    fun clearBox() {
        edt_text?.setText("")
    }

    /**
     * Update Listener
     */

    fun setOnBoxFocusChangeListener(listener: OnFocusChangeListener?) {
        mOnFocusChangeListener = listener
    }

    fun setOnEditorActionListener(listener: TextView.OnEditorActionListener?) {
        mOnEditorActionListener = listener
    }

    fun setOnCountDownTimerFinishListener(listener: OnCountDownTimerListener?) {
        mOnCountDownTimerFinishListener = listener
    }

    fun setOnCountDownTimerFinishListener(listener: () -> Unit) {
        mOnCountDownTimerFinishListener = object : OnCountDownTimerListener {
            override fun onFinish() {
                listener()
            }
        }
    }

    fun addTextChangedListener(watcher: TextWatcher?) {
        edt_text?.addTextChangedListener(watcher)
    }

    /**
     * Update Type
     */

    var hasError: Boolean = false
        set(value) {
            if (value) {
                field = value
                setBackgroundType(BG_TYPE_HAS_ERROR)
            }
        }

    fun setImeOptions(@InputBoxImeOptions imeOptions: Int) {
        edt_text?.imeOptions = imeOptions
    }

    fun setInputType(@InputBoxInputType inputType: Int) {
        edt_text?.inputType = inputType
    }

    /**
     * Update View
     */

    fun setBoxBgOn(bg: Drawable?) {
        box_bg_on = bg
    }

    fun setBoxBgOff(bg: Drawable?) {
        box_bg_off = bg
    }

    fun setBoxBgError(bg: Drawable?) {
        box_bg_error = bg
    }

    fun setTitle(text: String?) {
        tv_title?.text = text ?: ""
    }

    fun setText(text: String?) {
        var str = ""
        if (!text.isNullOrEmpty()) {
            str = text.trimStart()
        } else {

        }
        edt_text?.setText(str)
    }
    fun setSelection(length: Int) {
        edt_text?.setSelection(length)
    }


    fun getText(): String {
        return edt_text?.text.toString().trim()
    }

    fun setTextColor(color: Int) {
        edt_text?.setTextColor(color)
    }

    fun setTextHint(text: String?) {
        edt_text?.hint = text ?: ""
    }

    fun setTextHintColor(color: Int) {
        edt_text?.setHintTextColor(color)
    }

    fun setHighlight(visible: Boolean) {
        iv_highlight?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    // Password visible 버튼 활성화 여부
    fun setPasswordEnable(enable: Boolean) {
        btn_visible?.visibility = if (enable) View.VISIBLE else View.GONE
        if (enable) {
            setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
        }
    }

    // Password 글자 보기/안보기 여부
    fun setPasswordVisible(visible: Boolean) {
        isBtnPasswordVisible = visible
        btn_visible?.visibility = View.GONE //첨 시작 할때는 안보이도록 수정 -> if (visible) View.VISIBLE else View.GONE
        if (visible) {
            edt_text?.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        btn_visible?.setOnCheckedChangeListener(if (!visible) null else CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // show
                edt_text?.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                // hide
                edt_text?.transformationMethod = PasswordTransformationMethod.getInstance()
            }

            val pos = edt_text?.length()
            if (pos != null) {
                edt_text?.setSelection(pos)
            }
        })
    }

    fun setTextSize(size: Int?) {
        edt_text?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (size ?: 0).toFloat())
    }

    fun setTextMaxLength(maxLength: Int) {
        edt_text?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
    }

    fun setVerticalOffset(hight: Int) {
        vertical_offset = hight
    }

    fun updateClearButtonVisibility(text: String?) {
        if (isBtnClearVisible) {
            btn_clear?.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
        } else {
            btn_clear?.visibility = View.GONE
        }
    }

    fun updatePasswordButtonVisibility(text: String?) {
        if (isBtnPasswordVisible) {
            btn_visible?.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
        } else {
            btn_visible?.visibility = View.GONE
        }
    }

    // focus 변화시 배경색 변경
    open fun onBoxFocusChanged(view: View?, hasFocus: Boolean) {
        if (hasError) {
            setBackgroundType(BG_TYPE_HAS_ERROR)
        } else {
            if (hasFocus) {
                setBackgroundType(BG_TYPE_HAS_FOCUS)
            } else {
                setBackgroundType(BG_TYPE_DEFAULT)
            }
        }
    }

    open fun setBackgroundType(@InputBoxBgType bgType: Int) {
        when (bgType) {
            BG_TYPE_HAS_ERROR -> {

                if (box_bg_error != null) {
                    layout_main?.background = box_bg_error
                } else {
                    //var resId: Int = ResourceUtils.getAttributeResourceId(layout_main?.context, R.attr.box_bg_error)
                    var resId: Int = R.drawable.rect_stroke_rounded_red
                    layout_main?.setBackgroundResource(resId)
                }
            }
            BG_TYPE_HAS_FOCUS -> {
                if (box_bg_on != null) {
                    layout_main?.background = box_bg_on
                } else {
                    var resId: Int = R.drawable.rect_stroke_rounded_yellow
                    layout_main?.setBackgroundResource(resId)
                }
            }
            else -> {
                if (box_bg_off != null) {
                    layout_main?.background = box_bg_off
                } else {
                    var resId: Int = R.color.white
                    layout_main?.setBackgroundResource(resId)
                }
            }
        }
    }

    fun hideKeyboard() {

        edt_text?.let {
            val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            postDelayed({ imm.hideSoftInputFromWindow(it.windowToken, 0) }, 50)
        }
    }

    fun showKeyboard() {

        edt_text?.let {
            val imm: InputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            postDelayed({
                it.requestFocus()
                imm.showSoftInput(it, 0)
            }, 100)
        }
    }

    fun startTimer(milliSeconds: Long) {

        var time = if (milliSeconds <= 0) 30000 else milliSeconds
        var textTime = DateUtils.convertTommss(time)

        tv_timer?.setText(textTime)
        tv_timer?.visibility = View.VISIBLE

        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                timerCounting = true
                val seconds_remaining = millisUntilFinished / 1000
                textTime = DateUtils.convertTommss(seconds_remaining)
                tv_timer?.setText(textTime)
                Log.d(logTag, "textTime = $textTime")
            }

            override fun onFinish() {

                timerCounting = false
                mOnCountDownTimerFinishListener?.onFinish()
                Log.d(logTag, "done!")
            }
        }

        countDownTimer?.start()
    }

    fun cancelTimer() {
        countDownTimer?.cancel()
        timerCounting = false
        tv_timer?.setText("")
    }

    fun isTimerCounting(): Boolean {
        return timerCounting
    }

}

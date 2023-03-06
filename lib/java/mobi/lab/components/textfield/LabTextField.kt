package mobi.lab.components.textfield

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.annotation.Dimension
import androidx.core.view.updatePadding
import com.google.android.material.textfield.TextInputLayout
import mobi.lab.components.R
import mobi.lab.components.shared.ParcelCompat

class LabTextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {

    interface Listener {
        fun onTextChanged(text: String?)
        fun onFocusChanged(hasFocus: Boolean)
        fun onErrorCleared()
    }

    // TODO comment
    var listener: Listener? = null
    // TODO comment
    var clearErrorOnFocus: Boolean = true
    // TODO comment
    val editText: LabTextInputEditText

    private var boxHelper: LabTextFieldBoxHelper? = null
    private var errorState = false
    private var inDrawableStateChanged = false

    init {
        editText = LabTextInputEditText(context, attrs)
        editText.id = ID_EDIT_TEXT
        editText.hint = null
        editText.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        initEditText(editText)
        addView(editText)

        attrs?.let {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.LabTextField, 0, defStyleAttr)
            try {
                setTextPaddingVertical(
                    topPx = attributes.getDimensionPixelSize(R.styleable.LabTextField_textPaddingTop, NO_VALUE_INT),
                    bottomPx = attributes.getDimensionPixelSize(R.styleable.LabTextField_textPaddingBottom, NO_VALUE_INT),
                )
                setTextPaddingHorizontal(attributes.getDimensionPixelSize(R.styleable.LabTextField_textPaddingHorizontal, NO_VALUE_INT))
                clearErrorOnFocus = attributes.getBoolean(R.styleable.LabTextField_clearErrorOnFocus, clearErrorOnFocus)

                if (attributes.hasValue(R.styleable.LabTextField_android_inputType)) {
                    editText.inputType = attributes.getInt(R.styleable.LabTextField_android_inputType, EditorInfo.TYPE_CLASS_TEXT)
                }
                if (attributes.hasValue(R.styleable.LabTextField_android_imeOptions)) {
                    editText.imeOptions = attributes.getInt(R.styleable.LabTextField_android_imeOptions, EditorInfo.IME_ACTION_NONE)
                }
            } finally {
                attributes.recycle()
            }
        }

        boxHelper = LabTextFieldBoxHelper(this, attrs, defStyleAttr)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val parentState = super.onCreateDrawableState(extraSpace + 1)
        if (errorState) {
            mergeDrawableStates(parentState, intArrayOf(R.attr.state_error))
        }
        return parentState
    }

    override fun drawableStateChanged() {
        if (inDrawableStateChanged) {
            // Some of the calls below will update the drawable state of child views. Since we're
            // using addStatesFromChildren we can get into infinite recursion, hence we'll just
            // exit in this instance
            return
        }
        inDrawableStateChanged = true

        boxHelper?.updateBoxState()
        super.drawableStateChanged()

        inDrawableStateChanged = false
    }

    override fun setError(error: CharSequence?) {
        if (error != getError()) {
            setErrorStateInternal(textError = !TextUtils.isEmpty(error))
        }
        super.setError(error)
    }

    override fun onSaveInstanceState(): Parcelable {
        val parentState = super.onSaveInstanceState()
        val bundle = Bundle()
        bundle.putParcelable(STATE_PARENT, parentState)
        bundle.putCharSequence(STATE_ERROR, error)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state == null || state !is Bundle) {
            super.onRestoreInstanceState(state)
            return
        }
        error = state.getCharSequence(STATE_ERROR)
        super.onRestoreInstanceState(ParcelCompat.getParcelable(state, STATE_PARENT))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        editText.setOnFocusChangeListener { _, hasFocus ->
            listener?.onFocusChanged(hasFocus)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        editText.onFocusChangeListener = null
    }

    fun getText(): String {
        return editText.text.toString()
    }

    fun setText(text: CharSequence?) {
        editText.setText(text)
    }

    fun setTextAndSelection(text: CharSequence?) {
        setText(text)
        editText.apply {
            val value = getText()?.toString() ?: ""
            setSelection(value.length)
        }
    }

    fun setTextPaddingVertical(@Dimension(unit = Dimension.PX) topPx: Int, @Dimension(unit = Dimension.PX) bottomPx: Int) {
        val top = if (topPx != NO_VALUE_INT) topPx else editText.paddingTop
        val bottom = if (bottomPx != NO_VALUE_INT) bottomPx else editText.paddingBottom
        editText.updatePadding(top = top, bottom = bottom)
    }

    fun setTextPaddingHorizontal(@Dimension(unit = Dimension.PX) paddingPx: Int) {
        if (paddingPx != NO_VALUE_INT) {
            editText.compoundDrawablePadding = paddingPx
        }
    }

    fun setImeActionHandler(imeAction: Int, onImeAction: (keyEvent: KeyEvent) -> Unit) {
        editText.apply {
            if (imeOptions != imeAction) {
                imeOptions = imeAction
            }
            setOnEditorActionListener { _, actionId, keyEvent ->
                if (actionId == imeAction) {
                    onImeAction(keyEvent)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }
    }

    fun setInputType(inputType: Int) {
        if (editText.inputType != inputType) {
            // Some input types change the typeface. Let's reuse the old typeface
            val typeface = editText.typeface
            editText.inputType = inputType
            editText.typeface = typeface
        }
    }

    // TODO do we actually need this? I don't think so...
    fun clearError() {
        error = null
        listener?.onErrorCleared()
    }

    // TODO verify with Elmo that automatic clear error on focus should be a part of the component
    private fun initEditText(editText: LabTextInputEditText) {
        editText.focusedListener = {
            if (clearErrorOnFocus) {
                clearError()
            }
        }
        editText.textChangedListener = { text, stateRestore ->
            setErrorStateInternal(counterError = isCounterError(text))

            if (!stateRestore) {
                listener?.onTextChanged(text.toString())
                // Only reset errors when the user enters text
                clearError()
            }
        }
    }

    private fun isCounterError(text: CharSequence?): Boolean {
        val textLength = text?.length ?: -1
        return isCounterEnabled && textLength > counterMaxLength
    }

    private fun setErrorStateInternal(
        textError: Boolean = !TextUtils.isEmpty(error),
        counterError: Boolean = isCounterError(getText())
    ) {
        val newErrorState = textError || counterError
        if (errorState == newErrorState) {
            return
        }
        errorState = newErrorState
        editText.errorState = newErrorState
        refreshDrawableState()
    }

    companion object {
        private const val STATE_ERROR = "LabTextField.STATE_ERROR"
        private const val STATE_PARENT = "LabTextField.STATE_PARENT"

        private const val NO_VALUE_INT: Int = -1

        const val ID_EDIT_TEXT = android.R.id.text1
    }
}

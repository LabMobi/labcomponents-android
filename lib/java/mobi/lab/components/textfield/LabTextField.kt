package mobi.lab.components.textfield

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.annotation.Dimension
import androidx.core.view.updatePadding
import com.google.android.material.textfield.TextInputLayout
import mobi.lab.components.R
import mobi.lab.components.shared.ParcelCompat
import timber.log.Timber

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

    private var errorState = false

    init {
        editText = LabTextInputEditText(context, attrs)
        editText.id = ID_EDIT_TEXT
        editText.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        addView(editText)
        initEditText(editText)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val parentState = super.onCreateDrawableState(extraSpace + 1)
        if (errorState) {
            mergeDrawableStates(parentState, intArrayOf(R.attr.state_error))
        }
        return parentState
    }

    override fun setError(error: CharSequence?) {
        errorState = !TextUtils.isEmpty(error)
        editText.errorState = errorState
        refreshDrawableState()
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

    fun setTextPadding(
        @Dimension(unit = Dimension.PX) leftPx: Int,
        @Dimension(unit = Dimension.PX) topPx: Int,
        @Dimension(unit = Dimension.PX) rightPx: Int,
        @Dimension(unit = Dimension.PX) bottomPx: Int,
    ) {
        setTextPaddingInternal(leftPx = leftPx, topPx = topPx, rightPx = rightPx, bottomPx = bottomPx)
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

    private fun setTextPaddingInternal(leftPx: Int, topPx: Int, rightPx: Int, bottomPx: Int) {
        // TODO remove logging
        Timber.e("setTextPaddingInternal leftPx=$leftPx topPx=$topPx rightPx=$rightPx bottomPx=$bottomPx")

        val left = if (leftPx != NO_VALUE_INT) leftPx else editText.paddingLeft
        val top = if (topPx != NO_VALUE_INT) topPx else editText.paddingTop
        val right = if (rightPx != NO_VALUE_INT) rightPx else editText.paddingRight
        val bottom = if (bottomPx != NO_VALUE_INT) bottomPx else editText.paddingBottom

        editText.updatePadding(left = left, top = top, right = right, bottom = bottom)
    }

    // TODO verify with Elmo that automatic clear error on focus should be a part of the component
    private fun initEditText(editText: LabTextInputEditText) {
        editText.focusedListener = {
            if (clearErrorOnFocus) {
                clearError()
            }
        }
        editText.textChangedListener = { text, stateRestore ->
            if (!stateRestore) {
                listener?.onTextChanged(text.toString())
                // Only reset errors when the user enters text
                clearError()
            }
        }
    }

    companion object {
        private const val STATE_ERROR = "LabTextField.STATE_ERROR"
        private const val STATE_PARENT = "LabTextField.STATE_PARENT"

        private const val NO_VALUE_INT: Int = -1

        const val ID_EDIT_TEXT = android.R.id.text1
    }
}

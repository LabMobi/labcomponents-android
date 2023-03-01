package mobi.lab.components.textfield

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.google.android.material.textfield.TextInputLayout
import mobi.lab.components.shared.ParcelCompat
import mobi.lab.components.R

class LabTextInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {

    interface Listener {
        fun onTextChanged(text: String?)
        fun onErrorCleared()
    }

    var listener: Listener? = null
    var clearErrorOnFocus: Boolean = true

    private lateinit var editText: LabTextInputEditText
    private var errorState = false

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        if (child is LabTextInputEditText) {
            saveEditText(child)
        }
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

    fun getText(): String {
        return editText.text.toString()
    }

    fun setText(text: CharSequence?) {
        editText.setText(text)
    }

    private fun saveEditText(editText: LabTextInputEditText) {
        this.editText = editText
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

    private fun clearError() {
        error = null
        listener?.onErrorCleared()
    }

    companion object {
        private const val STATE_ERROR = "STATE_ERROR"
        private const val STATE_PARENT = "STATE_PARENT"
    }
}

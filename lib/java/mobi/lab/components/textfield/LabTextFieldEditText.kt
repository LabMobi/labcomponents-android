package mobi.lab.components.textfield

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import mobi.lab.components.shared.ParcelCompat
import mobi.lab.components.R

@Suppress("ClickableViewAccessibility")
public open class LabTextFieldEditText : TextInputEditText {

    public constructor(context: Context) : super(context)
    public constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, com.google.android.material.R.attr.editTextStyle)
    public constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    // Workaround to let text change watchers know of programmatic changes
    private var restoreStateInProgress = false

    internal var errorState = false
        set(value) {
            field = value
            refreshDrawableState()
        }

    /**
     * We have our own TextWatcher here so that we can remove it when restoring view state to avoid duplicate calls
     */
    @Suppress("EmptyFunctionBlock")
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            textChangedListener?.invoke(editable.toString(), restoreStateInProgress)
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
    }

    // Custom text changed listener that is not called when state is restored
    internal var textChangedListener: ((CharSequence, Boolean) -> Unit)? = null

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val parentState = super.onCreateDrawableState(extraSpace + 1)
        if (errorState) {
            mergeDrawableStates(parentState, intArrayOf(R.attr.state_error))
        }
        return parentState
    }

    override fun onSaveInstanceState(): Parcelable {
        val parentState = super.onSaveInstanceState()
        val bundle = Bundle()
        bundle.putParcelable(STATE_PARENT, parentState)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        restoreStateInProgress = true
        if (state == null || state !is Bundle) {
            super.onRestoreInstanceState(state)
            return
        }

        // Workaround to avoid duplicate text changed calls
        super.onRestoreInstanceState(ParcelCompat.getParcelable(state, STATE_PARENT))
        restoreStateInProgress = false
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addTextChangedListener(textWatcher)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeTextChangedListener(textWatcher)
    }

    internal companion object {
        private const val STATE_PARENT = "LabTextInputEditText.STATE_PARENT"
    }
}

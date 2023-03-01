package mobi.lab.components.textfield

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.material.textfield.TextInputEditText
import mobi.lab.components.shared.ParcelCompat
import mobi.lab.components.R

@Suppress("ClickableViewAccessibility")
class LabTextInputEditText : TextInputEditText {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

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

    // Custom focused listener that is called when the view is touched
    internal var focusedListener: (() -> Unit)? = null

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
        setOnTouchListener { _, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                focusedListener?.invoke()
            }
            return@setOnTouchListener false
        }
        addTextChangedListener(textWatcher)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeTextChangedListener(textWatcher)
        setOnTouchListener(null)
    }

    companion object {
        private const val STATE_PARENT = "STATE_PARENT"
    }
}

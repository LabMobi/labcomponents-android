package mobi.lab.components.textfield

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.Px
import androidx.annotation.RequiresApi
import androidx.core.view.updatePadding
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.textfield.TextInputLayout
import mobi.lab.components.R
import mobi.lab.components.shared.DrawableUtil
import mobi.lab.components.shared.Log
import mobi.lab.components.shared.ParcelCompat

/**
 * A wrapper around TextInputLayout with a custom box background to support Mobi Lab design components.
 */
@Suppress("FoldInitializerAndIfToElvis")
public open class LabTextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {

    public fun interface TextChangedListener {
        public fun onTextChanged(text: String?)
    }

    public interface StateChangedListener {
        public fun onErrorCleared()
    }

    /**
     * TextField listener for text changed events.
     */
    public var textChangedListener: TextChangedListener? = null

    /**
     * TextField listener for focus changed events and error cleared events.
     */
    public var stateChangedListener: StateChangedListener? = null

    /**
     * Mode for automatically clearing TextField's error state.
     * @see [AutoClearErrorMode]
     */
    public var autoClearErrorMode: AutoClearErrorMode = AutoClearErrorMode.ON_TOUCH

    /**
     * Inner [LabTextFieldEditText] that is automatically added as a child.
     */
    public var editText: LabTextFieldEditText? = null

    private var boxHelper: LabTextFieldBoxBackgroundHelper? = null
    private var errorState = false
    private var inDrawableStateChanged = false
    private var textPaddingTop: Int = NO_VALUE_INT
    private var textPaddingBottom: Int = NO_VALUE_INT
    private var textPaddingHorizontal: Int = NO_VALUE_INT

    // A temporary solution to update the cursor color until the Material lib's support for this becomes available
    private var cursorColorOverride: Int = NO_VALUE_INT

    init {
        attrs?.let {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.LabTextField, 0, defStyleAttr)
            try {
                // Cached and set later
                textPaddingTop = attributes.getDimensionPixelSize(R.styleable.LabTextField_textPaddingTop, NO_VALUE_INT)
                textPaddingBottom = attributes.getDimensionPixelSize(R.styleable.LabTextField_textPaddingBottom, NO_VALUE_INT)
                textPaddingHorizontal = attributes.getDimensionPixelSize(R.styleable.LabTextField_textPaddingHorizontal, NO_VALUE_INT)

                val rawClearErrorMode = attributes.getInt(R.styleable.LabTextField_autoClearErrorMode, autoClearErrorMode.value())
                autoClearErrorMode = AutoClearErrorMode.parse(rawClearErrorMode)
            } finally {
                attributes.recycle()
            }
        }

        boxHelper = LabTextFieldBoxBackgroundHelper(this, attrs, defStyleAttr)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (child is LabTextFieldEditText) {
            saveEditTextInternal(child)
        }
        super.addView(child, index, params)
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
        updateCursorColor()

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

    /**
     * Get the text value.
     *
     * @return Text
     */
    public fun getText(): String {
        useEditText {
            // Just to make sure the editText is available
        }
        return editText?.text?.toString() ?: ""
    }

    /**
     * Set the text value.
     *
     * @param text Text value
     */
    public fun setText(text: CharSequence?) {
        useEditText { it.setText(text) }
    }

    /**
     * Set the text value and move caret position to the end of the text.
     *
     * @param text Text value
     */
    public fun setTextAndSelection(text: CharSequence?) {
        setText(text)
        useEditText { editText ->
            val value = editText.text?.toString() ?: ""
            editText.setSelection(value.length)
        }
    }

    /**
     * Set inner EditText's vertical paddings. This controls where the EditText is positioned within the TextInputLayout container.
     *
     * @param topPx Top padding
     * @param bottomPx Bottom padding
     */
    public fun setTextPaddingVertical(@Dimension(unit = Dimension.PX) topPx: Int, @Dimension(unit = Dimension.PX) bottomPx: Int) {
        // Save paddings. Will be used when the EditText becomes available
        textPaddingTop = topPx
        textPaddingBottom = bottomPx

        useEditText { editText ->
            val top = if (topPx != NO_VALUE_INT) topPx else editText.paddingTop
            val bottom = if (bottomPx != NO_VALUE_INT) bottomPx else editText.paddingBottom
            editText.updatePadding(top = top, bottom = bottom)
        }
    }

    /**
     * Set inner EditText's horizontal paddings. The space between the start and end icons and/or prefix/suffix texts.
     *
     * @param paddingPx Start and end padding.
     */
    public fun setTextPaddingHorizontal(@Dimension(unit = Dimension.PX) paddingPx: Int) {
        // Save paddings. Will be used when the EditText becomes available
        textPaddingHorizontal = paddingPx

        useEditText { editText ->
            if (paddingPx != NO_VALUE_INT) {
                editText.compoundDrawablePadding = paddingPx
            }
        }
    }

    /**
     * Set IME action mode and handler callback.
     *
     * @param imeAction IME action. Example: [EditorInfo.IME_ACTION_NONE]
     * @onImeAction callback invoked with the [KeyEvent] when the matching IME action is invoked.
     */
    public fun setImeActionHandler(imeAction: Int, onImeAction: (keyEvent: KeyEvent) -> Unit) {
        useEditText { editText ->
            if (editText.imeOptions != imeAction) {
                editText.imeOptions = imeAction
            }
            editText.setOnEditorActionListener { _, actionId, keyEvent ->
                if (actionId == imeAction) {
                    onImeAction(keyEvent)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }
    }

    /**
     * Set the inner EditText's inputType.
     *
     * @param inputType [android.text.InputType]
     */
    public fun setInputType(inputType: Int) {
        useEditText { editText ->
            if (editText.inputType != inputType) {
                // Some input types change the typeface. Let's reuse the old typeface
                val typeface = editText.typeface
                editText.inputType = inputType
                editText.typeface = typeface
            }
        }
    }

    /**
     * Set a color for the cursor and text select handles. This overrides the default color from colorControlActivated value.
     * NB! Only supported on API 29+
     *
     * @param color The new color override
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    public fun setCursorColorOverride(@ColorInt color: Int) {
        cursorColorOverride = color
        updateCursorColor()
    }

    public override fun setBoxStrokeWidth(@Px boxStrokeWidth: Int) {
        super.setBoxStrokeWidth(boxStrokeWidth)
        boxHelper?.boxStrokeWidthDefaultPx = boxStrokeWidth
    }

    public override fun setBoxStrokeWidthFocused(@Px boxStrokeWidthFocused: Int) {
        super.setBoxStrokeWidthFocused(boxStrokeWidthFocused)
        boxHelper?.boxStrokeWidthFocusedPx = boxStrokeWidthFocused
    }

    override fun setBoxStrokeColor(boxStrokeColor: Int) {
        super.setBoxStrokeColor(boxStrokeColor)
        boxHelper?.boxStrokeColor = ColorStateList.valueOf(boxStrokeColor)
    }

    override fun setBoxStrokeColorStateList(boxStrokeColor: ColorStateList) {
        super.setBoxStrokeColorStateList(boxStrokeColor)
        boxHelper?.boxStrokeColor = boxStrokeColor
    }

    override fun setBoxBackgroundColor(boxBackgroundColor: Int) {
        super.setBoxBackgroundColor(boxBackgroundColor)
        boxHelper?.boxBackgroundColor = ColorStateList.valueOf(boxBackgroundColor)
    }

    override fun setBoxBackgroundColorStateList(boxBackgroundColor: ColorStateList) {
        super.setBoxBackgroundColorStateList(boxBackgroundColor)
        boxHelper?.boxBackgroundColor = boxBackgroundColor
    }

    override fun setBoxCornerRadii(
        boxCornerRadiusTopStart: Float,
        boxCornerRadiusTopEnd: Float,
        boxCornerRadiusBottomStart: Float,
        boxCornerRadiusBottomEnd: Float
    ) {
        super.setBoxCornerRadii(boxCornerRadiusTopStart, boxCornerRadiusTopEnd, boxCornerRadiusBottomStart, boxCornerRadiusBottomEnd)
        boxHelper?.setBoxCornerRadii(
            topStartPx = boxCornerRadiusTopStart,
            topEndPx = boxCornerRadiusTopEnd,
            bottomStartPx = boxCornerRadiusBottomStart,
            bottomEndPx = boxCornerRadiusBottomEnd
        )
    }

    override fun setShapeAppearanceModel(shapeAppearanceModel: ShapeAppearanceModel) {
        super.setShapeAppearanceModel(shapeAppearanceModel)
        boxHelper?.setShapeAppearanceModel(shapeAppearanceModel)
    }

    /**
     * Clear the error state.
     */
    public fun clearError() {
        error = null
        stateChangedListener?.onErrorCleared()
    }

    @Suppress("ClickableViewAccessibility")
    protected fun saveEditTextInternal(editText: LabTextFieldEditText) {
        this.editText = editText
        // Update states that might have been cached
        setTextPaddingVertical(topPx = textPaddingTop, bottomPx = textPaddingBottom)
        setTextPaddingHorizontal(textPaddingHorizontal)

        if (editText.errorState != errorState) {
            editText.errorState = errorState
        }

        editText.setOnTouchListener { _, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                if (autoClearErrorMode.value() >= AutoClearErrorMode.ON_TOUCH.value()) {
                    clearError()
                }
            }
            return@setOnTouchListener false
        }

        editText.textChangedListener = { text, stateRestore ->
            setErrorStateInternal(counterError = isCounterError(text))

            if (!stateRestore) {
                textChangedListener?.onTextChanged(text.toString())

                if (autoClearErrorMode.value() >= AutoClearErrorMode.ON_TEXT_CHANGED.value()) {
                    clearError()
                }
            }
        }

        boxHelper?.updateBoxState()
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
        editText?.errorState = newErrorState
        refreshDrawableState()
    }

    private fun useEditText(action: (LabTextFieldEditText) -> Unit) {
        val editText = editText
        if (editText == null) {
            Log.getInstance(this).e(Throwable("LabTextField inner EditText is null"), "Inner EditText is null but accessed")
        } else {
            action(editText)
        }
    }

    private fun updateCursorColor() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q || cursorColorOverride == NO_VALUE_INT) {
            // Not supported.
            return
        }

        val editText = editText
        if (editText == null) {
            // Nothing to update
            return
        }

        DrawableUtil.setDrawableColor(editText.textSelectHandleLeft, cursorColorOverride)
        DrawableUtil.setDrawableColor(editText.textSelectHandleRight, cursorColorOverride)
        DrawableUtil.setDrawableColor(editText.textSelectHandle, cursorColorOverride)
        DrawableUtil.setDrawableColor(editText.textCursorDrawable, cursorColorOverride)
    }

    internal companion object {
        private const val STATE_ERROR = "LabTextField.STATE_ERROR"
        private const val STATE_PARENT = "LabTextField.STATE_PARENT"

        private const val NO_VALUE_INT: Int = -1
    }
}

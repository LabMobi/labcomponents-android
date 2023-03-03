package mobi.lab.components.textfield

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import com.google.android.material.color.MaterialColors
import com.google.android.material.shape.MaterialShapeDrawable
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
        editText.hint = null
        editText.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        addView(editText)
        initEditText(editText)

        attrs?.let {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.LabTextField, 0, defStyleAttr)
            try {
                setTextPaddingVertical(
                    topPx = attributes.getDimensionPixelSize(R.styleable.LabTextField_textPaddingTop, NO_VALUE_INT),
                    bottomPx = attributes.getDimensionPixelSize(R.styleable.LabTextField_textPaddingBottom, NO_VALUE_INT),
                )
                setTextPaddingHorizontal(attributes.getDimensionPixelSize(R.styleable.LabTextField_textPaddingHorizontal, NO_VALUE_INT))

                clearErrorOnFocus = attributes.getBoolean(R.styleable.LabTextField_clearErrorOnFocus, clearErrorOnFocus)

                // TODO (2): Background

//                shapeAppearanceModel =
//                    ShapeAppearanceModel.builder(context, attrs, defStyleAttr, DEF_STYLE_RES)
//                        .build()

//                boxLabelCutoutPaddingPx = context
//                    .resources
//                    .getDimensionPixelOffset(R.dimen.mtrl_textinput_box_label_cutout_padding)
//                boxCollapsedPaddingTopPx =
//                    a.getDimensionPixelOffset(R.styleable.TextInputLayout_boxCollapsedPaddingTop, 0)
//
//                boxStrokeWidthDefaultPx = a.getDimensionPixelSize(
//                    R.styleable.TextInputLayout_boxStrokeWidth,
//                    context
//                        .resources
//                        .getDimensionPixelSize(R.dimen.mtrl_textinput_box_stroke_width_default)
//                )
//                boxStrokeWidthFocusedPx = a.getDimensionPixelSize(
//                    R.styleable.TextInputLayout_boxStrokeWidthFocused,
//                    context
//                        .resources
//                        .getDimensionPixelSize(R.dimen.mtrl_textinput_box_stroke_width_focused)
//                )
//                boxStrokeWidthPx = boxStrokeWidthDefaultPx
//
//                val boxCornerRadiusTopStart: Float =
//                    a.getDimension(R.styleable.TextInputLayout_boxCornerRadiusTopStart, -1f)
//                val boxCornerRadiusTopEnd: Float =
//                    a.getDimension(R.styleable.TextInputLayout_boxCornerRadiusTopEnd, -1f)
//                val boxCornerRadiusBottomEnd: Float =
//                    a.getDimension(R.styleable.TextInputLayout_boxCornerRadiusBottomEnd, -1f)
//                val boxCornerRadiusBottomStart: Float =
//                    a.getDimension(R.styleable.TextInputLayout_boxCornerRadiusBottomStart, -1f)
//                val shapeBuilder = shapeAppearanceModel.toBuilder()
//                if (boxCornerRadiusTopStart >= 0) {
//                    shapeBuilder.setTopLeftCornerSize(boxCornerRadiusTopStart)
//                }
//                if (boxCornerRadiusTopEnd >= 0) {
//                    shapeBuilder.setTopRightCornerSize(boxCornerRadiusTopEnd)
//                }
//                if (boxCornerRadiusBottomEnd >= 0) {
//                    shapeBuilder.setBottomRightCornerSize(boxCornerRadiusBottomEnd)
//                }
//                if (boxCornerRadiusBottomStart >= 0) {
//                    shapeBuilder.setBottomLeftCornerSize(boxCornerRadiusBottomStart)
//                }
//                shapeAppearanceModel = shapeBuilder.build()
//
//                val filledBackgroundColorStateList: ColorStateList =
//                    MaterialResources.getColorStateList(
//                        context, a, R.styleable.TextInputLayout_boxBackgroundColor
//                    )
//                if (filledBackgroundColorStateList != null) {
//                    defaultFilledBackgroundColor = filledBackgroundColorStateList.defaultColor
//                    boxBackgroundColor = defaultFilledBackgroundColor
//                    if (filledBackgroundColorStateList.isStateful) {
//                        disabledFilledBackgroundColor =
//                            filledBackgroundColorStateList.getColorForState(
//                                intArrayOf(-android.R.attr.state_enabled),
//                                -1
//                            )
//                        focusedFilledBackgroundColor =
//                            filledBackgroundColorStateList.getColorForState(
//                                intArrayOf(
//                                    android.R.attr.state_focused,
//                                    android.R.attr.state_enabled
//                                ), -1
//                            )
//                        hoveredFilledBackgroundColor =
//                            filledBackgroundColorStateList.getColorForState(
//                                intArrayOf(
//                                    android.R.attr.state_hovered,
//                                    android.R.attr.state_enabled
//                                ), -1
//                            )
//                    } else {
//                        focusedFilledBackgroundColor = defaultFilledBackgroundColor
//                        val mtrlFilledBackgroundColorStateList =
//                            AppCompatResources.getColorStateList(
//                                context,
//                                R.color.mtrl_filled_background_color
//                            )
//                        disabledFilledBackgroundColor =
//                            mtrlFilledBackgroundColorStateList.getColorForState(
//                                intArrayOf(-android.R.attr.state_enabled),
//                                -1
//                            )
//                        hoveredFilledBackgroundColor =
//                            mtrlFilledBackgroundColorStateList.getColorForState(
//                                intArrayOf(android.R.attr.state_hovered),
//                                -1
//                            )
//                    }
//                } else {
//                    boxBackgroundColor = Color.TRANSPARENT
//                    defaultFilledBackgroundColor = Color.TRANSPARENT
//                    disabledFilledBackgroundColor = Color.TRANSPARENT
//                    focusedFilledBackgroundColor = Color.TRANSPARENT
//                    hoveredFilledBackgroundColor = Color.TRANSPARENT
//                }
//
//                if (a.hasValue(R.styleable.TextInputLayout_android_textColorHint)) {
//                    focusedTextColor =
//                        a.getColorStateList(R.styleable.TextInputLayout_android_textColorHint)
//                    defaultHintTextColor = focusedTextColor
//                }
//
//                val boxStrokeColorStateList: ColorStateList = MaterialResources.getColorStateList(
//                    context,
//                    a,
//                    R.styleable.TextInputLayout_boxStrokeColor
//                )
//                // Default values for stroke colors if boxStrokeColorStateList is not stateful
//                // Default values for stroke colors if boxStrokeColorStateList is not stateful
//                focusedStrokeColor =
//                    a.getColor(R.styleable.TextInputLayout_boxStrokeColor, Color.TRANSPARENT)
//                defaultStrokeColor =
//                    ContextCompat.getColor(context, R.color.mtrl_textinput_default_box_stroke_color)
//                disabledColor =
//                    ContextCompat.getColor(context, R.color.mtrl_textinput_disabled_color)
//                hoveredStrokeColor =
//                    ContextCompat.getColor(context, R.color.mtrl_textinput_hovered_box_stroke_color)
//                // Values from boxStrokeColorStateList
//                // Values from boxStrokeColorStateList
//                if (boxStrokeColorStateList != null) {
//                    setBoxStrokeColorStateList(boxStrokeColorStateList)
//                }
//                if (a.hasValue(R.styleable.TextInputLayout_boxStrokeErrorColor)) {
//                    boxStrokeErrorColor = MaterialResources.getColorStateList(
//                        context, a, R.styleable.TextInputLayout_boxStrokeErrorColor
//                    )
//                }

                // We've read the attributes ourselves. Now clear the Filled TextInputLayout background's stroke
                boxStrokeWidth = 0
                boxStrokeWidthFocused = 0

                val boxBackground = MaterialShapeDrawable(shapeAppearanceModel)
                boxBackground.fillColor = ColorStateList.valueOf(boxBackgroundColor)

//                editText.background = getOutlinedBoxBackgroundWithRipple(context, boxBackground, EDIT_TEXT_BACKGROUND_RIPPLE_STATE)

            } finally {
                attributes.recycle()
            }
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
        // TODO remove logging
        Timber.e("setTextPaddingVertical topPx=$topPx bottomPx=$bottomPx")
        val top = if (topPx != NO_VALUE_INT) topPx else editText.paddingTop
        val bottom = if (bottomPx != NO_VALUE_INT) bottomPx else editText.paddingBottom
        editText.updatePadding(top = top, bottom = bottom)
    }

    fun setTextPaddingHorizontal(@Dimension(unit = Dimension.PX) paddingPx: Int) {
        // TODO remove logging
        Timber.e("setTextPaddingHorizontal paddingPx=$paddingPx")
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
            if (!stateRestore) {
                listener?.onTextChanged(text.toString())
                // Only reset errors when the user enters text
                clearError()
            }
        }
    }

    private fun getOutlinedBoxBackgroundWithRipple(
        context: Context,
        boxBackground: MaterialShapeDrawable,
        states: Array<IntArray>
    ): Drawable {
        val rippleColor = MaterialColors.getColor(editText, com.google.android.material.R.attr.colorControlHighlight)
        val surfaceColor = MaterialColors.getColor(context, com.google.android.material.R.attr.colorSurface, "TextInputLayout")
        val rippleBackground = MaterialShapeDrawable(boxBackground.shapeAppearanceModel)
        val pressedBackgroundColor = MaterialColors.layer(rippleColor, surfaceColor, 0.1f)
        val rippleBackgroundColors = intArrayOf(pressedBackgroundColor, Color.TRANSPARENT)
        rippleBackground.fillColor = ColorStateList(states, rippleBackgroundColors)
        rippleBackground.setTint(surfaceColor)
        val colors = intArrayOf(pressedBackgroundColor, surfaceColor)
        val rippleColorStateList = ColorStateList(states, colors)
        val mask = MaterialShapeDrawable(boxBackground.shapeAppearanceModel)
        mask.setTint(Color.WHITE)
        val rippleDrawable: Drawable = RippleDrawable(rippleColorStateList, rippleBackground, mask)
        val layers = arrayOf(rippleDrawable, boxBackground)
        return LayerDrawable(layers)
    }

    companion object {
        private const val STATE_ERROR = "LabTextField.STATE_ERROR"
        private const val STATE_PARENT = "LabTextField.STATE_PARENT"

        private const val NO_VALUE_INT: Int = -1
        private val EDIT_TEXT_BACKGROUND_RIPPLE_STATE = arrayOf(intArrayOf(android.R.attr.state_pressed), intArrayOf())

        const val ID_EDIT_TEXT = android.R.id.text1
    }
}

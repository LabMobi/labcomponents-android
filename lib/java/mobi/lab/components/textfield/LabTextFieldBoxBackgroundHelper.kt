package mobi.lab.components.textfield

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import mobi.lab.components.R
import mobi.lab.components.shared.ResourceUtil

/**
 * Helper class to take care of updating and creating a Drawable to use as LabTextField's EditText background.
 * The background is referred to as a 'box'.
 */
internal class LabTextFieldBoxBackgroundHelper(
    private val textField: LabTextField,
    attrs: AttributeSet?,
    defStyleAttr: Int
) {

    var boxStrokeWidthDefaultPx: Int = context.resources.getDimensionPixelSize(R.dimen.lab_internal_textfield_box_stroke_width)
        set(value) {
            field = value
            updateBoxState()
        }

    var boxStrokeWidthFocusedPx: Int = context.resources.getDimensionPixelSize(R.dimen.lab_internal_textfield_box_stroke_width_focused)
        set(value) {
            field = value
            updateBoxState()
        }

    var boxStrokeColor: ColorStateList? = null
        set(value) {
            field = value
            updateBoxState()
        }

    var boxBackgroundColor: ColorStateList? = null
        set(value) {
            field = value
            updateBoxState()
        }

    private var boxCornerRadiusTopStartPx: Float = NO_VALUE_FLOAT
    private var boxCornerRadiusTopEndPx: Float = NO_VALUE_FLOAT
    private var boxCornerRadiusBottomStartPx: Float = NO_VALUE_FLOAT
    private var boxCornerRadiusBottomEndPx: Float = NO_VALUE_FLOAT

    private val context: Context
        get() = textField.context

    private var shapeAppearanceModel: ShapeAppearanceModel
    private var boxBackground: MaterialShapeDrawable
    private var boxStrokeWidthPx: Int = boxStrokeWidthDefaultPx

    init {
        val context = textField.context

        shapeAppearanceModel = ShapeAppearanceModel.builder(context, attrs, defStyleAttr, R.style.Widget_Lab_TextField).build()
        boxBackground = MaterialShapeDrawable(shapeAppearanceModel)

        attrs?.let {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.LabTextField, 0, defStyleAttr)
            try {
                boxBackgroundColor = ResourceUtil.getColorStateList(context, attributes, R.styleable.LabTextField_boxBackgroundColor)
                boxStrokeColor = ResourceUtil.getColorStateList(context, attributes, R.styleable.LabTextField_boxStrokeColor)

                boxStrokeWidthDefaultPx = attributes.getDimensionPixelSize(R.styleable.LabTextField_boxStrokeWidth, boxStrokeWidthDefaultPx)
                boxStrokeWidthFocusedPx = attributes.getDimensionPixelSize(R.styleable.LabTextField_boxStrokeWidthFocused, boxStrokeWidthFocusedPx)
                boxStrokeWidthPx = boxStrokeWidthDefaultPx

                boxCornerRadiusTopStartPx = attributes.getDimension(R.styleable.LabTextField_boxCornerRadiusTopStart, NO_VALUE_FLOAT)
                boxCornerRadiusTopEndPx = attributes.getDimension(R.styleable.LabTextField_boxCornerRadiusTopEnd, NO_VALUE_FLOAT)
                boxCornerRadiusBottomStartPx = attributes.getDimension(R.styleable.LabTextField_boxCornerRadiusBottomStart, NO_VALUE_FLOAT)
                boxCornerRadiusBottomEndPx = attributes.getDimension(R.styleable.LabTextField_boxCornerRadiusBottomEnd, NO_VALUE_FLOAT)

                // We've read the attributes ourselves. Now clear the Filled TextInputLayout background's stroke
                textField.boxStrokeWidth = 0
                textField.boxStrokeWidthFocused = 0

                updateShapeAppearanceModel()
            } finally {
                attributes.recycle()
            }
        }
    }

    fun setBoxCornerRadii(topStartPx: Float, topEndPx: Float, bottomStartPx: Float, bottomEndPx: Float) {
        boxCornerRadiusTopStartPx = topStartPx
        boxCornerRadiusTopEndPx = topEndPx
        boxCornerRadiusBottomStartPx = bottomStartPx
        boxCornerRadiusBottomEndPx = bottomEndPx

        updateShapeAppearanceModel()
        updateBoxState()
    }

    @Suppress("FoldInitializerAndIfToElvis")
    fun updateBoxState() {
        val editText = textField.editText
        if (editText == null) {
            return
        }

        val hasFocus = textField.isFocused || editText.hasFocus()
        val isEnabled = textField.isEnabled

        // Update attribute values
        if (hasFocus && isEnabled) {
            boxStrokeWidthPx = boxStrokeWidthFocusedPx
        } else {
            boxStrokeWidthPx = boxStrokeWidthDefaultPx
        }

        // Apply attributes
        if (boxBackground.shapeAppearanceModel !== shapeAppearanceModel) {
            boxBackground.shapeAppearanceModel = shapeAppearanceModel
        }
        boxBackground.setStroke(boxStrokeWidthPx.toFloat(), boxStrokeColor)
        boxBackground.fillColor = boxBackgroundColor

        editText.background = boxBackground
    }

    private fun updateShapeAppearanceModel() {
        val shapeBuilder = shapeAppearanceModel.toBuilder()
        if (boxCornerRadiusTopStartPx >= 0) {
            shapeBuilder.setTopLeftCornerSize(boxCornerRadiusTopStartPx)
        }
        if (boxCornerRadiusTopEndPx >= 0) {
            shapeBuilder.setTopRightCornerSize(boxCornerRadiusTopEndPx)
        }
        if (boxCornerRadiusBottomEndPx >= 0) {
            shapeBuilder.setBottomRightCornerSize(boxCornerRadiusBottomEndPx)
        }
        if (boxCornerRadiusBottomStartPx >= 0) {
            shapeBuilder.setBottomLeftCornerSize(boxCornerRadiusBottomStartPx)
        }
        shapeAppearanceModel = shapeBuilder.build()
    }

    internal companion object {
        internal const val NO_VALUE_FLOAT = -1f
    }
}

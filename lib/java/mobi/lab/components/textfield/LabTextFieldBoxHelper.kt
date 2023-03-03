package mobi.lab.components.textfield

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import com.google.android.material.color.MaterialColors
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import mobi.lab.components.R
import mobi.lab.components.shared.util.ResourceUtil

internal class LabTextFieldBoxHelper(
    private val textField: LabTextField,
    attrs: AttributeSet?,
    defStyleAttr: Int
) {

    private val context: Context
        get() = textField.context

    private var shapeAppearanceModel: ShapeAppearanceModel
    private var boxBackground: MaterialShapeDrawable
    private var boxStrokeWidthDefaultPx: Int = context.resources.getDimensionPixelSize(R.dimen.lab_default_textfield_box_stroke_width)
    private var boxStrokeWidthFocusedPx: Int = context.resources.getDimensionPixelSize(R.dimen.lab_default_textfield_box_stroke_width_focused)
    private var boxStrokeWidthPx: Int = boxStrokeWidthDefaultPx
    private var boxStrokeColor: ColorStateList? = null
    private var boxBackgroundColor: ColorStateList? = null

    init {
        val context = textField.context

        shapeAppearanceModel = ShapeAppearanceModel.builder(context, attrs, defStyleAttr, R.style.Lab_Widget_TextField).build()
        boxBackground = MaterialShapeDrawable(shapeAppearanceModel)

        attrs?.let {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.LabTextField, 0, defStyleAttr)
            try {
                boxBackgroundColor = ResourceUtil.getColorStateList(context, attributes, R.styleable.LabTextField_boxBackgroundColor)
                boxStrokeColor = ResourceUtil.getColorStateList(context, attributes, R.styleable.LabTextField_boxStrokeColor)

                boxStrokeWidthDefaultPx = attributes.getDimensionPixelSize(R.styleable.LabTextField_boxStrokeWidth, boxStrokeWidthDefaultPx)
                boxStrokeWidthFocusedPx = attributes.getDimensionPixelSize(R.styleable.LabTextField_boxStrokeWidthFocused, boxStrokeWidthFocusedPx)
                boxStrokeWidthPx = boxStrokeWidthDefaultPx

                val boxCornerRadiusTopStart = attributes.getDimension(R.styleable.LabTextField_boxCornerRadiusTopStart, -1f)
                val boxCornerRadiusTopEnd = attributes.getDimension(R.styleable.LabTextField_boxCornerRadiusTopEnd, -1f)
                val boxCornerRadiusBottomStart = attributes.getDimension(R.styleable.LabTextField_boxCornerRadiusBottomStart, -1f)
                val boxCornerRadiusBottomEnd = attributes.getDimension(R.styleable.LabTextField_boxCornerRadiusBottomEnd, -1f)

                val shapeBuilder = shapeAppearanceModel.toBuilder()
                if (boxCornerRadiusTopStart >= 0) {
                    shapeBuilder.setTopLeftCornerSize(boxCornerRadiusTopStart)
                }
                if (boxCornerRadiusTopEnd >= 0) {
                    shapeBuilder.setTopRightCornerSize(boxCornerRadiusTopEnd)
                }
                if (boxCornerRadiusBottomEnd >= 0) {
                    shapeBuilder.setBottomRightCornerSize(boxCornerRadiusBottomEnd)
                }
                if (boxCornerRadiusBottomStart >= 0) {
                    shapeBuilder.setBottomLeftCornerSize(boxCornerRadiusBottomStart)
                }
                shapeAppearanceModel = shapeBuilder.build()


                // We've read the attributes ourselves. Now clear the Filled TextInputLayout background's stroke
                textField.boxStrokeWidth = 0
                textField.boxStrokeWidthFocused = 0
            } finally {
                attributes.recycle()
            }
        }

        textField.editText.background = boxBackground
    }

    fun updateBoxState() {
        val editText = textField.editText

        val hasFocus = textField.isFocused || editText.hasFocus()
        val isEnabled = textField.isEnabled

        // Update attribute values
        if (hasFocus && isEnabled) {
            boxStrokeWidthPx = boxStrokeWidthFocusedPx;
        } else {
            boxStrokeWidthPx = boxStrokeWidthDefaultPx;
        }

        // Apply attributes
        if (boxBackground.shapeAppearanceModel !== shapeAppearanceModel) {
            boxBackground.shapeAppearanceModel = shapeAppearanceModel
        }
        boxBackground.setStroke(boxStrokeWidthPx.toFloat(), boxStrokeColor)
        boxBackground.fillColor = boxBackgroundColor
    }
}

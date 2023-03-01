package mobi.lab.components.textfield

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.Dimension
import androidx.annotation.StringRes
import androidx.core.view.updatePadding
import mobi.lab.components.R
import mobi.lab.components.databinding.LabViewTextFieldBinding
import timber.log.Timber

/**
 * OnFocusChangedListener is overwritten in onAttachedToWindow
 */
class LabTextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    interface Listener {
        fun onTextChanged(text: String?)
        fun onFocusChanged(hasFocus: Boolean)
        fun onErrorCleared()
    }

    var listener: Listener? = null

    private val binding = LabViewTextFieldBinding.inflate(LayoutInflater.from(context), this)

    private val inputListener = object : LabTextInputLayout.Listener {
        override fun onTextChanged(text: String?) {
            listener?.onTextChanged(text)
        }

        override fun onErrorCleared() {
            listener?.onErrorCleared()
        }
    }

    init {
        attrs?.let {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.LabTextField, 0, defStyleAttr)
            try {
                val textPaddingLeft = attributes.getDimensionPixelSize(R.styleable.LabTextField_textPaddingLeft, NO_VALUE_INT)
                val textPaddingTop = attributes.getDimensionPixelSize(R.styleable.LabTextField_textPaddingTop, NO_VALUE_INT)
                val textPaddingRight = attributes.getDimensionPixelSize(R.styleable.LabTextField_textPaddingRight, NO_VALUE_INT)
                val textPaddingBottom = attributes.getDimensionPixelSize(R.styleable.LabTextField_textPaddingBottom, NO_VALUE_INT)
                setTextPaddingInternal(leftPx = textPaddingLeft, topPx = textPaddingTop, rightPx = textPaddingRight, bottomPx = textPaddingBottom)

                val boxCollapsedPaddingTop = attributes.getDimensionPixelSize(R.styleable.LabTextField_boxCollapsedPaddingTop, NO_VALUE_INT)
                if (boxCollapsedPaddingTop != NO_VALUE_INT) {
                    binding.inputLayout.boxCollapsedPaddingTop = boxCollapsedPaddingTop
                }
            } finally {
                attributes.recycle()
            }
        }

        setAddStatesFromChildren(true)
        descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS
    }

    fun getText(): String {
        return binding.inputLayout.getText()
    }

    fun setText(text: CharSequence?) {
        binding.inputLayout.listener = null
        binding.inputLayout.setText(text)
        binding.inputLayout.listener = inputListener
    }

    fun setTextAndSelection(text: CharSequence?) {
        setText(text)
        binding.editText.apply {
            val value = getText()?.toString() ?: ""
            setSelection(value.length)
        }
    }

    fun setHint(@StringRes textId: Int) {
        setHint(resources.getString(textId))
    }

    fun setHint(text: CharSequence?) {
        binding.inputLayout.hint = text
    }

    fun setPlaceholderText(text: CharSequence?) {
        binding.inputLayout.placeholderText = text
    }

    fun setError(text: CharSequence?) {
        binding.inputLayout.error = text
    }

    fun setInputType(inputType: Int) {
        binding.apply {
            if (editText.inputType != inputType) {
                // Some input types change the typeface. Let's reuse the old typeface
                val typeface = editText.typeface
                editText.inputType = inputType
                editText.typeface = typeface
            }
        }
    }

    fun setImeAction(imeAction: Int, onImeAction: () -> Unit) {
        binding.editText.apply {
            if (imeOptions != imeAction) {
                imeOptions = imeAction
            }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == imeAction) {
                    onImeAction()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
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

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.inputLayout.isEnabled = enabled
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            listener?.onFocusChanged(hasFocus)
        }
        binding.inputLayout.listener = inputListener
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding.editText.onFocusChangeListener = null
        binding.inputLayout.listener = null
    }

    // TODO remove
    private fun updateAlpha(view: View, alpha: Float, animate: Boolean = true) {
        if (view.alpha == alpha) {
            return
        }
        if (animate) {
            view.animate()
                .alpha(alpha)
                .setDuration(DURATION_ANIMATION_MS)
                .start()
        } else {
            view.alpha = alpha
        }
    }

    private fun setTextPaddingInternal(leftPx: Int, topPx: Int, rightPx: Int, bottomPx: Int) {
        // TODO remove logging
        Timber.e("setTextPaddingInternal leftPx=$leftPx topPx=$topPx rightPx=$rightPx bottomPx=$bottomPx")
        binding.apply {
            val noValue = NO_VALUE_INT
            val left = if (leftPx != noValue) leftPx else editText.paddingLeft
            val top = if (topPx != noValue) topPx else editText.paddingTop
            val right = if (rightPx != noValue) rightPx else editText.paddingRight
            val bottom = if (bottomPx != noValue) bottomPx else editText.paddingBottom

            editText.updatePadding(left = left, top = top, right = right, bottom = bottom)
        }
    }

    companion object {
        private const val DURATION_ANIMATION_MS = 60L
        private const val NO_VALUE_INT: Int = -1
    }
}

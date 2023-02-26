package mobi.lab.components.ui.textfield

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import mobi.lab.components.ui.textfield.databinding.ViewTextFieldBinding

/**
 * OnFocusChangedListener is overwritten in onAttachedToWindow
 */
class LabTextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    interface Listener {
        fun onTextChanged(text: String?)
        fun onFocusChanged(hasFocus: Boolean)
        fun onErrorCleared()
    }

    var listener: Listener? = null

    private val binding = ViewTextFieldBinding.inflate(LayoutInflater.from(context), this)
    private var hintValue: CharSequence? = null

    private var isInit: Boolean = true

    private val inputListener = object : LabTextInputLayout.Listener {
        override fun onTextChanged(text: String?) {
            listener?.onTextChanged(text)
        }

        override fun onErrorCleared() {
            listener?.onErrorCleared()
        }
    }

    init {
        setAddStatesFromChildren(true)
        descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS
        binding.containerBackground.setOnClickListener {
            binding.editText.requestFocus()
            binding.editText.performClick()
        }
    }

    fun getText(): String {
        return binding.inputLayout.getText()
    }

    fun setText(text: CharSequence?) {
        binding.inputLayout.listener = null
        binding.inputLayout.setText(text)
        binding.inputLayout.listener = inputListener
        updateHintLayout()
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
        hintValue = text
        binding.inputLayout.hint = text
        updateHintLayout()
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

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.textError.isEnabled = enabled
        binding.inputLayout.isEnabled = enabled
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            updateHintLayout(hasFocus)
            listener?.onFocusChanged(hasFocus)
        }
        binding.inputLayout.listener = inputListener
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding.editText.onFocusChangeListener = null
        binding.inputLayout.listener = null
    }

    private fun updateHintLayout(hasFocus: Boolean = hasFocus()) {
        val animate = !isInit
        isInit = false
        binding.apply {
            val replaceTextWithHint = TextUtils.isEmpty(getText()) && !hasFocus

            if (replaceTextWithHint) {
                editText.hint = hintValue
                updateAlpha(textError, 0.0f, animate = animate)
            } else {
                editText.hint = null
                updateAlpha(textError, 1.0f, animate = animate)
            }
        }
    }

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

    companion object {
        private const val DURATION_ANIMATION_MS = 60L
    }
}

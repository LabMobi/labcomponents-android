package mobi.lab.components.demo

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import mobi.lab.components.demo.databinding.ActivityTextFieldBinding
import mobi.lab.components.textfield.LabTextField

class TextFieldActivity : BaseComponentActivity() {

    private lateinit var binding: ActivityTextFieldBinding

    override fun getToolbar(): Toolbar {
        return binding.toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextFieldBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.apply {
            switchEnabled.setOnCheckedChangeListener { _, isChecked ->
                loopTextFields { it.isEnabled = isChecked }
            }
            switchError.setOnCheckedChangeListener { _, isChecked ->
                var errorText = ""
                if (isChecked) {
                    // TODO read from error input
//                    errorText = textField2.getText()
                    if (errorText.isEmpty()) {
                        errorText = "This is an error!"
                    }
                }
                loopTextFields { it.error = errorText }
            }
        }
    }

    private fun loopTextFields(action: (LabTextField) -> Unit) {
        listOf(
            binding.textFieldNoIcons,
            binding.textFieldStartIcon,
            binding.textFieldEndIcon,
            binding.textFieldBothIcons
        ).map(action)
    }

}

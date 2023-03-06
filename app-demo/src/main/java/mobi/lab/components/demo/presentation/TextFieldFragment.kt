package mobi.lab.components.demo.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import mobi.lab.components.demo.databinding.FragmentTextFieldBinding
import mobi.lab.components.demo.util.FragmentBindingHolder
import mobi.lab.components.demo.util.ViewBindingHolder
import mobi.lab.components.textfield.LabTextField

class TextFieldFragment : Fragment(), ViewBindingHolder<FragmentTextFieldBinding> by FragmentBindingHolder() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return createBinding(FragmentTextFieldBinding.inflate(inflater), this) {
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

    private fun FragmentTextFieldBinding.loopTextFields(action: (LabTextField) -> Unit) {
        listOf(
            textFieldNoIcons,
            textFieldStartIcon,
            textFieldEndIcon,
            textFieldBothIcons
        ).map(action)
    }
}

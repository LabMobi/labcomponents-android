package mobi.lab.components.demo.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import mobi.lab.components.demo.databinding.FragmentTextFieldBinding
import mobi.lab.components.demo.util.FragmentBindingHolder
import mobi.lab.components.demo.util.ViewBindingHolder
import mobi.lab.components.textfield.LabTextField

class TextFieldFragment : Fragment(), ViewBindingHolder<FragmentTextFieldBinding> by FragmentBindingHolder() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return createBinding(FragmentTextFieldBinding.inflate(inflater), this) {
            switchEnabled.setOnCheckedChangeListener { _, isChecked ->
                loopTextFields { isEnabled = isChecked }
            }
            switchError.setOnCheckedChangeListener { _, isChecked ->
                var errorText = ""
                if (isChecked) {
                    errorText = inputError.getText()
                    if (errorText.isEmpty()) {
                        errorText = "This is an error!"
                    }
                }
                loopTextFields { error = errorText }
            }

            buttonUpdate.setOnClickListener { updateTextFields() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val host = activity
        if (host is MenuHost) {
            val menuProvider = object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    val item = menu.add(0, MENU_ID_UPDATE, 0, "Update")
                    item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    if (menuItem.itemId == MENU_ID_UPDATE) {
                        binding?.updateTextFields()
                        return true
                    }
                    return false
                }
            }
            host.addMenuProvider(menuProvider, viewLifecycleOwner)
        }
    }

    private fun FragmentTextFieldBinding.updateTextFields() {
        val binding = this
        val helper = inputHelper.getText()
        val error = inputError.getText()
        val label = inputLabel.getText()
        val placeholder = inputPlaceholder.getText()
        val prefix = inputPrefix.getText()
        val suffix = inputSuffix.getText()
        val counterMax = inputCounterMax.getText().toIntOrNull() ?: -1

        loopTextFields {
            helperText = helper
            if (binding.switchError.isChecked) {
                setError(error)
            }
            hint = label
            placeholderText = placeholder
            prefixText = prefix
            suffixText = suffix
            if (counterMax > 0) {
                counterMaxLength = counterMax
                isCounterEnabled = true
            } else {
                isCounterEnabled = false
            }
        }
    }

    private fun FragmentTextFieldBinding.loopTextFields(action: LabTextField.() -> Unit) {
        listOf(
            textFieldNoIcons,
            textFieldStartIcon,
            textFieldEndIcon,
            textFieldBothIcons
        ).map(action)
    }

    companion object {
        private const val MENU_ID_UPDATE = 100
    }
}

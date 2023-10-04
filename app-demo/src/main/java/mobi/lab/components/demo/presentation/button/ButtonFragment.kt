package mobi.lab.components.demo.presentation.button

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import mobi.lab.components.button.LabButton
import mobi.lab.components.demo.databinding.FragmentButtonBinding
import mobi.lab.components.demo.util.FragmentBindingHolder
import mobi.lab.components.demo.util.ViewBindingHolder

class ButtonFragment : Fragment(), ViewBindingHolder<FragmentButtonBinding> by FragmentBindingHolder() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return createBinding(FragmentButtonBinding.inflate(inflater, container, false), this) {
            switchEnabled.setOnCheckedChangeListener { _, isChecked ->
                loopButtons { isEnabled = isChecked }
            }
        }
    }

    private fun FragmentButtonBinding.loopButtons(action: LabButton.() -> Unit) {
        listOf(
            filled, filledIconStart, filledIconEnd,
            filledBig, filledBigIconStart, filledBigIconEnd,
            tonal, tonalIconStart, tonalIconEnd,
            tonalBig, tonalBigIconStart, tonalBigIconEnd,
            outlined, outlinedIconStart, outlinedIconEnd,
            outlinedBig, outlinedBigIconStart, outlinedBigIconEnd,
        ).map(action)
    }
}

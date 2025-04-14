package mobi.lab.components.demo.presentation.button

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import mobi.lab.components.button.LabButton
import mobi.lab.components.demo.R
import mobi.lab.components.demo.databinding.FragmentButtonBinding
import mobi.lab.components.demo.util.EdgeToEdgeSpec
import mobi.lab.components.demo.util.EdgeToEdgeUtil
import mobi.lab.components.demo.util.FragmentBindingHolder
import mobi.lab.components.demo.util.ViewBindingHolder

class ButtonFragment : Fragment(), ViewBindingHolder<FragmentButtonBinding> by FragmentBindingHolder() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return createBinding(FragmentButtonBinding.inflate(inflater, container, false), this) {
            switchEnabled.setOnCheckedChangeListener { _, isChecked ->
                loopButtons { isEnabled = isChecked }
            }
            EdgeToEdgeUtil.applyPaddings(containerButtons, EdgeToEdgeSpec.AVOID_BAR_AND_CUTOUT_SET_BOTTOM, true)
        }
    }

    private fun FragmentButtonBinding.loopButtons(action: LabButton.() -> Unit) {
        listOf(
            filled, filledIconStart, filledIconEnd,
            filledSmall, filledSmallIconStart, filledSmallIconEnd,
            tonal, tonalIconStart, tonalIconEnd,
            tonalSmall, tonalSmallIconStart, tonalSmallIconEnd,
            outlined, outlinedIconStart, outlinedIconEnd,
            outlinedSmall, outlinedSmallIconStart, outlinedSmallIconEnd,
            text, textIconStart, textIconEnd,
            textSmall, textSmallIconStart, textSmallIconEnd,
        ).map(action)
    }
}

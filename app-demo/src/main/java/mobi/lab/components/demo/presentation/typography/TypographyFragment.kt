package mobi.lab.components.demo.presentation.typography

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import mobi.lab.components.demo.databinding.FragmentTypographyBinding
import mobi.lab.components.demo.util.EdgeToEdgeSpec
import mobi.lab.components.demo.util.EdgeToEdgeUtil
import mobi.lab.components.demo.util.FragmentBindingHolder
import mobi.lab.components.demo.util.ViewBindingHolder

class TypographyFragment : Fragment(), ViewBindingHolder<FragmentTypographyBinding> by FragmentBindingHolder() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return createBinding(FragmentTypographyBinding.inflate(inflater), this) {
            EdgeToEdgeUtil.applyPaddings(containerTypography, EdgeToEdgeSpec.AVOID_BAR_AND_CUTOUT_SET_BOTTOM, true)
        }
    }
}

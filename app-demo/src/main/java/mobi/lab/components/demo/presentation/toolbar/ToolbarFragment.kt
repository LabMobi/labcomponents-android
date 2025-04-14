package mobi.lab.components.demo.presentation.toolbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import mobi.lab.components.demo.R
import mobi.lab.components.demo.databinding.FragmentToolbarBinding
import mobi.lab.components.demo.util.EdgeToEdgeSpec
import mobi.lab.components.demo.util.EdgeToEdgeUtil
import mobi.lab.components.demo.util.FragmentBindingHolder
import mobi.lab.components.demo.util.ViewBindingHolder
import mobi.lab.components.toolbar.LabToolbar

class ToolbarFragment : Fragment(), ViewBindingHolder<FragmentToolbarBinding> by FragmentBindingHolder() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return createBinding(FragmentToolbarBinding.inflate(inflater), this) {
            initToolbar(toolbarElevation)
            initToolbar(toolbarNoElevation)
            EdgeToEdgeUtil.applyPaddings(containerToolbars, EdgeToEdgeSpec.AVOID_BAR_AND_CUTOUT_SET_BOTTOM, true)
        }
    }

    private fun initToolbar(toolbar: LabToolbar) {
        toolbar.enableNavigationIcon {
            findNavController().popBackStack()
        }

        toolbar.inflateMenu(R.menu.toolbar_demo)
    }
}

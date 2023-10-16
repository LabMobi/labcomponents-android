package mobi.lab.components.demo.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import mobi.lab.components.demo.R
import mobi.lab.components.demo.databinding.FragmentComponentListBinding
import mobi.lab.components.demo.util.FragmentBindingHolder
import mobi.lab.components.demo.util.ViewBindingHolder

class ComponentListFragment : Fragment(), ViewBindingHolder<FragmentComponentListBinding> by FragmentBindingHolder() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return createBinding(FragmentComponentListBinding.inflate(inflater), this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        requireBinding {
            itemButtons.setOnClickListener { navController.navigate(R.id.action_componentListFragment_to_buttonFragment) }
            itemColors.setOnClickListener { navController.navigate(R.id.action_componentListFragment_to_colorsFragment) }
            itemTextField.setOnClickListener { navController.navigate(R.id.action_componentListFragment_to_textFieldFragment) }
            itemToolbar.setOnClickListener { navController.navigate(R.id.action_componentListFragment_to_toolbarFragment) }
            itemTypography.setOnClickListener { navController.navigate(R.id.action_componentListFragment_to_typographyFragment) }
        }
    }
}

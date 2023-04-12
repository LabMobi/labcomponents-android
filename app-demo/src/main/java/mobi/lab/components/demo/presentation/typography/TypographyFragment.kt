package mobi.lab.components.demo.presentation.typography

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import mobi.lab.components.demo.databinding.FragmentTypographyBinding

class TypographyFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentTypographyBinding.inflate(inflater, container, false).root
    }
}

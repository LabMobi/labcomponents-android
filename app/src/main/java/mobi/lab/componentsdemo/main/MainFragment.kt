package mobi.lab.componentsdemo.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import mobi.lab.componentsdemo.BuildConfig
import mobi.lab.componentsdemo.R
import mobi.lab.componentsdemo.app.common.exhaustive
import mobi.lab.componentsdemo.common.BaseFragment
import mobi.lab.componentsdemo.common.FragmentBindingHolder
import mobi.lab.componentsdemo.common.ViewBindingHolder
import mobi.lab.componentsdemo.common.ViewModelFactory
import mobi.lab.componentsdemo.common.debug.DebugActions
import mobi.lab.componentsdemo.common.platform.LogoutMonitor
import mobi.lab.componentsdemo.common.util.NavUtil
import mobi.lab.componentsdemo.databinding.FragmentMainBinding
import mobi.lab.componentsdemo.di.Injector
import mobi.lab.componentsdemo.prototype.PrototypeActivity
import javax.inject.Inject

class MainFragment : BaseFragment(), ViewBindingHolder<FragmentMainBinding> by FragmentBindingHolder() {

    @Inject lateinit var debugActions: DebugActions

    @Inject lateinit var factory: ViewModelFactory

    private val viewModel: MainViewModel by viewModels { factory }

    init {
        Injector.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogoutMonitor.reset() // Reset logout monitor. If we can see this screen, then we have a valid session
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return createBinding(FragmentMainBinding.inflate(inflater), this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireBinding {
            initToolbar(this)
            buttonOpen.setOnClickListener { viewModel.onOpenPrototypeClicked() }
        }

        /**
         * Init ViewModel in onViewCreated as they are connected to View's lifecycle.
         */
        viewModel.action.onEachEvent { event ->
            when (event) {
                is MainViewModel.Action.OpenWebLink -> openPrototype(event.url)
                is MainViewModel.Action.RestartApplication -> restartApplication()
                is MainViewModel.Action.OpenDebug -> openDebug().exhaustive
            }
        }
    }

    private fun initToolbar(binding: FragmentMainBinding) {
        if (BuildConfig.DEBUG) {
            binding.toolbar.inflateMenu(R.menu.debug_toolbar)
            binding.toolbar.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.menu_logout) {
                    viewModel.onLogoutClicked()
                    return@setOnMenuItemClickListener true
                }
                if (item.itemId == R.id.button_debug) {
                    viewModel.onDebugClicked()
                    return@setOnMenuItemClickListener true
                }

                return@setOnMenuItemClickListener false
            }
        } else {
            binding.toolbar.inflateMenu(R.menu.main_toolbar)
            binding.toolbar.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.menu_logout) {
                    viewModel.onLogoutClicked()
                    return@setOnMenuItemClickListener true
                }
                return@setOnMenuItemClickListener false
            }
        }
    }

    private fun openPrototype(url: String) {
        // Open PrototypeActivity to demonstrate assisted injection with runtime arguments
        context?.let { startActivity(PrototypeActivity.getIntent(it, url)) }
    }

    private fun restartApplication() {
        context?.let { NavUtil.restartApplication(it) }
    }

    private fun openDebug() {
        // Open DebugActivity
        context?.let { debugActions.launchDebugActivity(it) }
    }

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}

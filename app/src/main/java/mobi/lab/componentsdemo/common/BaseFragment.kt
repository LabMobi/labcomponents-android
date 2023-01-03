package mobi.lab.componentsdemo.common

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import mobi.lab.componentsdemo.common.util.isFragmentVisible
import mobi.lab.componentsdemo.mvvm.MvvmLiveDataExtensions
import timber.log.Timber

open class BaseFragment : Fragment, MvvmLiveDataExtensions {

    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    /**
     * We want to return the View lifecycle here not the Fragment lifecycle.
     * This is important in cases where the View is destroyed but our Fragment instance is not.
     * Unless we return the View's lifecycle here, our LiveData instances are not notified of
     * data changes when the Fragment's View is recreated unless we specifically make it do so,
     * because the Observable is not destroyed when the View is destroyed.
     * @see [androidx.lifecycle.LiveData.observe] for more detailed information.
     *
     * NB! viewLifecycleOwner can't be referenced before onCreateView has returned a View.
     * This means that any ViewModel LiveData subscriptions should be done in onViewCreated or at a later stage.
     */
    override fun getLifecycleOwner(): LifecycleOwner = viewLifecycleOwner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("onCreate this=$this")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log("onViewCreated this=$this")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        log("onDestroyView this=$this")
    }

    override fun onStart() {
        super.onStart()
        log("onStart this=$this")
    }

    override fun onResume() {
        super.onResume()
        log("onResume this=$this")
    }

    override fun onPause() {
        super.onPause()
        log("onPause this=$this")
    }

    override fun onStop() {
        super.onStop()
        log("onStop this=$this")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy this=$this")
    }

    /**
     * Run functions only when a Fragment is actually visible to the user.
     * Utility function for handling EventBus events.
     */
    protected fun eventGuard(function: () -> Unit) {
        if (isFragmentVisible()) {
            function.invoke()
        } else {
            Timber.w("eventGuard event ignored this=$this")
        }
    }

    private fun log(message: String) {
        Timber.v(message)
    }
}

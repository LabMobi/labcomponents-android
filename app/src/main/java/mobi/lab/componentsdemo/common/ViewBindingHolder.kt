package mobi.lab.componentsdemo.common

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import mobi.lab.componentsdemo.app.common.className

/**
 * A ViewBinding holder interface that takes care of releasing the binding object when the View is destroyed
 *
 * Call createBinding() to save the ViewBinding object and observe View's lifecycle.
 *
 * Provides access via
 * - val binding: A nullable variable reference to the binding. Null before creation and after ON_DESTROY
 * - requireBinding(): Enforces non-null value. Throws IllegalStateException otherwise. Similar to requireActivity() and requireContext()
 */
interface ViewBindingHolder<BINDING : ViewBinding> {
    val binding: BINDING?

    /**
     * Saves the binding in the holder. Calls the optional [action] with the binding as its receiver.
     * When [fragment] View lifecycle reached ON_DESTROY state, the binding value is set to null
     * @return binding.root View
     */
    fun createBinding(binding: BINDING, fragment: Fragment, action: (BINDING.() -> Unit)? = null): View

    /**
     * Returns the non-null saved ViewBinding or throws an IllegalStateException
     * @throws IllegalStateException
     * @return BINDING instance
     */
    fun requireBinding(action: (BINDING.() -> Unit)? = null): BINDING
}

class FragmentBindingHolder<BINDING : ViewBinding> : ViewBindingHolder<BINDING>, LifecycleEventObserver {
    override var binding: BINDING? = null
    private var viewLifecycle: Lifecycle? = null
    private var name: String? = null

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event.targetState == Lifecycle.State.DESTROYED) {
            viewLifecycle?.removeObserver(this)
            viewLifecycle = null
            binding = null
        }
    }

    override fun createBinding(binding: BINDING, fragment: Fragment, action: (BINDING.() -> Unit)?): View {
        this.binding = binding

        // Observe to Fragment View's lifecycle
        viewLifecycle = fragment.viewLifecycleOwner.lifecycle
        viewLifecycle?.addObserver(this)
        name = fragment.className()

        action?.invoke(binding)
        return binding.root
    }

    @Suppress("UseCheckOrError")
    override fun requireBinding(action: (BINDING.() -> Unit)?): BINDING {
        val binding = binding
        @Suppress("FoldInitializerAndIfToElvis")
        if (binding == null) {
            throw IllegalStateException("ViewBinding access outside of Fragment($name) lifecycle")
        }
        action?.invoke(binding)
        return binding
    }
}

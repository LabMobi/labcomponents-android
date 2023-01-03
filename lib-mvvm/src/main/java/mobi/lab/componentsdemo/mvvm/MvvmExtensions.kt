package mobi.lab.componentsdemo.mvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner

fun <T : Any> MutableLiveData<T>.asLiveData() = this as LiveData<T>
fun <T : Any> MutableLiveData<T?>.asNullableLiveData() = this as LiveData<T?>

/**
 * Activity based Assisted Injection
 *
 * Convenience function to init ViewModels lazily using Assisted Injection.
 * Wraps the assisted factory invocation with a ViewModelProvider.Factory
 */
inline fun <reified VM : ViewModel> ComponentActivity.assistedViewModels(
    crossinline assistedFactory: () -> VM
): Lazy<VM> {
    return lazy(LazyThreadSafetyMode.NONE) { createViewModel(this, assistedFactory) }
}

/**
 * Fragment based Assisted Injection
 *
 * Convenience function to init ViewModels lazily using Assisted Injection.
 * Wraps the assisted factory invocation with a ViewModelProvider.Factory
 */
inline fun <reified VM : ViewModel> Fragment.assistedViewModels(
    crossinline assistedFactory: () -> VM
): Lazy<VM> {
    return lazy(LazyThreadSafetyMode.NONE) { createViewModel(this, assistedFactory) }
}

/**
 * Activity based Assisted Injection with SavedStateHandle
 *
 * Convenience function to init ViewModels lazily using Assisted Injection and get access to SavedStateHandle.
 * Wraps the assisted factory invocation with a AbstractSavedStateViewModelFactory
 */
inline fun <reified VM : ViewModel> ComponentActivity.assistedSavedStateViewModels(
    crossinline initialStateFactory: () -> Bundle? = { null },
    crossinline savedStateFactory: (handle: SavedStateHandle) -> VM
): Lazy<VM> {
    return lazy(LazyThreadSafetyMode.NONE) { createSavedStateViewModel(this, this, initialStateFactory, savedStateFactory) }
}

/**
 * Fragment based Assisted Injection with SavedStateHandle
 *
 * Convenience function to init ViewModels lazily using Assisted Injection and get access to SavedStateHandle.
 * Wraps the assisted factory invocation with a AbstractSavedStateViewModelFactory
 */
inline fun <reified VM : ViewModel> Fragment.assistedSavedStateViewModels(
    crossinline initialStateFactory: () -> Bundle? = { null },
    crossinline savedStateFactory: (handle: SavedStateHandle) -> VM
): Lazy<VM> {
    return lazy(LazyThreadSafetyMode.NONE) { createSavedStateViewModel(this, this, initialStateFactory, savedStateFactory) }
}

/**
 * Internal function to create SavedStateViewModels by invoking a custom factory
 */
inline fun <reified VM : ViewModel> createSavedStateViewModel(
    viewModelStoreOwner: ViewModelStoreOwner,
    savedStateRegistryOwner: SavedStateRegistryOwner,
    crossinline initialStateFactory: () -> Bundle?,
    crossinline savedStateFactory: (handle: SavedStateHandle) -> VM
): VM {
    // An internal factory that invokes the custom producer function
    val factory = object : AbstractSavedStateViewModelFactory(savedStateRegistryOwner, initialStateFactory.invoke()) {
        @Suppress("UNCHECKED_CAST")
        override fun <VM : ViewModel> create(key: String, modelClass: Class<VM>, handle: SavedStateHandle): VM {
            return savedStateFactory.invoke(handle) as VM
        }
    }
    return ViewModelProvider(viewModelStoreOwner, factory).get(VM::class.java)
}

/**
 * Internal function to create ViewModels by invoking a custom factory
 */
inline fun <reified VM : ViewModel> createViewModel(
    viewModelStoreOwner: ViewModelStoreOwner,
    crossinline assistedFactory: () -> VM
): VM {

    // An internal factory that invokes the custom producer function
    val factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <VM : ViewModel> create(modelClass: Class<VM>) = assistedFactory() as VM
    }
    return ViewModelProvider(viewModelStoreOwner, factory).get(VM::class.java)
}

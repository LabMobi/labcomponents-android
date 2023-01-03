package mobi.lab.componentsdemo.mvvm

import androidx.lifecycle.Observer

/**
 * An [Observer] for [SingleEvent]s, simplifying the pattern of checking if the [SingleEvent]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [SingleEvent]'s contents has not been handled.
 */
class SingleEventObserver<T>(private val unhandledContentObserver: (T) -> Unit) : Observer<SingleEvent<T>> {
    override fun onChanged(event: SingleEvent<T>?) {
        event?.getContentIfNotHandled()?.let { value ->
            unhandledContentObserver.invoke(value)
        }
    }
}

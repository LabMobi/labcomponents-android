package mobi.lab.componentsdemo.mvvm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

interface MvvmLiveDataExtensions {
    fun getLifecycleOwner(): LifecycleOwner

    fun <T> LiveData<T>.onEachNotNull(block: (T) -> Unit) {
        this.observe(getLifecycleOwner(), Observer { it?.let(block) })
    }

    fun <T> LiveData<T>.onEach(block: (T?) -> Unit) {
        this.observe(getLifecycleOwner(), Observer { block(it) })
    }

    @Suppress("UNCHECKED_CAST")
    fun <T, E : SingleEvent<T>> LiveData<E>.onEachEvent(block: (T) -> Unit) {
        this.observe(getLifecycleOwner(), SingleEventObserver<T> { block(it) } as Observer<E>)
    }
}

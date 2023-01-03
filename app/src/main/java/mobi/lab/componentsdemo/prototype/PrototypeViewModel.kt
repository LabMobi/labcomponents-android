package mobi.lab.componentsdemo.prototype

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import mobi.lab.componentsdemo.mvvm.SingleEvent
import mobi.lab.componentsdemo.mvvm.asLiveData
import timber.log.Timber

/**
 * An example ViewModel that uses AssistedInjection to get argument values at runtime.
 * prototypeUrl is provided at runtime when the ViewModel is injected.
 *
 * Other dependencies can be injected in a regular manner by adding them as constructor arguments.
 *
 * NB! Note @AssistedInject annotation for the constructor and @Assisted annotation for the assisted argument.
 */
class PrototypeViewModel @AssistedInject constructor(
    @Assisted private val prototypeUrl: String
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(prototypeUrl: String): PrototypeViewModel
    }

    private val _action = MutableLiveData<SingleEvent<Action>>()
    val action = _action.asLiveData()

    init {
        Timber.d("PrototypeViewModel init prototypeUrl=$prototypeUrl")
        _action.value = if (prototypeUrl.isBlank()) {
            SingleEvent(Action.Close)
        } else {
            SingleEvent(Action.OpenWebLinkAndClose(prototypeUrl))
        }
    }

    sealed class Action {
        data class OpenWebLinkAndClose(val url: String) : Action()
        object Close : Action()
    }
}

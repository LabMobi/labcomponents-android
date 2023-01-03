package mobi.lab.componentsdemo.login

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.parcelize.Parcelize
import mobi.lab.componentsdemo.common.rx.SchedulerProvider
import mobi.lab.componentsdemo.common.rx.dispose
import mobi.lab.componentsdemo.common.util.errorCode
import mobi.lab.componentsdemo.domain.entities.ErrorCode
import mobi.lab.componentsdemo.domain.usecases.auth.LoginUseCase
import mobi.lab.componentsdemo.mvvm.SingleEvent
import mobi.lab.componentsdemo.mvvm.asLiveData
import mobi.lab.componentsdemo.prototype.PrototypeViewModel
import java.util.concurrent.TimeUnit

/**
 * A ViewModel that gets a SavedStateHandle via AssistedFactory. This can be combined with additional @Assisted arguments.
 * @see PrototypeViewModel for generic AssistedInject usage
 */
class LoginViewModel @AssistedInject constructor(
    @Assisted private val handle: SavedStateHandle,
    private val loginUseCase: LoginUseCase,
    private val schedulers: SchedulerProvider,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(handle: SavedStateHandle): LoginViewModel
    }

    private val _action = MutableLiveData<SingleEvent<Action>>()
    val action = _action.asLiveData()

    private val _state: MutableLiveData<State> = handle.getLiveData(STATE_VIEW_MODEL_STATE, State.Default)
    val state = _state.asLiveData()

    private var disposable: Disposable? = null

    fun onLoginClicked(email: String, password: String) {
        _state.value = State.Progress
        dispose(disposable)
        disposable = delay().andThen(loginUseCase.execute(email, password)) // Small delay to demo progress UX
            .compose(schedulers.single())
            .subscribe(
                { onLoginSuccess() },
                { onLoginError(it) }
            )
    }

    override fun onCleared() {
        super.onCleared()
        dispose(disposable)
    }

    private fun onLoginSuccess() {
        _action.value = SingleEvent(Action.OpenApplication)
    }

    private fun onLoginError(error: Throwable) {
        _state.value = State.Error(error.errorCode())
    }

    private fun delay(): Completable {
        return Completable.complete().delay(LOGIN_DELAY_MILLIS, TimeUnit.MILLISECONDS, schedulers.computation)
    }

    sealed class State : Parcelable {
        @Parcelize object Default : State()
        @Parcelize object Progress : State()
        @Parcelize data class Error(val errorCode: ErrorCode) : State()
    }

    sealed class Action {
        object OpenApplication : Action()
    }

    companion object {
        private const val LOGIN_DELAY_MILLIS = 1000L
        const val STATE_VIEW_MODEL_STATE = "state_viewmodel_state"
    }
}

package mobi.lab.componentsdemo.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Single
import mobi.lab.componentsdemo.common.rx.TestSchedulerProvider
import mobi.lab.componentsdemo.domain.entities.DomainException
import mobi.lab.componentsdemo.domain.entities.ErrorCode
import mobi.lab.componentsdemo.domain.entities.Session
import mobi.lab.componentsdemo.domain.usecases.auth.LoginUseCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LoginViewModelTest {

    @get:Rule var instantExecutorRule = InstantTaskExecutorRule()

    @Mock lateinit var stateObserver: Observer<LoginViewModel.State>
    @Mock private lateinit var useCase: LoginUseCase
    @Mock private lateinit var handle: SavedStateHandle

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        whenever(handle.getLiveData<LoginViewModel.State>(eq(LoginViewModel.STATE_VIEW_MODEL_STATE), any()))
            .thenReturn(MutableLiveData(LoginViewModel.State.Default))

        viewModel = LoginViewModel(handle, useCase, TestSchedulerProvider)
    }

    @Test
    fun open_app_when_login_is_successful() {
        whenever(useCase.execute(any(), any())).thenReturn(Single.just(Session("test")))
        viewModel.state.observeForever(stateObserver)
        viewModel.onLoginClicked("email", "password")
        verify(stateObserver).onChanged(LoginViewModel.State.Default)
        verify(stateObserver).onChanged(LoginViewModel.State.Progress)
        assertEquals(viewModel.action.value!!.peekContent(), LoginViewModel.Action.OpenApplication)
    }

    @Test
    fun set_error_state_when_login_fails() {
        whenever(useCase.execute(any(), any())).thenReturn(Single.error(DomainException.unknown()))
        viewModel.state.observeForever(stateObserver)
        viewModel.onLoginClicked("email", "password")
        assertNull(viewModel.action.value)
        verify(stateObserver).onChanged(LoginViewModel.State.Default)
        verify(stateObserver).onChanged(LoginViewModel.State.Progress)
        verify(stateObserver).onChanged(LoginViewModel.State.Error(ErrorCode.UNKNOWN))
    }
}

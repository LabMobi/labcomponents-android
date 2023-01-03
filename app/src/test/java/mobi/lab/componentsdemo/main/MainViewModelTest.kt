package mobi.lab.componentsdemo.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Completable
import mobi.lab.componentsdemo.common.rx.TestSchedulerProvider
import mobi.lab.componentsdemo.domain.entities.DomainException
import mobi.lab.componentsdemo.domain.usecases.auth.LogoutUseCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

class MainViewModelTest {

    @get:Rule var instantExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var useCase: LogoutUseCase

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = MainViewModel(useCase, TestSchedulerProvider)
    }

    @Test
    fun open_link_when_open_prototype_is_clicked() {
        viewModel.onOpenPrototypeClicked()
        assertEquals(viewModel.action.value!!.peekContent(), MainViewModel.Action.OpenWebLink("https://www.google.com"))
    }

    @Test
    fun restart_app_when_logout_is_successful() {
        whenever(useCase.execute()).thenReturn(Completable.complete())
        viewModel.onLogoutClicked()
        assertEquals(viewModel.action.value!!.peekContent(), MainViewModel.Action.RestartApplication)
    }

    @Test
    fun restart_app_when_logout_fails() {
        whenever(useCase.execute()).thenReturn(Completable.error(DomainException.unknown()))
        viewModel.onLogoutClicked()
        assertEquals(viewModel.action.value!!.peekContent(), MainViewModel.Action.RestartApplication)
    }
}

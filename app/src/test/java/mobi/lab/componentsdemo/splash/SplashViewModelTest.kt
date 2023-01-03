package mobi.lab.componentsdemo.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Completable
import mobi.lab.componentsdemo.common.rx.TestSchedulerProvider
import mobi.lab.componentsdemo.domain.usecases.auth.HasValidSessionUseCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

class SplashViewModelTest {

    @get:Rule var instantExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var useCase: HasValidSessionUseCase

    private lateinit var viewModel: SplashViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun launch_app_when_session_is_valid() {
        whenever(useCase.execute()).thenReturn(Completable.complete())
        viewModel = SplashViewModel(useCase, TestSchedulerProvider)
        assertEquals(viewModel.action.value?.peekContent(), SplashViewModel.Action.LaunchApplication)
    }

    @Test
    fun launch_login_when_session_is_invalid() {
        whenever(useCase.execute()).thenReturn(Completable.error(RuntimeException()))
        viewModel = SplashViewModel(useCase, TestSchedulerProvider)
        assertEquals(viewModel.action.value?.peekContent(), SplashViewModel.Action.LaunchLogin)
    }
}

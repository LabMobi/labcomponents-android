package mobi.lab.componentsdemo.di

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import mobi.lab.componentsdemo.common.rx.SchedulerProvider
import mobi.lab.componentsdemo.util.BaseInstrumentationTest
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertSame

/**
 * A sample test showing how to inject dependencies via Dagger.
 * Verifies that our TestAppComponent gets its schedulers from TestSchedulerModule
 */
class SchedulerModuleTest : BaseInstrumentationTest() {

    @Inject
    lateinit var schedulers: SchedulerProvider

    init {
        getAppComponent().inject(this)
    }

    @Test
    fun verify_test_schedulers() {
        assertSame(schedulers.main, AndroidSchedulers.mainThread())
        assertSame(schedulers.io, AndroidSchedulers.mainThread())
        assertSame(schedulers.computation, AndroidSchedulers.mainThread())
    }
}

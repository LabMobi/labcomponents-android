package mobi.lab.componentsdemo

import mobi.lab.componentsdemo.di.AppModule
import mobi.lab.componentsdemo.di.DaggerTestAppComponent
import mobi.lab.componentsdemo.di.Injector
import mobi.lab.componentsdemo.di.TestAppComponent

/**
 * Custom Application class for tests that has its own TestAppComponent.
 * This component can be used to inject dependencies into test classes directly
 * and it is provided to Injector class instead of the application code's AppComponent.
 */
class TestApp : App() {

    val appComponent: TestAppComponent = DaggerTestAppComponent
        .builder()
        .appModule(AppModule(this))
        .build()

    override fun initDependencyInjection() {
        // Init Injector with a component with custom test modules
        Injector.buildGraph(appComponent)
    }
}

package mobi.lab.componentsdemo.util

import android.content.Context
import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import mobi.lab.componentsdemo.TestApp
import mobi.lab.componentsdemo.di.TestAppComponent

open class BaseInstrumentationTest {

    fun getContext(): Context {
        return InstrumentationRegistry.getInstrumentation().targetContext
    }

    fun getAppComponent(): TestAppComponent {
        return (getContext().applicationContext as TestApp).appComponent
    }

    /**
     * Run operations with a [ConditionIdlingResource] scoped to the function.
     * Meaning the [IdlingResource] is created, registered when the function is run and unregistered
     * after the function returns.
     */
    fun withConditionIdlingResource(action: (ConditionIdlingResource) -> Unit) {
        val resource = ConditionIdlingResource()
        try {
            IdlingRegistry.getInstance().register(resource)
            action.invoke(resource)
        } finally {
            resource.destroy()
            IdlingRegistry.getInstance().unregister(resource)
        }
    }
}

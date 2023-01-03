package mobi.lab.componentsdemo.util

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import mobi.lab.componentsdemo.TestApp

/**
 * A runner to provide a different Application class from the regular application code.
 * We need a separate Application class to change the BaseAppComponent Injector uses and
 * to provide different dependency implementations for tests as needed.
 * Referenced from app/build.gradle.
 */
@Suppress("unused")
class CustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, TestApp::class.java.name, context)
    }
}

package mobi.lab.components.demo

import android.annotation.SuppressLint
import android.app.Application
import android.os.StrictMode
import timber.log.Timber

open class App : Application() {

    @SuppressLint("LogNotTimber")
    override fun onCreate() {
        super.onCreate()
        initLogging()
        initStrictMode()
    }

    private fun initLogging() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    // Use this if actually looking for something
                    // .penaltyDeath()
                    .build()
            )
        }
    }
}

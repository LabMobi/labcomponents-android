package mobi.lab.componentsdemo

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Application
import android.os.Process
import android.os.StrictMode
import mobi.lab.componentsdemo.common.logging.ScrollsTree
import mobi.lab.componentsdemo.common.util.isDebugBuild
import mobi.lab.scrolls.LogPost
import mobi.lab.scrolls.LogPostBuilder
import mobi.lab.scrolls.LogPostImpl
import timber.log.Timber

open class App : Application() {

    @SuppressLint("LogNotTimber")
    override fun onCreate() {
        super.onCreate()

        initLogPost()
        if (isLogPostProcess()) {
            android.util.Log.d("LogPost", "Halting app onCreate as we are in logpost")
            return
        }

        initLogPostHandler()
        initLogging()
        initStrictMode()
    }

    private fun initLogging() {
        Timber.plant(ScrollsTree(this))
    }

    private fun initLogPostHandler() {
        if (!isDebugBuild()) {
            return
        }

        val defaultExceptionHandler = Thread.currentThread().uncaughtExceptionHandler
        Thread.currentThread().uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { thread, ex ->
            // Do the things we need to do
            Timber.e(ex, "FATAL EXCEPTION")

            LogPostBuilder()
                .setConfirmEnabled(true)
                .setShowResultEnabled(true)
                .addTags(LogPost.LOG_TAG_CRASH)
                .launchActivity(applicationContext)

            // Call the system default handler
            defaultExceptionHandler?.uncaughtException(thread, ex)
        }
    }

    private fun initLogPost() {
        if (!isDebugBuild()) {
            return
        }

        /* Configure posting to email */
        LogPostImpl.configure(this, BuildConfig.APPLICATION_ID + ".logs")
    }

    private fun isLogPostProcess(): Boolean {
        if (!isDebugBuild()) {
            return false
        }
        for (inf in (getSystemService(ACTIVITY_SERVICE) as ActivityManager).runningAppProcesses) {
            if (inf.pid == Process.myPid()) {
                if (".LogPostProcess" == inf.processName) {
                    return true
                }
            }
        }
        return false
    }

    private fun initStrictMode() {
        if (isDebugBuild()) {
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

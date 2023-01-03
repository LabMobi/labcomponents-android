package mobi.lab.componentsdemo.common.logging

import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashlyticsTree(private val logPriority: Int) : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority >= logPriority) {
            FirebaseCrashlytics.getInstance().log("${ensureTag(tag)}: $message")
        }
    }

    private fun ensureTag(tag: String?): String {
        return tag ?: "null"
    }
}

package mobi.lab.componentsdemo.common.logging

import android.content.Context
import mobi.lab.scrolls.Log
import mobi.lab.scrolls.LogDeleteImplAge
import mobi.lab.scrolls.LogImplCat
import mobi.lab.scrolls.LogImplComposite
import mobi.lab.scrolls.LogImplFile
import timber.log.Timber
import java.util.regex.Pattern

class ScrollsTree(context: Context) : Timber.Tree() {

    init {
        /* Init Logging to go both to logcat and file when we have a debug build*/
        LogImplFile.init(context.filesDir, LogDeleteImplAge(LogDeleteImplAge.AGE_KEEP_1_MONTH))
        LogImplComposite.init(arrayOf<Class<*>>(LogImplCat::class.java, LogImplFile::class.java))
        Log.setImplementation(LogImplComposite::class.java)
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val log = Log.getInstance(ensureTag(tag))
        when (priority) {
            android.util.Log.VERBOSE -> log.v(t, message)
            android.util.Log.DEBUG -> log.d(t, message)
            android.util.Log.INFO -> log.i(t, message)
            android.util.Log.WARN -> log.w(t, message)
            android.util.Log.ERROR -> log.e(t, message)
            android.util.Log.ASSERT -> log.wtf(t, message)
        }
    }

    private fun ensureTag(tag: String?): String {
        return tag ?: createDebugTag()
    }

    @Suppress("ThrowingExceptionsWithoutMessageOrCause")
    private fun createDebugTag(): String {
        return Throwable().stackTrace
            .first { it.className !in stackIgnore }
            .let(::createStackElementTag)
    }

    private fun createStackElementTag(element: StackTraceElement): String {
        var tag = element.className
        val m = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        return tag.substring(tag.lastIndexOf('.') + 1)
    }

    companion object {
        private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")
        private val stackIgnore = listOf(
            Timber::class.java.name,
            Timber.Forest::class.java.name,
            Timber.Tree::class.java.name,
            Timber.DebugTree::class.java.name,
            ScrollsTree::class.java.name,
        )
    }
}

package mobi.lab.components.shared.util

import android.annotation.SuppressLint
import mobi.lab.components.BuildConfig
import mobi.lab.components.shared.util.Log.LogImplementation
import kotlin.reflect.KClass

/**
 * A simple logging interface to avoid adding an unnecessary dependency to a library.
 */
class Log private constructor(
    private val tag: String,
    private var impl: LogImplementation
) {

    fun interface LogImplementation {
        fun writeLog(level: Level, tag: String, throwable: Throwable?, message: String?)

        enum class Level {
            DEBUG, ERROR
        }
    }

    fun setLogImplementation(impl: LogImplementation) {
        this.impl = impl
    }

    fun d(message: String?) {
        impl.writeLog(LogImplementation.Level.DEBUG, tag, null, message)
    }

    fun d(t: Throwable?, message: String?) {
        impl.writeLog(LogImplementation.Level.DEBUG, tag, t, message)
    }

    fun d(t: Throwable?) {
        impl.writeLog(LogImplementation.Level.DEBUG, tag, t, null)
    }

    fun e(message: String?) {
        impl.writeLog(LogImplementation.Level.ERROR, tag, null, message)
    }

    fun e(t: Throwable?, message: String?) {
        impl.writeLog(LogImplementation.Level.ERROR, tag, t, message)
    }

    fun e(t: Throwable?) {
        impl.writeLog(LogImplementation.Level.ERROR, tag, t, null)
    }

    companion object {
        fun newInstance(): Log {
            return newInstance("components-lib")
        }

        fun newInstance(cls: KClass<*>): Log {
            return newInstance(cls)
        }

        fun newInstance(cls: Class<*>): Log {
            return newInstance(cls.simpleName)
        }

        fun newInstance(tag: String): Log {
            return Log(tag, defaultLogImpl())
        }

        @SuppressLint("LogNotTimber")
        private fun defaultLogImpl(): LogImplementation {
            return LogImplementation { level, tag, throwable, message ->
                when (level) {
                    LogImplementation.Level.DEBUG -> {
                        if (BuildConfig.DEBUG) {
                            android.util.Log.d(tag, message, throwable)
                        }
                    }
                    LogImplementation.Level.ERROR -> android.util.Log.e(tag, message, throwable)
                }
            }
        }
    }
}

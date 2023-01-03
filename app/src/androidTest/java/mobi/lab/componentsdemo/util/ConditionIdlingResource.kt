package mobi.lab.componentsdemo.util

import androidx.test.espresso.IdlingResource
import com.thefitsphere.android.util.conditions.Condition
import timber.log.Timber
import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.thread

/**
 * An IdlingResource that periodically checks for [Condition]s added to it.
 */
class ConditionIdlingResource(
    private val checkIntervalMs: Long = DEFAULT_CHECK_INTERVAL_MS
) : IdlingResource {

    private val conditions: Queue<Condition> = ConcurrentLinkedQueue()

    private var idleResourceCallback: IdlingResource.ResourceCallback? = null
    private var isActive: Boolean = true

    init {
        startConditionCheckThread()
    }

    override fun getName(): String {
        return "Custom idling resource"
    }

    override fun isIdleNow(): Boolean {
        return conditions.isEmpty()
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        idleResourceCallback = callback
    }

    fun addCondition(condition: Condition) {
        conditions.add(condition)
    }

    fun destroy() {
        isActive = false
    }

    private fun startConditionCheckThread() {
        thread(name = THREAD_NAME, start = true) {
            while (isActive) {
                try {
                    if (checkNextCondition()) {
                        // Remove the condition and check for idle
                        conditions.remove()
                        if (conditions.isEmpty()) {
                            log("Transition to idle")
                            idleResourceCallback?.onTransitionToIdle()
                        }
                    }
                } catch (e: UnsupportedOperationException) {
                    log("Condition cannot be removed, or removal is not supported")
                }

                Thread.sleep(checkIntervalMs)
            }
        }
    }

    private fun checkNextQueuedCondition(): Boolean {
        val head = conditions.peek()
        if (head != null) {
            log("Checking ${head.getName()}")
            return head.check()
        }
        return false
    }

    /**
     * A single logging function to quickly modify logging conf when needed
     */
    private fun log(message: String) {
        Timber.d(message)
    }

    companion object {
        private const val THREAD_NAME = "ConditionIdlingResourceThread"
        private const val DEFAULT_CHECK_INTERVAL_MS: Long = 250
    }
}

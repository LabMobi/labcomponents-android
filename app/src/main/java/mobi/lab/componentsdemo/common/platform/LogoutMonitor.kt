package mobi.lab.componentsdemo.common.platform

import android.app.Application
import mobi.lab.componentsdemo.common.util.NavUtil
import mobi.lab.componentsdemo.domain.usecases.auth.DeleteSessionUseCase
import timber.log.Timber

/**
 * Helps to handle automatic logouts. The automatic logout is locked until we are sure that we have logged back in.
 * logout() is called whenever a network request fails because of an unauthorized session.
 * Needs to be a singleton (object) to enforce global syncing of logout requests
 *
 * reset() is called in 2 places:
 * MainActivity.onCreate():
 *     - Main application screen => We have a proper session and are ready to handle any errors
 * PickCompanyFragment.onCreate():
 *     - During login we might see this screen before MainActivity => Let's be ready to handle errors
 */
object LogoutMonitor {

    @Volatile private lateinit var application: Application
    @Volatile private var logoutHandled = false
    @Volatile private lateinit var deleteSessionUseCase: DeleteSessionUseCase

    fun init(application: Application, deleteSessionUseCase: DeleteSessionUseCase) {
        this.application = application
        this.deleteSessionUseCase = deleteSessionUseCase
    }

    /**
     * Call this once the application knows that the user is in a logged in state
     * and can start interact with the application. The main starting screen for example.
     */
    fun reset() {
        synchronized(this) {
            logoutHandled = false
        }
    }

    /**
     * Call this once the application knows that the session is not valid
     */
    fun logout() {
        synchronized(this) {
            if (logoutHandled) {
                Timber.e("logout called, already called, ignore")
                return
            }

            logoutHandled = true
            deleteSessionUseCase.execute()
            NavUtil.restartApplication(application)
        }
    }
}

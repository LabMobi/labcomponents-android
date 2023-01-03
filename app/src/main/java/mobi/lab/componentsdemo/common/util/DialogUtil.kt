package mobi.lab.componentsdemo.common.util

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import mobi.lab.componentsdemo.app.common.isStringEmpty
import timber.log.Timber

/**
 * Some methods to make DialogFragments safer to use.
 * Note: This v2 impl uses an EventBus to send dialog button/dismiss/cancel events to the UI.
 * This means that every dialog shown should have a UNIQUE tag. Otherwise events will be handled
 * in several places.
 */

object DialogUtil {

    private const val RETRY_COUNT = 3
    private const val RETRY_DELAY = 800L

    fun show(fragment: Fragment, dialog: DialogFragment, tag: String) {
        show(fragment.activity, dialog, tag, RETRY_COUNT)
    }

    fun show(activity: FragmentActivity?, dialog: DialogFragment?, tag: String, retryCount: Int = RETRY_COUNT) {
        if (activity == null || dialog == null || isStringEmpty(tag)) {
            Timber.e("showDialogFragment: activity == null || dialog == null || isStringEmpty(tag)")
            // Fail
            return
        }

        if (activity.isFinishing) {
            // No need
            Timber.w("showDialogFragment: activity.isFinishing()")
            return
        }

        try {
            val ft = activity.supportFragmentManager.beginTransaction()
            removePreviousFragmentIfAny(activity, ft, tag)
            // Create and show the dialog.
            dialog.show(ft, tag)
        } catch (e: IllegalStateException) {
            Timber.e(e, "showDialogFragment")
            if (retryCount > 0) {
                Timber.d("showDialogFragment: will retry")
                retryAction { show(activity, dialog, tag, retryCount - 1) }
            }
        }
    }

    fun dismiss(fragment: Fragment, tag: String) {
        dismiss(fragment.activity, tag)
    }

    fun dismiss(activity: FragmentActivity?, tag: String, retryCount: Int = RETRY_COUNT) {
        if (activity == null || isStringEmpty(tag)) {
            Timber.e("dismissDialogFragment: activity == null || isStringEmpty(tag)")
            return
        }
        try {
            val fragment = activity.supportFragmentManager.findFragmentByTag(tag) as DialogFragment?
            if (fragment != null) {
                fragment.dismissAllowingStateLoss()
                Timber.d("dismissDialogFragment: dismissed")
            } else if (retryCount > 0) {
                Timber.d("dismissDialogFragment: will retry")
                retryAction { dismiss(activity, tag, retryCount - 1) }
            }
        } catch (e: IllegalStateException) {
            Timber.e(e, "dismissDialogFragment")
            if (retryCount > 0) {
                retryAction { dismiss(activity, tag, retryCount - 1) }
            }
        }
    }

    fun isShowing(activity: FragmentActivity?, tag: String): Boolean {
        return if (activity == null) {
            false
        } else {
            activity.supportFragmentManager.findFragmentByTag(tag) != null
        }
    }

    private fun retryAction(action: () -> Unit) {
        val uiHandler = Handler(Looper.getMainLooper())
        uiHandler.postDelayed(action, RETRY_DELAY)
    }

    private fun removePreviousFragmentIfAny(activity: FragmentActivity?, ft: FragmentTransaction?, tag: String) {
        if (activity == null || ft == null || TextUtils.isEmpty(tag)) {
            return
        }
        val previousFragment = activity.supportFragmentManager.findFragmentByTag(tag)
        if (previousFragment != null) {
            // Remove the previous one?
            ft.remove(previousFragment)
        }
    }
}

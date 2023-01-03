package mobi.lab.componentsdemo.common.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.ShareCompat
import mobi.lab.componentsdemo.splash.SplashActivity
import timber.log.Timber

object NavUtil {

    fun openBrowser(context: Context, url: String) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (error: ActivityNotFoundException) {
            Timber.e(error, "openWebUrl")
        }
    }

    fun sendEmail(activity: Activity, title: String, to: String) {
        ShareCompat.IntentBuilder(activity)
            .setType("text/plain")
            .addEmailTo(to)
            .setChooserTitle(title)
            .startChooser()
    }

    fun restartApplication(context: Context) {
        val intent = Intent(context, SplashActivity::class.java)
        val restartIntent = Intent.makeRestartActivityTask(intent.component)
        context.startActivity(restartIntent)
    }
}

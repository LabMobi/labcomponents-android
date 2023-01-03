package mobi.lab.componentsdemo.common.platform

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import mobi.lab.componentsdemo.R

class MyNotificationManager private constructor(private val context: Context) {

    private val notificationManager: android.app.NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
        as android.app.NotificationManager

    fun createChannels() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            // Ignore, this is not available on older versions
            return
        }
        createUploadChannel()
    }

    @SuppressLint("NewApi")
    private fun createUploadChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            android.app.NotificationManager.IMPORTANCE_LOW
        )

        channel.enableLights(false)
        channel.enableVibration(false)
        channel.setSound(null, null)
        notificationManager.createNotificationChannel(channel)
    }

    @Suppress("UnusedPrivateMember") // A convenience function to create notifications. Not used in the template
    private fun createNotification(message: String, priority: Int = NotificationCompat.PRIORITY_DEFAULT): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, R.color.primary))
            .setAutoCancel(true)
            .setPriority(priority)
            .build()
    }

    companion object {
        private const val CHANNEL_ID = "uploads"

        fun newInstance(context: Context): MyNotificationManager {
            return MyNotificationManager(context)
        }
    }
}

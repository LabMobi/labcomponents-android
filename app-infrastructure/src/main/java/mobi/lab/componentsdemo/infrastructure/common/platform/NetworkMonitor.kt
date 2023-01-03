package mobi.lab.componentsdemo.infrastructure.common.platform

import android.content.Context
import mobi.lab.componentsdemo.app.common.isNetworkConnected

class NetworkMonitor(private val context: Context) {
    fun isConnected(): Boolean {
        return isNetworkConnected(context)
    }
}

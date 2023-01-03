package mobi.lab.componentsdemo.app.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * Check if we have a network connection
 */
@SuppressWarnings("MissingPermission")
fun isNetworkConnected(context: Context): Boolean {
    // We assume that for modern devices null ConnectivityManager means no network capability
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager? ?: return false
    // Network object for the current default network or
    // null if no default network is currently active or if the default network is blocked for the caller
    // We assume the app can't use the connection then.
    val network = connectivityManager.activeNetwork ?: return false
    // This method returns null if the network is unknown or if the |network| argument is null
    // We do not assume there is no connection if this object is null,
    // we just count it as no evidence wither way.
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return true
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

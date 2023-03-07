package mobi.lab.components.shared

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

internal object ParcelCompat {

    inline fun <reified T : Parcelable> getParcelable(bundle: Bundle, key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }
}

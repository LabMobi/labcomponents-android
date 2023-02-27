package mobi.lab.components.ui.shared

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

object ParcelCompat {

    inline fun <reified T : Parcelable> getParcelable(bundle: Bundle, key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }
}

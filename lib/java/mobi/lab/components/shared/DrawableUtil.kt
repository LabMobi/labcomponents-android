package mobi.lab.components.shared

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.DrawableCompat

internal object DrawableUtil {

    fun setTintList(drawable: Drawable?, tint: ColorStateList) {
        if (drawable != null) {
            DrawableCompat.setTintList(drawable, tint)
        }
    }
}

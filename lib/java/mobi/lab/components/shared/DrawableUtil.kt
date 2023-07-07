package mobi.lab.components.shared

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat

internal object DrawableUtil {

    fun setDrawableColor(drawable: Drawable?, @ColorInt color: Int) {
        if (drawable != null) {
            DrawableCompat.setTintList(drawable, ColorStateList.valueOf(color))
        }
    }
}

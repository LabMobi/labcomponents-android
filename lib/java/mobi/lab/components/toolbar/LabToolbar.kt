package mobi.lab.components.toolbar

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.google.android.material.appbar.MaterialToolbar

/**
 * A wrapper around [MaterialToolbar].
 */
public class LabToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialToolbar(context, attrs, defStyleAttr) {

    public fun enableNavigationIcon(
        @DrawableRes iconResId: Int? = null,
        @ColorInt iconTint: Int? = null,
        listener: () -> Unit
    ) {
        if (iconResId != null) {
            setNavigationIcon(iconResId)
        }
        if (iconTint != null) {
            setNavigationIconTint(iconTint)
        }
        setNavigationOnClickListener { listener.invoke() }
    }
}

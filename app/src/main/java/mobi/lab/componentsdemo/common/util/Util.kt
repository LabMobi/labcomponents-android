package mobi.lab.componentsdemo.common.util

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import mobi.lab.componentsdemo.BuildConfig
import kotlin.math.roundToInt

fun isDebugBuild(): Boolean {
    return BuildConfig.DEBUG
}

fun showToast(ctx: Context?, message: CharSequence) {
    ctx?.let {
        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show()
    }
}

fun showToast(ctx: Context?, @StringRes resId: Int) {
    ctx?.let {
        Toast.makeText(ctx, resId, Toast.LENGTH_LONG).show()
    }
}

fun setViewVisibility(view: View?, visibility: Int) {
    if (view == null) {
        return
    }
    view.visibility = visibility
}

fun setViewEnabled(view: View, enabled: Boolean) {
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            setViewEnabled(view.getChildAt(i), enabled)
        }
    }
    view.isEnabled = enabled
}

fun closeKeyboard(view: View) {
    val manager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    manager.hideSoftInputFromWindow(view.windowToken, 0)
}

@Suppress("MagicNumber")
@ColorInt
fun setColorAlpha(alpha: Float, @ColorInt color: Int): Int {
    return Color.argb(
        (255 * alpha).roundToInt(),
        Color.red(color),
        Color.green(color),
        Color.blue(color)
    )
}

fun dpToPx(context: Context?, dp: Int): Float {
    if (context == null) {
        return dp.toFloat()
    }
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)
}

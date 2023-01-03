package mobi.lab.componentsdemo.common.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import mobi.lab.componentsdemo.BuildConfig
import mobi.lab.componentsdemo.R
import mobi.lab.componentsdemo.domain.entities.ErrorCode
import kotlin.math.roundToInt

fun hasExternalStorageWritePermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
}

fun isExternalStorageMounted(): Boolean {
    return android.os.Environment.MEDIA_MOUNTED == android.os.Environment.getExternalStorageState()
}

fun isDebugBuild(): Boolean {
    return BuildConfig.DEBUG
}

fun isCrashlyticsEnabled(): Boolean {
    return !isDebugBuild()
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

fun formatErrorCode(context: Context?, error: ErrorCode?, @StringRes default: Int = R.string.error_generic): String {
    if (context == null) {
        return ""
    }
    val resId = when (error) {
        ErrorCode.LOCAL_NO_NETWORK -> R.string.error_no_network
        else -> default
    }
    return context.getString(resId)
}

fun dpToPx(context: Context?, dp: Int): Float {
    if (context == null) {
        return dp.toFloat()
    }
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)
}

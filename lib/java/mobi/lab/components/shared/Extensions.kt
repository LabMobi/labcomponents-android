package mobi.lab.components.shared

import android.content.Context
import android.util.TypedValue
import androidx.annotation.Dimension
import kotlin.math.roundToInt

fun dpToPx(context: Context?, @Dimension(unit = Dimension.DP) dp: Int): Int {
    if (context == null) {
        return dp
    }
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).roundToInt()
}

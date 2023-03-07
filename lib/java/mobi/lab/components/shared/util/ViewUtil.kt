package mobi.lab.components.shared.util

import android.graphics.Point
import android.view.View

internal object ViewUtil {

    fun getLocationOnScreen(view: View): Point {
        val input = IntArray(2)
        view.getLocationOnScreen(input)
        return Point(input[0], input[1])
    }
}

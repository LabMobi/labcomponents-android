package mobi.lab.components.button

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton

open class LabButton : MaterialButton {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}

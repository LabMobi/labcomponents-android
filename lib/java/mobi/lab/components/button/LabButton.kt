package mobi.lab.components.button

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton

public open class LabButton : MaterialButton {
    public constructor(context: Context) : super(context)
    public constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    public constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}

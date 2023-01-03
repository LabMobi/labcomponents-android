package mobi.lab.components.demo.common.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.theme.MaterialComponentsViewInflater

/**
 * We don't want to replace all Button usages with MaterialButton as the default styles differ too much.
 * See styles.xml AppTheme viewInflater override.
 *
 * Also, we need to keep this class intact during R8 optimizations.
 */
class CustomMaterialComponentViewInflater : MaterialComponentsViewInflater() {
    override fun createButton(context: Context, attrs: AttributeSet): AppCompatButton {
        return AppCompatButton(context, attrs)
    }
}

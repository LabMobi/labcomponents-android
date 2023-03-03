package mobi.lab.components.demo

import android.os.Bundle
import androidx.appcompat.widget.Toolbar

class TypographyActivity : BaseComponentActivity() {

    override fun getToolbar(): Toolbar {
        return findViewById(R.id.toolbar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_typography)
    }
}

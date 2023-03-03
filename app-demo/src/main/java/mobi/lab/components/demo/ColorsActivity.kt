package mobi.lab.components.demo

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import mobi.lab.components.demo.databinding.ActivityColorsBinding

class ColorsActivity : BaseComponentActivity() {

    private lateinit var binding: ActivityColorsBinding

    override fun getToolbar(): Toolbar {
        return binding.toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityColorsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }
}

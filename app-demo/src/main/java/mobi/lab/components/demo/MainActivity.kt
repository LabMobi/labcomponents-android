package mobi.lab.components.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import mobi.lab.components.demo.databinding.ActivityMainBinding
import kotlin.reflect.KClass

class MainActivity : BaseComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun getToolbar(): Toolbar {
        return binding.toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.apply {
            itemTextField.setOnClickListener { startActivityInternal(TextFieldActivity::class) }
            itemTypography.setOnClickListener { startActivityInternal(TypographyActivity::class) }
            itemColors.setOnClickListener { startActivityInternal(ColorsActivity::class) }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun startActivityInternal(activityClass: KClass<out Activity>) {
        startActivity(Intent(this, activityClass.java))
    }
}

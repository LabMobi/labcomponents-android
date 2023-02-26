package mobi.lab.components.demo

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDelegate
import mobi.lab.components.demo.common.BaseActivity
import mobi.lab.components.demo.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        initUi(binding)
    }

    private fun initUi(binding: ActivityMainBinding) {
        binding.textField.setHint("This is a hint")

        binding.buttonSwitchMode.setOnClickListener {
            val nightMode = if (isNightModeEnabled()) {
                AppCompatDelegate.MODE_NIGHT_NO
            } else {
                AppCompatDelegate.MODE_NIGHT_YES
            }
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }

    private fun isNightModeEnabled(): Boolean {
        val uiMode: Int = resources?.configuration?.uiMode ?: return false
        return uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}

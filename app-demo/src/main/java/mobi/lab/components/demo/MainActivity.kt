package mobi.lab.components.demo

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import mobi.lab.components.demo.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initUi(binding)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, MENU_ITEM_SWITCH_UI_MODE, 0, "Switch UI mode")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (MENU_ITEM_SWITCH_UI_MODE == item.itemId) {
            toggleUiMode()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initUi(binding: ActivityMainBinding) {
        binding.apply {
            setSupportActionBar(toolbar)
            textField.hint = "This is a hint"
            textField2.hint = "This is a hint"
            checkTextFieldEnabled.setOnCheckedChangeListener { _, isChecked ->
                textField.isEnabled = isChecked
                textField2.isEnabled = isChecked
            }
            checkTextFieldError.setOnCheckedChangeListener { _, isChecked ->
                var errorText = ""
                if (isChecked) {
                    errorText = textField2.getText()
                    if (errorText.isEmpty()) {
                        errorText = "This is an error!"
                    }
                }
                textField.error = errorText
                textField2.error = errorText
            }
        }
    }

    private fun toggleUiMode() {
        val nightMode = if (isNightModeEnabled()) {
            AppCompatDelegate.MODE_NIGHT_NO
        } else {
            AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    private fun isNightModeEnabled(): Boolean {
        val uiMode: Int = resources?.configuration?.uiMode ?: return false
        return uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }

    companion object {
        private const val MENU_ITEM_SWITCH_UI_MODE = 0

        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}

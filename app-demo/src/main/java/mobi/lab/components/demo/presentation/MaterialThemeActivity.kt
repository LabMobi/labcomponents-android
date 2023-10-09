package mobi.lab.components.demo.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import mobi.lab.components.button.LabButton
import mobi.lab.components.demo.databinding.ActivityMaterialThemeBinding

/**
 * Test activity to test LabComponents themes.
 * We don't have support for opening this Activity from the UI. It's just a convenience for development.
 */
class MaterialThemeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMaterialThemeBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.switchEnabled.setOnCheckedChangeListener { _, isChecked ->
            binding.loopButtons { isEnabled = isChecked }
        }
    }

    private fun ActivityMaterialThemeBinding.loopButtons(action: LabButton.() -> Unit) {
        listOf(
            filled, filledIconStart, filledIconEnd,
            filledBig, filledBigIconStart, filledBigIconEnd,
            tonal, tonalIconStart, tonalIconEnd,
            tonalBig, tonalBigIconStart, tonalBigIconEnd,
            outlined, outlinedIconStart, outlinedIconEnd,
            outlinedBig, outlinedBigIconStart, outlinedBigIconEnd,
            text, textIconStart, textIconEnd,
            textBig, textBigIconStart, textBigIconEnd,
        ).map(action)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, MENU_ITEM_SWITCH_NIGHT_MODE, 0, "Switch UI mode")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            MENU_ITEM_SWITCH_NIGHT_MODE -> {
                toggleNightMode()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun toggleNightMode() {
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
        private const val MENU_ITEM_SWITCH_NIGHT_MODE = 0
    }
}

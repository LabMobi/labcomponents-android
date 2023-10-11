package mobi.lab.components.demo.presentation

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import mobi.lab.components.demo.R
import mobi.lab.components.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, MENU_ITEM_SWITCH_NIGHT_MODE, 0, "Switch UI mode")
        // Restore this for development purposes. We don't want to display always display this option to everyone
//        menu.add(0, MENU_ITEM_SWITCH_MATERIAL_THEME, 1, "Open Material Activity")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            MENU_ITEM_SWITCH_NIGHT_MODE -> {
                toggleNightMode()
                true
            }
            MENU_ITEM_SWITCH_MATERIAL_THEME -> {
                startActivity(Intent(this, MaterialThemeActivity::class.java))
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController()
        return navController.navigateUp() || super.onSupportNavigateUp()
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

    private fun findNavController(): NavController {
        return findNavController(R.id.nav_host_fragment)
    }

    companion object {
        private const val MENU_ITEM_SWITCH_NIGHT_MODE = 0
        private const val MENU_ITEM_SWITCH_MATERIAL_THEME = 1
    }
}

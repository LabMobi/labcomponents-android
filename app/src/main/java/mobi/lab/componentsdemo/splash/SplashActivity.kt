package mobi.lab.componentsdemo.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import mobi.lab.componentsdemo.common.BaseActivity
import mobi.lab.componentsdemo.common.ViewModelFactory
import mobi.lab.componentsdemo.di.Injector
import mobi.lab.componentsdemo.login.LoginActivity
import mobi.lab.componentsdemo.main.MainActivity
import javax.inject.Inject

// We want the custom SplashActivity here for routing purposes
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    @Inject lateinit var factory: ViewModelFactory

    private val viewModel: SplashViewModel by viewModels { factory }

    init {
        Injector.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initSplash()
        super.onCreate(savedInstanceState)
        /**
         * Android launcher bug workaround. Previous task is not resumed, but a new one is opened.
         * See:
         * https://issuetracker.google.com/issues/64108432 A new bug report
         * https://issuetracker.google.com/issues/36907463 Old, closed bug report
         */
        if (!isTaskRoot) {
            val intent = intent
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && TextUtils.equals(Intent.ACTION_MAIN, intent.action)) {
                finish()
                return
            }
        }
        initViewModel()
    }

    private fun initSplash() {
        val splash = installSplashScreen()
        splash.setKeepOnScreenCondition { true }
    }

    private fun initViewModel() {
        viewModel.action.onEachEvent { action ->
            val intent = when (action) {
                SplashViewModel.Action.LaunchApplication -> MainActivity.getIntent(this)
                SplashViewModel.Action.LaunchLogin -> LoginActivity.getIntent(this)
            }
            startActivity(intent)
            finish()
        }
    }
}

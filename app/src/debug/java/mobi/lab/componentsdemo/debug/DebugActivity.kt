package mobi.lab.componentsdemo.debug

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import mobi.lab.componentsdemo.common.BaseActivity
import mobi.lab.componentsdemo.common.platform.LogoutMonitor
import mobi.lab.componentsdemo.databinding.ActivityDebugBinding
import mobi.lab.scrolls.LogImplFile
import mobi.lab.scrolls.LogViewBuilder

class DebugActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDebugBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.initUi()
    }

    private fun ActivityDebugBinding.initUi() {
        buttonLogout.setOnClickListener { LogoutMonitor.logout() }
        buttonCrash.setOnClickListener { throw RuntimeException() }
        buttonScrolls.setOnClickListener {
            val builder = LogViewBuilder()
            builder.setDirectory(LogImplFile.getLogDir())
                .addTags("manual_post")
                .launchActivity(this@DebugActivity)
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, DebugActivity::class.java)
        }
    }
}

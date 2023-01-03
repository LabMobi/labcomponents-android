package mobi.lab.components.demo.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import mobi.lab.components.demo.R
import mobi.lab.components.demo.common.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}

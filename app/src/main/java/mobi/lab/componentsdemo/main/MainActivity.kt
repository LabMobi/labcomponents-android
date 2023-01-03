package mobi.lab.componentsdemo.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import mobi.lab.componentsdemo.common.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}

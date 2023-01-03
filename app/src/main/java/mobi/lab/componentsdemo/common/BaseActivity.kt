package mobi.lab.componentsdemo.common

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

open class BaseActivity : AppCompatActivity {

    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("onCreate this=$this")
    }

    override fun onStart() {
        super.onStart()
        Timber.v("onStart this=$this")
    }

    override fun onResume() {
        super.onResume()
        Timber.v("onResume this=$this")
    }

    override fun onPause() {
        super.onPause()
        Timber.v("onPause this=$this")
    }

    override fun onStop() {
        super.onStop()
        Timber.v("onStop this=$this")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.v("onDestroy this=$this")
    }
}

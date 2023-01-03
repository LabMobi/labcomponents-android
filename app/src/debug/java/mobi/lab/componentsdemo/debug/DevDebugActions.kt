package mobi.lab.componentsdemo.debug

import android.content.Context
import mobi.lab.componentsdemo.common.debug.DebugActions

class DevDebugActions : DebugActions {
    override fun launchDebugActivity(context: Context) {
        return context.startActivity(DebugActivity.getIntent(context))
    }
}

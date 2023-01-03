package mobi.lab.componentsdemo.util.conditions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.thefitsphere.android.util.conditions.Condition

class FragmentNotResumedCondition(
    private val fragmentManager: FragmentManager,
    private val fragmentTag: String
) : Condition {

    constructor(activity: FragmentActivity, fragmentTag: String) : this(activity.supportFragmentManager, fragmentTag)
    constructor(fragment: Fragment, fragmentTag: String) : this(fragment.parentFragmentManager, fragmentTag)

    override fun check(): Boolean {
        val currentFragment = fragmentManager.findFragmentByTag(fragmentTag)
        return !(currentFragment?.isResumed ?: false)
    }

    override fun getName(): String {
        return "Fragment $fragmentTag not resumed condition"
    }

    override fun toString(): String {
        return "FragmentNotResumedCondition(fragmentTag='$fragmentTag')"
    }
}

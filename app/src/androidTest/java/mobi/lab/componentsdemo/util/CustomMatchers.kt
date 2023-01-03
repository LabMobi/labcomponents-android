package mobi.lab.componentsdemo.util

import android.view.View
import androidx.annotation.StringRes
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.material.textfield.TextInputLayout
import mobi.lab.componentsdemo.app.common.isStringEmpty
import mobi.lab.componentsdemo.app.common.stringEquals
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun hasNoTextInputLayoutError(): Matcher<View> {
    return object : TypeSafeMatcher<View>() {

        override fun describeTo(description: Description?) {
            description?.appendText("Expected no error text")
        }

        override fun matchesSafely(view: View?): Boolean {
            if (view !is TextInputLayout) {
                return false
            }

            return isStringEmpty(view.error)
        }
    }
}

fun hasTextInputLayoutError(expectedErrorText: String): Matcher<View> {
    return object : TypeSafeMatcher<View>() {

        override fun describeTo(description: Description?) {
            description?.appendText("Expected error $expectedErrorText not found")
        }

        override fun matchesSafely(view: View?): Boolean {
            if (view !is TextInputLayout) {
                return false
            }

            return stringEquals(expectedErrorText, view.error)
        }
    }
}

fun hasTextInputLayoutError(@StringRes id: Int): Matcher<View> {
    return hasTextInputLayoutError(InstrumentationRegistry.getInstrumentation().targetContext.getString(id))
}

package mobi.lab.componentsdemo.login

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import mobi.lab.componentsdemo.R
import mobi.lab.componentsdemo.main.MainActivity
import mobi.lab.componentsdemo.util.BaseInstrumentationTest
import mobi.lab.componentsdemo.util.ConditionIdlingResource
import mobi.lab.componentsdemo.util.conditions.FragmentNotResumedCondition
import mobi.lab.componentsdemo.util.conditions.FragmentResumedCondition
import mobi.lab.componentsdemo.util.hasNoTextInputLayoutError
import mobi.lab.componentsdemo.util.hasTextInputLayoutError
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeTest
class LoginActivityTest : BaseInstrumentationTest() {

    /**
     * Use [androidx.test.ext.junit.rules.ActivityScenarioRule] to create and launch the activity under test before each test,
     * and close it after each test. This is a replacement for
     * [androidx.test.rule.ActivityTestRule].
     */
    @get:Rule val activityScenarioRule = activityScenarioRule<LoginActivity>()

    private lateinit var activity: LoginActivity

    @Before
    fun setUp() {
        Intents.init()
        activityScenarioRule.scenario.onActivity {
            activity = it
        }
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun show_input_error_when_fields_are_empty() = withConditionIdlingResource { idlingResource ->
        onView(withId(R.id.button_login)).perform(click())

        idlingResource.waitForProgressDialog()

        onView(withId(R.id.input_layout_email)).check(matches(hasTextInputLayoutError(TEXT_ID_REQUIRED)))
        onView(withId(R.id.input_layout_password)).check(matches(hasTextInputLayoutError(TEXT_ID_REQUIRED)))

        Intents.assertNoUnverifiedIntents()
    }

    @Test
    fun show_input_error_when_only_username_is_filled() = withConditionIdlingResource { idlingResource ->
        onView(withId(R.id.edit_text_email)).perform(typeText("asd"), closeSoftKeyboard())
        onView(withId(R.id.button_login)).perform(click())

        idlingResource.waitForProgressDialog()

        onView(withId(R.id.input_layout_email)).check(matches(hasNoTextInputLayoutError()))
        onView(withId(R.id.input_layout_password)).check(matches(hasTextInputLayoutError(TEXT_ID_REQUIRED)))

        Intents.assertNoUnverifiedIntents()
    }

    @Test
    fun show_input_error_when_only_password_is_filled() = withConditionIdlingResource { idlingResource ->
        onView(withId(R.id.edit_text_password)).perform(typeText("asd"), closeSoftKeyboard())
        onView(withId(R.id.button_login)).perform(click())

        idlingResource.waitForProgressDialog()

        onView(withId(R.id.input_layout_email)).check(matches(hasTextInputLayoutError(TEXT_ID_REQUIRED)))
        onView(withId(R.id.input_layout_password)).check(matches(hasNoTextInputLayoutError()))

        Intents.assertNoUnverifiedIntents()
    }

    @Test
    fun login_success_when_fields_are_filled() = withConditionIdlingResource { idlingResource ->
        onView(withId(R.id.edit_text_email)).perform(typeText("asd"), closeSoftKeyboard())
        onView(withId(R.id.edit_text_password)).perform(typeText("asd"), closeSoftKeyboard())
        onView(withId(R.id.button_login)).perform(click())

        idlingResource.waitForProgressDialog()

        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
    }

    @Test
    fun show_error_dialog_when_login_fails() = withConditionIdlingResource { idlingResource ->
        // "test" is a keyword to trigger an error response. See LoginUseCase implementation
        onView(withId(R.id.edit_text_email)).perform(typeText("test"), closeSoftKeyboard())
        onView(withId(R.id.edit_text_password)).perform(typeText("asd"), closeSoftKeyboard())
        onView(withId(R.id.button_login)).perform(click())

        idlingResource.waitForProgressDialog()
        idlingResource.addCondition(FragmentResumedCondition(activity, LoginFragment.TAG_DIALOG_ERROR))

        // Validate the dialog and close it
        onView(withText(R.string.error_generic)).check(matches(isDisplayed()))
        Espresso.pressBack()

        onView(withId(R.id.input_layout_email)).check(matches(hasNoTextInputLayoutError()))
        onView(withId(R.id.input_layout_password)).check(matches(hasNoTextInputLayoutError()))

        Intents.assertNoUnverifiedIntents()
    }

    private fun ConditionIdlingResource.waitForProgressDialog() {
        // Check that the progress dialog is not active anymore
        addCondition(FragmentNotResumedCondition(activity, LoginFragment.TAG_DIALOG_PROGRESS))
    }

    companion object {
        private const val TEXT_ID_REQUIRED = R.string.text_required
    }
}

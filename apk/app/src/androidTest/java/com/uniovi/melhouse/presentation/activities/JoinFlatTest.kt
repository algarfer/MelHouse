package com.uniovi.melhouse.presentation.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.uniovi.melhouse.R
import com.uniovi.melhouse.preference.Prefs
import com.uniovi.melhouse.preferences.TestPrefs
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class JoinFlatTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Rule(order = 1)
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SplashScreenActivity::class.java)

    @Before
    fun init() {
        // Perform any setup here
        hiltRule.inject() // Inject Hilt dependencies
    }

    @BindValue
    @JvmField
    val prefs: Prefs = TestPrefs()

    @Test
    fun joinFlatTest() {
        signIn()

        val textInputEditText6 = onView(
            allOf(
                withId(R.id.etFlatCode),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.flatCodeLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText6.perform(replaceText("JaviMont"), closeSoftKeyboard())

        val materialButton4 = onView(
            allOf(
                withId(R.id.btnJoinFlat), withText("Unirse"),
                childAtPosition(
                    allOf(
                        withId(R.id.formLayout),
                        childAtPosition(
                            withId(R.id.cvJoinFlat),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())

        checkJoinedFlat()

    }
}
package com.uniovi.melhouse.presentation.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
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
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class UserTest {

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
    fun registerUserTest() {
        val materialButton = onView(
            allOf(
                withId(R.id.signUpButton), withText("Registrarse"),
                childAtPosition(
                    allOf(
                        withId(R.id.main),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val textInputEditText = onView(
            allOf(
                withId(R.id.etName),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nameLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("melquiades"), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.etEmail),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.emailLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(replaceText("mel@mel.mel"), closeSoftKeyboard())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.etPassword),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.passwordLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText3.perform(replaceText("Melmel666"), closeSoftKeyboard())

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.etConfirmPassword),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.confirmPasswordLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText4.perform(replaceText("Melmel666"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(
                withId(R.id.btnSignup), withText("Continuar"),
                childAtPosition(
                    allOf(
                        withId(R.id.formLayout),
                        childAtPosition(
                            withClassName(`is`("com.google.android.material.card.MaterialCardView")),
                            0
                        )
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.tvNoFlat), withText("Entrar a un piso"),
                withParent(withParent(withId(R.id.menuOptionsFragment))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Entrar a un piso")))
    }



    @Test
    fun singInTest() {
        singIn()
    }


}

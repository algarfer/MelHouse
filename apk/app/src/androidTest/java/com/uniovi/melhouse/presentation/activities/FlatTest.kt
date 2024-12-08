package com.uniovi.melhouse.presentation.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
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
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class FlatTest {

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
    fun createFlatTest() {
        singIn()

        val materialButton5 = onView(
            allOf(
                withId(R.id.btnCreateFlat), withText("Crear"),
                childAtPosition(
                    allOf(
                        withId(R.id.cvCreateFlat),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            5
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton5.perform(click())

        val textInputEditText12 = onView(
            allOf(
                withId(R.id.etFlatName),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.flatNameLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText12.perform(replaceText("Piso de mel"), closeSoftKeyboard())

        val textInputEditText13 = onView(
            allOf(
                withId(R.id.etAddress),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.flatAddressLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText13.perform(click())

        val textInputEditText14 = onView(
            allOf(
                withId(R.id.etAddress),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.flatAddressLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText14.perform(replaceText("Valdes Salas"), closeSoftKeyboard())

        val textInputEditText15 = onView(
            allOf(
                withId(R.id.etFloor),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.flatFloorLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText15.perform(replaceText("1"), closeSoftKeyboard())

        val textInputEditText16 = onView(
            allOf(
                withId(R.id.etDoor),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.flatDoorLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText16.perform(replaceText("A"), closeSoftKeyboard())

        val textInputEditText17 = onView(
            allOf(
                withId(R.id.etFlatStair),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.flatStairLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText17.perform(replaceText("A"), closeSoftKeyboard())

        val textInputEditText18 = onView(
            allOf(
                withId(R.id.etFlatBedrooms),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.flatBedroomsLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText18.perform(replaceText("3"), closeSoftKeyboard())

        val materialButton6 = onView(
            allOf(
                withId(R.id.btnContinuar), withText("Continuar"),
                childAtPosition(
                    allOf(
                        withId(R.id.formLayout),
                        childAtPosition(
                            withId(R.id.cvCreateFlat),
                            0
                        )
                    ),
                    8
                ),
                isDisplayed()
            )
        )
        materialButton6.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.tvTodayTasks), withText("Tareas de hoy"),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Tareas de hoy")))
    }

    @Test
    fun joinFlatTest(){
        singIn()

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

        //TODO Check that the user has joined the flat
    }
}

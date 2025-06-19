package com.uniovi.melhouse.presentation.activities

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.uniovi.melhouse.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anyOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher

fun signIn() {
    val materialButton = onView(
        allOf(
            withId(R.id.loginButton), withText("Iniciar Sesión"),
            childAtPosition(
                allOf(
                    withId(R.id.main),
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    )
                ),
                1
            ),
            isDisplayed()
        )
    )
    materialButton.perform(click())

    val textInputEditText = onView(
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
    textInputEditText.perform(replaceText("mel@mel.mel"), closeSoftKeyboard())

    val textInputEditText2 = onView(
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
    textInputEditText2.perform(replaceText("Melmel666"), closeSoftKeyboard())

    val materialButton2 = onView(
        allOf(
            withId(R.id.btnLogin), withText("Continuar"),
            childAtPosition(
                allOf(
                    withId(R.id.formLayout),
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.card.MaterialCardView")),
                        0
                    )
                ),
                4
            ),
            isDisplayed()
        )
    )
    materialButton2.perform(click())

    try {
        val materialButton3 = onView(
            allOf(
                anyOf(
                    withText("Permitir"),
                    withText("Allow")
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())
    } catch (_: Exception) {
    }

    val textView = onView(
        allOf(
            withId(R.id.tvNoFlat), withText("Entrar a un piso"),
            withParent(withParent(withId(R.id.menuOptionsFragment))),
            isDisplayed()
        )
    )
    textView.check(matches(withText("Entrar a un piso")))
}

fun createFlat() {
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

    checkJoinedFlat()
}

fun checkJoinedFlat() {
    val textView = onView(
        allOf(
            withId(R.id.tvTodayTasks), withText("Tareas pendientes para hoy"),
            isDisplayed()
        )
    )
    textView.check(matches(withText("Tareas pendientes para hoy")))
}

fun signOut() {
    clickMenuOption(R.id.navigation_logout, 7)

    val button = onView(
        allOf(
            withId(R.id.loginButton), withText("Iniciar Sesión"),
            withParent(
                allOf(
                    withId(R.id.main),
                    withParent(withId(android.R.id.content))
                )
            ),
            isDisplayed()
        )
    )
    button.check(matches(isDisplayed()))
}

fun childAtPosition(
    parentMatcher: Matcher<View>, position: Int
): Matcher<View> {

    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("Child at position $position in parent ")
            parentMatcher.describeTo(description)
        }

        public override fun matchesSafely(view: View): Boolean {
            val parent = view.parent
            return parent is ViewGroup && parentMatcher.matches(parent)
                    && view == parent.getChildAt(position)
        }
    }
}

fun createBill() {
    clickMenuOption(R.id.navigation_bills, 5)

    /*runBlocking {
        delay(60000) // Wait for the RecyclerView to load
    }*/

    val fab = onView(
        allOf(
            withId(R.id.add_bill_fab),
            isDisplayed()
        )
    )
    fab.check(matches(isDisplayed())).perform(click())

    val appCompatEditText = onView(
        allOf(
            withId(R.id.editTextText),
            childAtPosition(
                childAtPosition(
                    withClassName(`is`("android.widget.ScrollView")),
                    0
                ),
                3
            )
        )
    )
    appCompatEditText.perform(scrollTo(), replaceText("Concepto"), closeSoftKeyboard())

    val appCompatEditText2 = onView(
        allOf(
            withId(R.id.editTextNumberDecimal),
            childAtPosition(
                childAtPosition(
                    withClassName(`is`("android.widget.ScrollView")),
                    0
                ),
                4
            )
        )
    )
    appCompatEditText2.perform(scrollTo(), replaceText("10"), closeSoftKeyboard())

    val appCompatEditText3 = onView(
        allOf(
            withId(R.id.etValue), withText("0.0"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recyclerDynamicList),
                    0
                ),
                1
            )
        )
    )
    appCompatEditText3.perform(scrollTo(), replaceText("10.0"))

    val appCompatEditText4 = onView(
        allOf(
            withId(R.id.etValue), withText("10.0"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recyclerDynamicList),
                    0
                ),
                1
            ),
            isDisplayed()
        )
    )
    appCompatEditText4.perform(closeSoftKeyboard())

    val materialButton6 = onView(
        allOf(
            withId(R.id.btnAdd),
            childAtPosition(
                childAtPosition(
                    withClassName(`is`("android.widget.ScrollView")),
                    0
                ),
                2
            )
        )
    )
    materialButton6.perform(scrollTo(), click())

    val billState = onView(
        allOf(
            withText("Facturas"),
            isDisplayed()
        )
    )
    billState.check(matches(isDisplayed()))
}

fun removeFirstBill() {
    //TODO
}

fun editFirstBill() {
    //TODO
}

private fun clickMenuOption(optionId: Int, optionIndex: Int) {
    val materialButton3 = onView(
        allOf(
            withId(R.id.btnMenuLines),
            childAtPosition(
                allOf(
                    withId(R.id.main),
                    childAtPosition(
                        withId(R.id.drawerLayout),
                        0
                    )
                ),
                2
            ),
            isDisplayed()
        )
    )
    materialButton3.perform(click())

    val navigationMenuItemView = onView(
        allOf(
            withId(optionId),
            childAtPosition(
                allOf(
                    withId(com.google.android.material.R.id.design_navigation_view),
                    childAtPosition(
                        withId(R.id.navigationView),
                        0
                    )
                ),
                optionIndex
            ),
            isDisplayed()
        )
    )
    navigationMenuItemView.check(matches(isDisplayed())).perform(click())
}
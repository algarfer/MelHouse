package com.uniovi.melhouse.presentation.activities

import android.view.View
import android.view.ViewGroup
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
import com.uniovi.melhouse.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher

fun singIn() {
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
    textInputEditText.perform(replaceText("measdadsl@mel.mel"), closeSoftKeyboard())

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

    val textView = onView(
        allOf(
            withId(R.id.tvNoFlat), withText("Entrar a un piso"),
            withParent(withParent(withId(R.id.menuOptionsFragment))),
            isDisplayed()
        )
    )
    textView.check(matches(withText("Entrar a un piso")))
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
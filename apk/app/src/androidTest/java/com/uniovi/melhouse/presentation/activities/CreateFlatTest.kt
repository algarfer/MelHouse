package com.uniovi.melhouse.presentation.activities

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.preference.Prefs
import com.uniovi.melhouse.preferences.TestPrefs
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CreateFlatTest {

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
    fun createFlatTest() = runTest {
        mockkStatic("com.uniovi.melhouse.data.repository.user.UserRepositoryKt")

        val user = mockk<User>(relaxed = true)

        // Configurar el mock de la función suspendida
//        coEvery { user.loadTasks(any()) } just Runs

        signIn()
        createFlat()
    }
}

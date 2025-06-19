package com.uniovi.melhouse.presentation.activities

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.uniovi.melhouse.di.modules.TestRepositoriesModule
import com.uniovi.melhouse.preference.Prefs
import com.uniovi.melhouse.preferences.TestPrefs
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class BillTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Rule(order = 1)
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SplashScreenActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.POST_NOTIFICATIONS"
        )

    @Before
    fun init() {
        hiltRule.inject()
        TestRepositoriesModule.clearAll()
    }

    @BindValue
    @JvmField
    val prefs: Prefs = TestPrefs()

    @Test
    fun createBillTest() {
        signIn()
        createFlat()
        createBill()
    }

    @Test
    fun removeBillTest() {
        //TODO
        /*signIn()
        createFlat()
        createBill()
        removeFirstBill()*/
    }

    @Test
    fun editBillTest() {
        //TODO
        /*signIn()
        createFlat()
        createBill()
        editFirstBill()*/
    }


}
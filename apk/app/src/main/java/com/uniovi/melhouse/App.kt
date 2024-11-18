package com.uniovi.melhouse

import android.app.Application
import android.app.UiModeManager
import com.uniovi.melhouse.preference.Prefs
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject lateinit var prefs: Prefs

    override fun onCreate() {
        super.onCreate()
        // Enable dark mode based on the system settings
        val uiModeManager = getSystemService(UI_MODE_SERVICE) as UiModeManager
        uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_AUTO)

        prefs.setContext(this)
    }
}
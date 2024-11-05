package com.uniovi.melhouse

import android.app.Application
import android.app.UiModeManager
import com.uniovi.melhouse.data.database.SQLite
import com.uniovi.melhouse.presentation.Prefs

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Enable dark mode based on the system settings
        val uiModeManager = getSystemService(UI_MODE_SERVICE) as UiModeManager
        uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_AUTO)

        SQLite.init(this)
        Prefs.init(this)
    }
}
package com.uniovi.melhouse

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.uniovi.melhouse.preference.Prefs
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject lateinit var prefs: Prefs

    override fun onCreate() {
        super.onCreate()
        prefs.setContext(this)
        if (prefs.getDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
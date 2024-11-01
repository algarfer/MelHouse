package com.uniovi.melhouse

import android.app.Application
import com.uniovi.melhouse.data.database.SQLite

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SQLite.init(this)
    }
}
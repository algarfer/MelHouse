package com.uniovi.melhouse.preference

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Prefs @Inject constructor() {

    private val SHARED_NAME = "MelhousePrefs"

    @Volatile
    private var storage: SharedPreferences? = null

    fun setContext(context: Context) {
        storage = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
    }

    fun clearAll() {
        storage!!
            .edit()
            .clear()
            .apply()
    }
}
package com.uniovi.melhouse.presentation

import android.content.Context
import android.content.SharedPreferences

object Prefs {

    private const val SHARED_NAME = "MelhousePrefs"
    private const val SHARED_USER_NAME = "username"

    @Volatile
    private var storage: SharedPreferences? = null

    fun init(context: Context) {
        if(storage != null) return
        storage = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
    }

    fun setUserName(newUserName: String) {
        storage!!.edit().putString(SHARED_USER_NAME, newUserName).apply()
    }

    fun getUserName(): String {
        return storage!!.getString(SHARED_USER_NAME, "")!!
    }
}
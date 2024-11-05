package com.uniovi.melhouse.presentation

import android.content.Context
import android.content.SharedPreferences
import java.util.UUID

object Prefs {

    private const val SHARED_NAME = "MelhousePrefs"
    private const val SHARED_USER_NAME = "username"
    private const val SHARED_USER_ID = "user_id"
    private const val SHARED_FLAT_ID = "flat_id"

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

    fun setFlatId(newId: UUID) {
        storage!!.edit().putString(SHARED_FLAT_ID, newId.toString()).apply()
    }

    fun getFlatId(): UUID {
        return UUID.fromString(storage!!.getString(SHARED_FLAT_ID, "")!!)
    }

    fun setUserId(newId: UUID) {
        storage!!.edit().putString(SHARED_USER_ID, newId.toString()).apply()
    }

    fun getUserId(): UUID {
        return UUID.fromString(storage!!.getString(SHARED_USER_ID, "")!!)
    }
}
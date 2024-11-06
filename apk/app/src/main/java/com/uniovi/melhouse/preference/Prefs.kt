package com.uniovi.melhouse.preference

import android.content.Context
import android.content.SharedPreferences
import java.util.UUID

object Prefs {

    private const val SHARED_NAME = "MelhousePrefs"
    private const val SHARED_NAME_USER = "name"
    private const val SHARED_EMAIL = "email"
    private const val SHARED_USER_ID = "user_id"
    private const val SHARED_FLAT_ID = "flat_id"

    @Volatile
    private var storage: SharedPreferences? = null

    fun init(context: Context) {
        if(storage != null) return
        storage = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
    }

    fun setEmail(newEmail: String) {
        storage!!.edit().putString(SHARED_EMAIL, newEmail).apply()
    }

    fun getEmail(): String {
        return storage!!.getString(SHARED_EMAIL, "")!!
    }

    fun setFlatId(newId: UUID?) {
        storage!!.edit().putString(SHARED_FLAT_ID, newId.toString()).apply()
    }

    fun getFlatId(): UUID? {
        val uuid = storage!!.getString(SHARED_FLAT_ID, "")
        return if(uuid!!.isEmpty()) {
            null
        } else {
            UUID.fromString(uuid)
        }
    }

    fun setUserId(newId: UUID) {
        storage!!.edit().putString(SHARED_USER_ID, newId.toString()).apply()
    }

    fun getUserId(): UUID {
        return UUID.fromString(storage!!.getString(SHARED_USER_ID, "")!!)
    }

    fun setName(name: String) {
        storage!!.edit().putString(SHARED_NAME_USER, name).apply()
    }

    fun getName(): String {
        return storage!!.getString(SHARED_NAME_USER, "")!!
    }
}
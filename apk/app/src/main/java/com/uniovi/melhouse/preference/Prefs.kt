package com.uniovi.melhouse.preference

import android.content.Context
import android.content.SharedPreferences
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Prefs @Inject constructor() {

    private val SHARED_NAME = "MelhousePrefs"
    private val SHARED_NAME_USER = "name"
    private val SHARED_EMAIL = "email"
    private val SHARED_USER_ID = "user_id"
    private val SHARED_FLAT_ID = "flat_id"

    @Volatile
    private var storage: SharedPreferences? = null

    fun setContext(context: Context) {
        storage = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
    }

    fun setEmail(newEmail: String?) {
        storage!!
            .edit()
            .putString(SHARED_EMAIL, newEmail)
            .apply()
    }

    fun getEmail(): String {
        return storage!!.getString(SHARED_EMAIL, "")!!
    }

    fun setFlatId(newId: UUID?) {
        storage!!
            .edit()
            .putString(SHARED_FLAT_ID, newId.toString())
            .apply()
    }

    fun getFlatId(): UUID? {
        return storage!!.getString(SHARED_FLAT_ID, null)?.let {
            UUID.fromString(it)
        }
    }

    fun setUserId(newId: UUID) {
        storage!!
            .edit()
            .putString(SHARED_USER_ID, newId.toString())
            .apply()
    }

    fun getUserId(): UUID {
        return UUID.fromString(storage!!.getString(SHARED_USER_ID, "")!!)
    }

    fun setName(name: String) {
        storage!!
            .edit()
            .putString(SHARED_NAME_USER, name)
            .apply()
    }

    fun getName(): String {
        return storage!!.getString(SHARED_NAME_USER, "")!!
    }

    fun clearAll() {
        storage!!
            .edit()
            .clear()
            .apply()
    }
}
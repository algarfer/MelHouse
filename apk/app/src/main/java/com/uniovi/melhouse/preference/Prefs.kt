package com.uniovi.melhouse.preference

import android.content.Context
import android.content.SharedPreferences
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class Prefs @Inject constructor() {

    companion object {
        private const val SHARED_NAME = "MelhousePrefs"
        private const val SHARED_FLAT_ID = "flatId"
        private const val IS_DARK_MODE_ENABLED = "darkMode"
        private const val FCM_TOKEN = "fcmToken"
        private const val FCM_TOKEN_STORED_SERVER = "fcmTokenStoredServer"
    }

    @Volatile
    private var storage: SharedPreferences? = null

    open fun setContext(context: Context) {
        storage = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
    }

    open fun setFlatId(flatId: UUID?) {
        storage!!
            .edit()
            .putString(SHARED_FLAT_ID, flatId?.toString())
            .apply()
    }

    open fun getFlatId(): UUID? {
        return storage!!
            .getString(SHARED_FLAT_ID, null)
            ?.let { UUID.fromString(it) }
    }

    open fun clearAll() {
        storage!!
            .edit()
            .remove(SHARED_FLAT_ID)
            .remove(FCM_TOKEN_STORED_SERVER)
            .apply()
    }

    open fun setDarkMode(value : Boolean) {
        storage!!
            .edit()
            .putBoolean(IS_DARK_MODE_ENABLED, value)
            .apply()
    }

    open fun getDarkMode(): Boolean {
        return storage!!
            .getBoolean(IS_DARK_MODE_ENABLED, false)
    }

    open fun setFcmToken(token: String) {
        storage!!
            .edit()
            .putString(FCM_TOKEN, token)
            .apply()
    }

    open fun getFcmToken(): String? {
        return storage!!
            .getString(FCM_TOKEN, null)
    }

    open fun setFcmTokenStoredServer(value: Boolean) {
        storage!!
            .edit()
            .putBoolean(FCM_TOKEN_STORED_SERVER, value)
            .apply()
    }

    open fun getFcmTokenStoredServer(): Boolean {
        return storage!!
            .getBoolean(FCM_TOKEN_STORED_SERVER, false)
    }

}
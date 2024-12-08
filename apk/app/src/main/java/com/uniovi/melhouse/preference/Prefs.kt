package com.uniovi.melhouse.preference

import android.content.Context
import android.content.SharedPreferences
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Prefs @Inject constructor() {

    companion object {
        private const val SHARED_NAME = "MelhousePrefs"
        private const val SHARED_FLAT_ID = "flatId"
    }

    @Volatile
    private var storage: SharedPreferences? = null

    fun setContext(context: Context) {
        storage = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
    }

    fun setFlatId(flatId: UUID?) {
        storage!!
            .edit()
            .putString(SHARED_FLAT_ID, flatId?.toString())
            .apply()
    }

    fun getFlatId(): UUID? {
        return storage!!
            .getString(SHARED_FLAT_ID, null)
            ?.let { UUID.fromString(it) }
    }

    fun clearAll() {
        storage!!
            .edit()
            .clear()
            .apply()
    }
}
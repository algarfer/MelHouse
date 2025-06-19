package com.uniovi.melhouse.preferences

import android.content.Context
import com.uniovi.melhouse.preference.Prefs
import java.util.UUID

class TestPrefs : Prefs() {
    private var flatId: UUID? = null

    override fun setContext(context: Context) {}

    override fun setFlatId(flatId: UUID?) {
        this.flatId = flatId
    }

    override fun getFlatId(): UUID? {
        return flatId
    }

    override fun clearAll() {
        flatId = null
    }

    override fun setFcmToken(token: String) {}

    override fun getFcmToken() = ""

    override fun setFcmTokenStoredServer(value: Boolean) {}

    override fun getFcmTokenStoredServer() = true
}
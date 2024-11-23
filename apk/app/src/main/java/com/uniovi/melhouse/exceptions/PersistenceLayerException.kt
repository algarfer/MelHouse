package com.uniovi.melhouse.exceptions

import android.content.Context
import androidx.annotation.StringRes

class PersistenceLayerException(private val defaultMessage: String, @StringRes val code: Int? = null) : Exception() {

    fun getMessage(context: Context): String {
        return if (code != null) {
            context.getString(code)
        } else {
            defaultMessage
        }
    }
}
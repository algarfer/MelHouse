package com.uniovi.melhouse.exceptions

import android.content.Context
import androidx.annotation.StringRes
import com.uniovi.melhouse.R

class PersistenceLayerException(override val message: String, @StringRes val code: Int? = null) :
    Exception() {

    fun getMessage(context: Context): String {
        return if (code != null) {
            "${context.getString(R.string.error_error)}: ${context.getString(code)}"
        } else {
            message
        }
    }
}
package com.uniovi.melhouse.data

import com.uniovi.melhouse.R
import com.uniovi.melhouse.exceptions.PersistenceLayerException
import io.github.jan.supabase.exceptions.RestException

object Executor {

    suspend fun <T> safeCall(action: suspend () -> T): T {
        return try {
            action()
        } catch (e: RestException) {
            handleRestException(e)
        }
    }

    private fun handleRestException(exception: RestException): Nothing {
        val code = if (exception.statusCode == 400 && exception.description == "Invalid login credentials") {
            R.string.error_invalid_login_credentials
        } else if (exception.statusCode == 422 && exception.error == "user_already_exists") {
            R.string.error_form_signup_user_already_exists
        } else if (exception.statusCode == 500 && exception.error == "flat_not_found") {
            R.string.error_flat_not_found
        } else {
            R.string.error_unknown
        }

        throw PersistenceLayerException(exception.description ?: exception.error, code)
    }
}
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
        } else if(exception.error == "cannot_delete_user_in_flat") {
            R.string.error_cannot_delete_user_in_flat
        } else if(exception.error == "cannot_leave_empty_flat_admin") {
            R.string.error_cannot_leave_empty_flat_admin
        } else if(exception.error == "admin_not_in_flat") {
            R.string.error_admin_not_in_flat
        } else if(exception.error == "max_user_flat") {
            R.string.error_max_user_flat
        } else if(exception.error == "user_no_flat") {
            R.string.error_user_no_flat
        } else {
            null
        }

        throw PersistenceLayerException(exception.description ?: exception.error, code)
    }
}
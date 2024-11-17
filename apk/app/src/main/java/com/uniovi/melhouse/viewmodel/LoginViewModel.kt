package com.uniovi.melhouse.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.database.Database
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.di.qualifiers.SupabaseDatabaseQualifier
import com.uniovi.melhouse.preference.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @SupabaseDatabaseQualifier private val userRepository: UserRepository,
    private val supabase: Database<SupabaseClient>
) : ViewModel() {

    val passwordError: MutableLiveData<String?> = MutableLiveData(null)
    val emailError: MutableLiveData<String?> = MutableLiveData(null)
    val loginSuccessfull: MutableLiveData<Boolean> = MutableLiveData(false)

    fun login(email: String, password: String, context: Context) {
        var areErrors = false
        if(email.isEmpty()) {
            emailError.postValue(context.getString(R.string.error_form_login_email_empty))
            areErrors = true
        }

        if(password.isEmpty()) {
            passwordError.postValue(context.getString(R.string.error_form_login_password_empty))
            areErrors = true
        }

        if(areErrors) return

        val supabaseClient = supabase.getInstance()

        viewModelScope.launch(Dispatchers.IO) {
            supabaseClient.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            supabaseClient
                .auth.sessionManager
                .saveSession(
                    supabaseClient
                        .auth
                        .currentSessionOrNull()!!
                )

            userRepository
                .findById(
                    UUID
                        .fromString(
                            supabaseClient
                                .auth
                                .currentUserOrNull()!!
                                .id))
                ?.let {
                Prefs.setUserId(it.id)
                Prefs.setEmail(it.email)
                Prefs.setFlatId(it.flatId)
                Prefs.setName(it.name)
            }

            loginSuccessfull.postValue(true)
        }
    }
}
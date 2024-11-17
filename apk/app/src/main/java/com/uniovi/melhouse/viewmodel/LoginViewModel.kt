package com.uniovi.melhouse.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.repository.user.UserRepository
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
    private val userRepository: UserRepository,
    private val supabase: SupabaseClient
) : ViewModel() {

    val passwordError: LiveData<String?>
        get() = _passwordError
    private val _passwordError: MutableLiveData<String?> = MutableLiveData(null)
    private val _emailError: MutableLiveData<String?> = MutableLiveData(null)
    val emailError: LiveData<String?>
        get() = _emailError
    val loginSuccessfull: LiveData<Boolean>
        get() = _loginSuccessfull
    private val _loginSuccessfull = MutableLiveData(false)

    fun login(email: String, password: String, context: Context) {
        var areErrors = false
        if(email.isEmpty()) {
            _emailError.postValue(context.getString(R.string.error_form_login_email_empty))
            areErrors = true
        }

        if(password.isEmpty()) {
            _passwordError.postValue(context.getString(R.string.error_form_login_password_empty))
            areErrors = true
        }

        if(areErrors) return

        viewModelScope.launch(Dispatchers.IO) {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            supabase
                .auth.sessionManager
                .saveSession(
                    supabase
                        .auth
                        .currentSessionOrNull()!!
                )

            userRepository
                .findById(
                    UUID
                        .fromString(
                            supabase
                                .auth
                                .currentUserOrNull()!!
                                .id))
                ?.let {
                Prefs.setUserId(it.id)
                Prefs.setEmail(it.email)
                Prefs.setFlatId(it.flatId)
                Prefs.setName(it.name)
            }

            _loginSuccessfull.postValue(true)
        }
    }
}
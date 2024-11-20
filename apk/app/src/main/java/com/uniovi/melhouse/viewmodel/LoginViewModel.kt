package com.uniovi.melhouse.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.SupabaseUserSessionFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val supabaseUserSessionFacade: SupabaseUserSessionFacade
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
            supabaseUserSessionFacade.logIn(email, password)

            _loginSuccessfull.postValue(true)
        }
    }
}
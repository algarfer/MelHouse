package com.uniovi.melhouse.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.SupabaseUserSessionFacade
import com.uniovi.melhouse.exceptions.PersistenceLayerException
import com.uniovi.melhouse.preference.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val supabaseUserSessionFacade: SupabaseUserSessionFacade,
    private val prefs: Prefs,
    @ApplicationContext private val applicationContext: Context
) : ViewModel() {

    val passwordError: LiveData<String?>
        get() = _passwordError
    private val _passwordError: MutableLiveData<String?> = MutableLiveData(null)
    val emailError: LiveData<String?>
        get() = _emailError
    private val _emailError: MutableLiveData<String?> = MutableLiveData(null)
    val loginSuccessfull: LiveData<Boolean>
        get() = _loginSuccessfull
    private val _loginSuccessfull = MutableLiveData(false)
    val genericError: LiveData<String>
        get() = _genericError
    private val _genericError = MutableLiveData<String>(null)

    fun login(email: String, password: String) {
        var areErrors = false
        if(email.isEmpty()) {
            _emailError.postValue(applicationContext.getString(R.string.error_form_login_email_empty))
            areErrors = true
        }

        if(password.isEmpty()) {
            _passwordError.postValue(applicationContext.getString(R.string.error_form_login_password_empty))
            areErrors = true
        }

        if(areErrors) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                supabaseUserSessionFacade.logIn(email.trim(), password)
                supabaseUserSessionFacade.getUserData().let {
                    prefs.setFlatId(it.flatId)
                }
                _loginSuccessfull.postValue(true)
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.getMessage(applicationContext))
            }
        }
    }
}
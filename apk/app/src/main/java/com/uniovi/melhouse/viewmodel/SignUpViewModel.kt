package com.uniovi.melhouse.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.SupabaseUserSessionFacade
import com.uniovi.melhouse.preference.Prefs
import com.uniovi.melhouse.utils.validateEmail
import com.uniovi.melhouse.utils.validateLength
import com.uniovi.melhouse.utils.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val prefs: Prefs,
    private val supabaseUserSessionFacade: SupabaseUserSessionFacade
) : ViewModel() {

    val nameError: LiveData<String?>
        get() = _nameError
    private val _nameError: MutableLiveData<String?> = MutableLiveData(null)
    val emailError: LiveData<String?>
        get() = _emailError
    private val _emailError: MutableLiveData<String?> = MutableLiveData(null)
    val passwordError: LiveData<String?>
        get() = _passwordError
    private val _passwordError: MutableLiveData<String?> = MutableLiveData(null)
    val password2Error: LiveData<String?>
        get() = _password2Error
    private val _password2Error: MutableLiveData<String?> = MutableLiveData(null)
    val signupSuccessfull: LiveData<Boolean>
        get() = _signupSuccessfull
    private val _signupSuccessfull = MutableLiveData(false)

    fun signup(name: String, email: String, password: String, password2: String, context: Context) {
        if(preCheck(context, name, email, password, password2)) return

        viewModelScope.launch(Dispatchers.IO) {
            supabaseUserSessionFacade.signUp(email, password, name).let {
                prefs.setUserId(it.id)
                prefs.setEmail(it.email)
                prefs.setFlatId(it.flatId)
                prefs.setName(it.name)
            }

            _signupSuccessfull.postValue(true)
        }
    }

    private fun preCheck(context: Context, name: String, email: String, password: String, password2: String): Boolean {
        var areErrors = false

        if(name.isEmpty()) {
            _nameError.postValue(context.getString(R.string.error_form_signup_name_empty))
            areErrors = true
        } else {
            if(!name.validateLength()) {
                _nameError.postValue(context.getString(R.string.error_form_signup_name_length))
                areErrors = true
            } else {
                _nameError.postValue(null)
            }
        }
        if(email.isEmpty()) {
            _emailError.postValue(context.getString(R.string.error_form_signup_email_empty))
            areErrors = true
        } else {
            if(!name.validateLength()) {
                _emailError.postValue(context.getString(R.string.error_form_signup_email_length))
                areErrors = true
            } else {
                if(!email.validateEmail()) {
                    _emailError.postValue(context.getString(R.string.error_form_signup_email_invalid))
                    areErrors = true
                } else {
                    _emailError.postValue(null)
                }
            }
        }

        if (password.isEmpty()) {
            _passwordError.postValue(context.getString(R.string.error_form_signup_password_empty))
            areErrors = true
        } else {
            if(!password.validatePassword()) {
                _passwordError.postValue(context.getString(R.string.error_form_signup_password_invalid))
                areErrors = true
            } else {
                if(!password.validateLength(0, 50)) {
                    _passwordError.postValue(context.getString(R.string.error_form_signup_password_length))
                    areErrors = true
                } else {
                    _passwordError.postValue(null)
                }
            }
        }
        if(password != password2) {
            _password2Error.postValue(context.getString(R.string.error_form_signup_password_not_match))
            areErrors = true
        } else {
            _password2Error.postValue(null)
        }

        return areErrors
    }
}
package com.uniovi.melhouse.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.database.Database
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.di.qualifiers.SupabaseDatabaseQualifier
import com.uniovi.melhouse.preference.Prefs
import com.uniovi.melhouse.utils.validateEmail
import com.uniovi.melhouse.utils.validateLength
import com.uniovi.melhouse.utils.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    @SupabaseDatabaseQualifier private val userRepository: UserRepository,
    private val supabase: Database<SupabaseClient>
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

        val supabaseClient = supabase.getInstance()

        viewModelScope.launch(Dispatchers.IO) {
            supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                data = buildJsonObject {
                    put("name", name)
                }
            }

            supabaseClient
                .auth
                .sessionManager
                .saveSession(
                    supabaseClient
                        .auth
                        .currentSessionOrNull()!!)

            userRepository.findById(UUID.fromString(supabaseClient.auth.currentUserOrNull()!!.id))?.let {
                Prefs.setUserId(it.id)
                Prefs.setEmail(it.email)
                Prefs.setFlatId(it.flatId)
                Prefs.setName(it.name)
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
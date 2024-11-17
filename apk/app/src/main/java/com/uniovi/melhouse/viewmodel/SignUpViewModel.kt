package com.uniovi.melhouse.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.di.qualifiers.SupabaseDatabaseQualifier
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
    @SupabaseDatabaseQualifier private val userRepository: UserRepository,
) : ViewModel() {
    val nameError: MutableLiveData<String?> = MutableLiveData(null)
    val emailError: MutableLiveData<String?> = MutableLiveData(null)
    val passwordError: MutableLiveData<String?> = MutableLiveData(null)
    val password2Error: MutableLiveData<String?> = MutableLiveData(null)
    val signupSuccessfull: MutableLiveData<Boolean> = MutableLiveData(false)

    fun signup(name: String, email: String, password: String, password2: String, context: Context) {

        if(preCheck(context, name, email, password, password2)) return

        viewModelScope.launch(Dispatchers.IO) {
            val previous = userRepository.findByEmail(email)

            if(previous != null) {
                emailError.postValue(context.getString(R.string.error_form_signup_user_already_exists))
                return@launch
            }

            val user = User(
                name = name,
                email = email,
                flatId = null
            )

            userRepository.insert(user)

            Prefs.setUserId(user.id)
            Prefs.setEmail(user.email)
            Prefs.setFlatId(user.flatId)
            Prefs.setName(user.name)

            signupSuccessfull.postValue(true)
        }
    }

    private fun preCheck(context: Context, name: String, email: String, password: String, password2: String): Boolean {
        var areErrors = false

        if(name.isEmpty()) {
            nameError.postValue(context.getString(R.string.error_form_signup_name_empty))
            areErrors = true
        } else {
            if(!name.validateLength()) {
                nameError.postValue(context.getString(R.string.error_form_signup_name_length))
                areErrors = true
            } else {
                nameError.postValue(null)
            }
        }
        if(email.isEmpty()) {
            emailError.postValue(context.getString(R.string.error_form_signup_email_empty))
            areErrors = true
        } else {
            if(!name.validateLength()) {
                emailError.postValue(context.getString(R.string.error_form_signup_email_length))
                areErrors = true
            } else {
                if(!email.validateEmail()) {
                    emailError.postValue(context.getString(R.string.error_form_signup_email_invalid))
                    areErrors = true
                } else {
                    emailError.postValue(null)
                }
            }
        }

        // TODO - Enable with remote database
//        if (password.isEmpty()) {
//            passwordError.postValue(context.getString(R.string.error_form_signup_password_empty))
//            areErrors = true
//        } else {
//            if(!password.validatePassword()) {
//                passwordError.postValue(context.getString(R.string.error_form_signup_password_invalid))
//                areErrors = true
//            } else {
//                if(!password.validateLength(0, 50)) {
//                    passwordError.postValue(context.getString(R.string.error_form_signup_password_length))
//                    areErrors = true
//                } else {
//                    passwordError.postValue(null)
//                }
//            }
//        }
//        if(password != password2) {
//            password2Error.postValue(context.getString(R.string.error_form_signup_password_not_match))
//            areErrors = true
//        } else {
//            password2Error.postValue(null)
//        }

        return areErrors
    }
}
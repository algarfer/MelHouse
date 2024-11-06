package com.uniovi.melhouse.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.di.qualifiers.SQLiteDatabaseQualifier
import com.uniovi.melhouse.preference.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    @SQLiteDatabaseQualifier private val userRepository: UserRepository
) : ViewModel() {
    val emailError: MutableLiveData<String?> = MutableLiveData(null)
    val passwordError: MutableLiveData<String?> = MutableLiveData(null)
    val password2Error: MutableLiveData<String?> = MutableLiveData(null)
    val signupSuccessfull: MutableLiveData<Boolean> = MutableLiveData(false)

    // TODO - Add fields validation
    fun signup(name: String, email: String, password: String, password2: String, context: Context) {

        if(password != password2) {
            password2Error.postValue(context.getString(R.string.error_form_signup_password_not_match))
            return
        }

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
}
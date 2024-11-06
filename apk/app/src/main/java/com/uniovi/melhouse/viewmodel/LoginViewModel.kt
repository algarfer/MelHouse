package com.uniovi.melhouse.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.di.qualifiers.SQLiteDatabaseQualifier
import com.uniovi.melhouse.preference.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @SQLiteDatabaseQualifier private val userRepository: UserRepository
) : ViewModel() {

    val passwordError: MutableLiveData<String?> = MutableLiveData(null)
    val emailError: MutableLiveData<String?> = MutableLiveData(null)
    val loginSuccessfull: MutableLiveData<Boolean> = MutableLiveData(false)

    // TODO - Improve for backend server
    // TODO - Add fields validation
    fun login(email: String, password: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userRepository.findByEmail(email)
            if (user != null) {
                Prefs.setUserId(user.id)
                Prefs.setEmail(user.email)
                Prefs.setFlatId(user.flatId)
                Prefs.setName(user.name)
                loginSuccessfull.postValue(true)
            } else {
                emailError.postValue(context.getString(R.string.error_form_login_user_not_found))
            }
        }
    }
}
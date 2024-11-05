package com.uniovi.melhouse.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.database.SQLite
import com.uniovi.melhouse.databinding.ActivityLoginBinding
import com.uniovi.melhouse.presentation.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    val passwordError: MutableLiveData<String?> = MutableLiveData(null)
    val emailError: MutableLiveData<String?> = MutableLiveData(null)
    val loginSuccessfull: MutableLiveData<Boolean> = MutableLiveData(false)

    // TODO - Improve for backend server
    // TODO - Move to correct layer
    // TODO - Add fields validation
    fun login(email: String, password: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = SQLite.getUserRepository().findByEmail(email)
            if (user != null) {
                Prefs.setUserId(user.id)
                Prefs.setEmail(user.email)
                Prefs.setFlatId(user.flatId)
                loginSuccessfull.postValue(true)
            } else {
                emailError.postValue(context.getString(R.string.error_form_login_user_not_found))
            }
        }
    }
}
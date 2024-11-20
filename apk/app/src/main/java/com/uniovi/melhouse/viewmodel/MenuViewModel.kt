package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.SupabaseUserSessionFacade
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.preference.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val prefs:Prefs,
    private val userSessionFacade: SupabaseUserSessionFacade
) : ViewModel() {

    val user: LiveData<User?>
        get() = _user
    private val _user = MutableLiveData<User?>(null)

//    val isLogged: LiveData<Boolean>
//        get() = _isLogged
//    private val _isLogged = MutableLiveData(true)

    fun onCreate() {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(userSessionFacade.getUserData())
        }
    }

    fun logout() {
        prefs.clearAll()
//        _isLogged.postValue(false)
    }

}
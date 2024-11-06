package com.uniovi.melhouse.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uniovi.melhouse.preference.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor() : ViewModel() {
    val isReady: MutableLiveData<Boolean> = MutableLiveData(false)
    var isLogged: Boolean = false
        private set

    fun initApp() {
        Prefs.getEmail().isEmpty().let {
            isLogged = !it
        }
        isReady.postValue(true)
    }
}
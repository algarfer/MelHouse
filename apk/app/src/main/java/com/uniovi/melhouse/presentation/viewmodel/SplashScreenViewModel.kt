package com.uniovi.melhouse.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SplashScreenViewModel : ViewModel() {
    val isReady: MutableLiveData<Boolean> = MutableLiveData(false)
    var isLogged: Boolean = false
        private set

    // TODO - Move to usecase
    fun initApp() {
        // TODO - Handle session recover and determine if the user isLogged

        isReady.postValue(true)
    }
}
package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uniovi.melhouse.preference.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor() : ViewModel() {

    val isLogged: MutableLiveData<Boolean> = MutableLiveData(true)

    fun logout() {
        Prefs.clearAll()
        isLogged.postValue(false)
    }

}
package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uniovi.melhouse.preference.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val prefs:Prefs) : ViewModel() {

    val isLogged: MutableLiveData<Boolean> = MutableLiveData(true)

    fun logout() {
        prefs.clearAll()
        isLogged.postValue(false)
    }

}
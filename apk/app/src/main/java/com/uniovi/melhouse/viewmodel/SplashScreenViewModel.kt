package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val supabase : SupabaseClient
) : ViewModel() {

    val isReady: LiveData<Boolean>
        get() = _isReady
    private val _isReady = MutableLiveData(false)

    var isLogged: Boolean = false
        private set

    fun initApp() {
        viewModelScope.launch(Dispatchers.IO) {
            isLogged = supabase.auth.loadFromStorage()

            _isReady.postValue(true)
        }
    }
}
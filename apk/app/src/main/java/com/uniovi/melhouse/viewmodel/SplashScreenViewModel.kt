package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.database.Database
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val supabase : Database<SupabaseClient>
) : ViewModel() {
    val isReady: MutableLiveData<Boolean> = MutableLiveData(false)
    var isLogged: Boolean = false
        private set

    fun initApp() {
        val supabaseClient = supabase.getInstance()

        viewModelScope.launch(Dispatchers.IO) {
            isLogged = supabaseClient.auth.loadFromStorage()

            isReady.postValue(true)
        }
    }
}
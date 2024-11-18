package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.preference.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val prefs: Prefs,
    private val supabase: SupabaseClient
) : ViewModel() {

    val isLogged: LiveData<Boolean>
        get() = _isLogged
    private val _isLogged = MutableLiveData(true)

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            supabase.auth.signOut()
            supabase.auth.clearSession()

            prefs.clearAll()
            _isLogged.postValue(false)
        }
    }
}
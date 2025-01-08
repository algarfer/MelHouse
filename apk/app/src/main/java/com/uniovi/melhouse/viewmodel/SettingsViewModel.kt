package com.uniovi.melhouse.viewmodel

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.Executor
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.exceptions.PersistenceLayerException
import com.uniovi.melhouse.preference.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: Prefs,
    private val userRepository: UserRepository,
    private val supabase: SupabaseClient,
    @ApplicationContext private val applicationContext: Context
) : ViewModel() {

    val genericError: LiveData<String?>
        get() = _genericError
    private val _genericError = MutableLiveData<String?>()

    val goToStart: LiveData<Boolean>
        get() = _goToStart
    private val _goToStart = MutableLiveData(false)

    fun setDarkMode(b: Boolean) {
        if (b) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        prefs.setDarkMode(b)
    }

    fun getDarkMode(): Boolean {
        return prefs.getDarkMode()
    }

    fun deleteUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Executor.safeCall {
                    userRepository.deleteUserForever()

                    supabase.auth.signOut()
                    supabase.auth.clearSession()

                    prefs.clearAll()
                    _goToStart.postValue(true)
                }
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.getMessage(applicationContext))
            }
        }
    }
}

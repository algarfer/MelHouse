package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.SupabaseUserSessionFacade
import com.uniovi.melhouse.exceptions.PersistenceLayerException
import com.uniovi.melhouse.preference.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val supabaseUserSessionFacade: SupabaseUserSessionFacade,
    private val prefs: Prefs
) : ViewModel() {

    val isReady: LiveData<Boolean>
        get() = _isReady
    private val _isReady = MutableLiveData(false)

    var isLogged: Boolean = false
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLogged = supabaseUserSessionFacade.loadFromStorage()

                if (!isLogged) prefs.clearAll()
            } catch (e: PersistenceLayerException) {
                prefs.clearAll()
                supabaseUserSessionFacade.clearSession()
            } finally {
                _isReady.postValue(true)
            }
        }
    }

    fun updateFCMToken(token: String) {
        val previousToken = prefs.getFcmToken()

        if (previousToken == null || previousToken != token) {
            prefs.setFcmToken(token)
            prefs.setFcmTokenStoredServer(false)
        }

        uploadToken()
    }

    private fun uploadToken() {
        supabaseUserSessionFacade.getUserId()?.let {
            if (!prefs.getFcmTokenStoredServer()) {
                viewModelScope.launch(Dispatchers.IO) {
                    supabaseUserSessionFacade.updateFCMToken(prefs.getFcmToken())
                }
            }
        }
    }
}
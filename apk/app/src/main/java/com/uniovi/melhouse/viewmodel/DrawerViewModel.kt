package com.uniovi.melhouse.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.SupabaseUserSessionFacade
import com.uniovi.melhouse.data.model.Flat
import com.uniovi.melhouse.data.model.User
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
class DrawerViewModel @Inject constructor(
    private val prefs:Prefs,
    private val userSessionFacade: SupabaseUserSessionFacade,
    private val supabase: SupabaseClient,
    @ApplicationContext private val applicationContext: Context
) : ViewModel() {

    val user: LiveData<User?>
        get() = _user
    private val _user = MutableLiveData<User?>(null)
    val flat: LiveData<Flat?>
        get() = _flat
    private val _flat = MutableLiveData<Flat?>(null)
    // TODO - Try to handle in AbstractActivity
    val isLogged: LiveData<Boolean>
        get() = _isLogged
    private val _isLogged = MutableLiveData(true)
    val genericError: LiveData<String?>
        get() = _genericError
    private val _genericError = MutableLiveData<String?>(null)

    fun onCreate() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _user.postValue(userSessionFacade.getUserData())
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.getMessage(applicationContext))
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _flat.postValue(userSessionFacade.getFlat())
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.getMessage(applicationContext))
            }
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            userSessionFacade.updateFCMToken(null)
            supabase.auth.signOut()
            supabase.auth.clearSession()

            prefs.clearAll()
            _isLogged.postValue(false)
        }
    }
}
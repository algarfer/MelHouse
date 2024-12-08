package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.SupabaseUserSessionFacade
import com.uniovi.melhouse.data.model.Flat
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.repository.flat.FlatRepository
import com.uniovi.melhouse.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlatFragmentViewModel @Inject constructor(
    private val userSessionFacade: SupabaseUserSessionFacade,
    private val usersRepository: UserRepository,
    private val flatRepository: FlatRepository
) : ViewModel() {

    val flat: LiveData<Flat>
        get() = _flat
    private val _flat = MutableLiveData<Flat>(null)

    val partners: LiveData<List<User>>
        get() = _partners
    private val _partners = MutableLiveData<List<User>>(emptyList())

    val isAdmin: LiveData<Boolean>
        get() = _isAdmin
    private val _isAdmin = MutableLiveData(false)

    val currentUser: LiveData<User?>
        get() = _currentUser
    private val _currentUser = MutableLiveData<User?>(null)

    val done: LiveData<Boolean>
        get() = _done
    private val _done = MutableLiveData(false)

    fun onCreate() {
        viewModelScope.launch(Dispatchers.IO) {
            userSessionFacade.getFlat().let {
                _flat.postValue(it)
            }
        }
        checkAdmin()
    }

    private fun checkAdmin() {
        viewModelScope.launch(Dispatchers.IO) {
            userSessionFacade.isFlatAdmin().let {
                _isAdmin.postValue(it)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            usersRepository.getRoommates(userSessionFacade.getFlat()!!.id).let {
                _partners.postValue(it)
            }

            userSessionFacade.getUserData().let {
                _currentUser.postValue(it)
            }

            _done.postValue(true)
        }
    }

    fun promoteToAdmin(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val newFlat = userSessionFacade.getFlat()!!.copy(adminId = user.id)
            flatRepository.update(newFlat)

            checkAdmin() // Update View
        }
    }

    fun kickUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val newUser = user.copy(flatId = null)
            usersRepository.update(newUser)

            checkAdmin() // Update View
        }
    }
}
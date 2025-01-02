package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.Executor
import com.uniovi.melhouse.data.SupabaseUserSessionFacade
import com.uniovi.melhouse.data.model.Flat
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.repository.flat.FlatRepository
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.data.repository.user.loadTasks
import com.uniovi.melhouse.exceptions.PersistenceLayerException
import com.uniovi.melhouse.preference.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlatFragmentViewModel @Inject constructor(
    private val userSessionFacade: SupabaseUserSessionFacade,
    private val usersRepository: UserRepository,
    private val flatRepository: FlatRepository,
    private val taskRepository: TaskRepository,
    private val supabaseClient: SupabaseClient,
    private val prefs: Prefs
) : ViewModel() {

    val flat: LiveData<Flat> = flatRepository.findByIdAsFlow(prefs.getFlatId()!!)
        .catch { _genericError.postValue(it.message) }
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    val partners: LiveData<List<User>>
        get() = _partners
    private val _partners = MutableLiveData<List<User>>(emptyList())

    val isAdmin: LiveData<Boolean>
        get() = _isAdmin
    private val _isAdmin = MutableLiveData(false)

    val currentUser: LiveData<User?>
        get() = _currentUser
    private val _currentUser = MutableLiveData<User?>(null)

    val tasks: LiveData<List<Task>>
        get() = _tasks
    private val _tasks = MutableLiveData<List<Task>>(emptyList())

    val done: LiveData<Boolean>
        get() = _done
    private val _done = MutableLiveData(false)

    val hasLeaved: LiveData<Boolean>
        get() = _hasLeaved
    private val _hasLeaved = MutableLiveData(false)

    val genericError: LiveData<String?>
        get() = _genericError
    private val _genericError = MutableLiveData<String?>(null)

    fun onCreate() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Executor.safeCall {
                    taskRepository.findByFlatId(prefs.getFlatId()!!).let {
                        _tasks.postValue(it)
                    }
                }
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.message)
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
            try {
                Executor.safeCall {
                    usersRepository.getRoommates(prefs.getFlatId()!!).let {
                        it.forEach { it.loadTasks(supabaseClient) }
                        _partners.postValue(it)
                    }

                    userSessionFacade.getUserData().let {
                        _currentUser.postValue(it)
                    }
                    _done.postValue(true)
                }
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.message)
            }
        }
    }

    fun promoteToAdmin(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Executor.safeCall {
                    val newFlat = userSessionFacade.getFlat()!!.copy(adminId = user.id)
                    flatRepository.update(newFlat)

                    checkAdmin() // Update View
                }
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.message)
            }
        }
    }

    fun kickUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Executor.safeCall {
                    val newUser = user.copy(flatId = null)
                    usersRepository.update(newUser)

                    checkAdmin() // Update View
                }
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.message)
            }
        }
    }

    fun leave() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Executor.safeCall {
                    val user = userSessionFacade.getUserData().copy(flatId = null)
                    usersRepository.update(user)
                    _hasLeaved.postValue(true)
                    prefs.setFlatId(null)
                }
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.message)
            }
        }
    }
}
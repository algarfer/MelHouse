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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
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

    private val _flat = flatRepository.findByIdAsFlow(prefs.getFlatId()!!)
        .catch { _genericError.postValue(it.message) }
        .shareIn(viewModelScope, started = SharingStarted.Lazily)
    val flat: LiveData<Flat> = _flat
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    val partners: LiveData<List<User>> = usersRepository.getRoommatesAsFlow(prefs.getFlatId()!!)
        .map { users -> users.sortedBy { if (it.id == userSessionFacade.getUserId()!!) Int.MIN_VALUE else it.id.hashCode() } }
        .map { users -> users.map { it.loadTasks(supabaseClient); it } }
        .catch { _genericError.postValue(it.message) }
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    private val _currentUser = usersRepository.findByIdAsFlow(userSessionFacade.getUserId()!!)
        .catch { _genericError.postValue(it.message) }
        .shareIn(viewModelScope, started = SharingStarted.Lazily)
    val currentUser: LiveData<User> = _currentUser
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    val isAdmin: LiveData<Boolean> =
        _flat.combine(_currentUser) { flat, user ->
            flat.adminId == user.id
        }.catch { _genericError.postValue(it.message) }
            .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    val tasks: LiveData<List<Task>> = taskRepository.findByFlatIdAsFlow(prefs.getFlatId()!!)
        .catch { _genericError.postValue(it.message) }
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    val done: LiveData<Boolean>
        get() = _done
    private val _done = MutableLiveData(false)

    val hasLeaved: LiveData<Boolean>
        get() = _hasLeaved
    private val _hasLeaved = MutableLiveData(false)

    val genericError: LiveData<String?>
        get() = _genericError
    private val _genericError = MutableLiveData<String?>(null)

    fun promoteToAdmin(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Executor.safeCall {
                    val newFlat = userSessionFacade.getFlat()!!.copy(adminId = user.id)
                    flatRepository.update(newFlat)
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
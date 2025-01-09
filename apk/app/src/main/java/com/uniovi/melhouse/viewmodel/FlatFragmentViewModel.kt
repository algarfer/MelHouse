package com.uniovi.melhouse.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.Executor
import com.uniovi.melhouse.data.SupabaseUserSessionFacade
import com.uniovi.melhouse.data.model.Flat
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.repository.flat.FlatRepository
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.data.repository.taskuser.TaskUserRepository
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.exceptions.PersistenceLayerException
import com.uniovi.melhouse.preference.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    taskRepository: TaskRepository,
    taskUserRepository: TaskUserRepository,
    private val prefs: Prefs,
    @ApplicationContext private val applicationContext: Context
) : AbstractViewModel() {

    private val _flat = flatRepository.findByIdAsFlow(prefs.getFlatId()!!)
        .catch { e -> _genericError.postValue(e.localizedMessage) }
        .shareIn(viewModelScope, started = SharingStarted.Lazily)
    private val _currentUser = usersRepository.findByIdAsFlow(userSessionFacade.getUserId()!!)
        .catch { e -> _genericError.postValue(e.localizedMessage) }
        .shareIn(viewModelScope, started = SharingStarted.Lazily)
    private val _partners = usersRepository.getRoommatesAsFlow(prefs.getFlatId()!!)
        .map { users -> users.sortedBy { if (it.id == userSessionFacade.getUserId()!!) Int.MIN_VALUE else it.id.hashCode() } }
        .catch { e -> _genericError.postValue(e.localizedMessage) }
        .shareIn(viewModelScope, started = SharingStarted.Lazily)
    private val _tasks = taskRepository.findByFlatIdAsFlow(prefs.getFlatId()!!)
        .catch { e -> _genericError.postValue(e.localizedMessage) }
        .shareIn(viewModelScope, started = SharingStarted.Lazily)
    private val _userTasks = taskUserRepository.findAllAsFlow()
        .catch { e -> _genericError.postValue(e.localizedMessage) }
        .shareIn(viewModelScope, started = SharingStarted.Lazily)

    val flat: LiveData<Flat> = _flat
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    val partners = combine(_partners, _tasks, _userTasks) { users, tasks, userTasks ->
        val tasksIds = tasks.map { it.id }
        val assignments = userTasks.filter { it.taskId in tasksIds }

        users
            .map { user -> user.tasks = assignments
                .filter { it.userId == user.id }
                .map { taskId -> tasks
                    .find { it.id == taskId.taskId }!! }.toSet(); user }
    }.asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    val currentUser: LiveData<User> = _currentUser
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    val isAdmin: LiveData<Boolean> = _flat.combine(_currentUser) { flat, user ->
        flat.adminId == user.id
    }
        .catch { e -> _genericError.postValue(e.localizedMessage) }
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    val tasks: LiveData<List<Task>> = _tasks
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    // TODO - Move to abstract activity and use Realtime api to check
    val hasLeaved: LiveData<Boolean>
        get() = _hasLeaved
    private val _hasLeaved = MutableLiveData(false)

    fun promoteToAdmin(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Executor.safeCall {
                    val newFlat = userSessionFacade.getFlat()!!.copy(adminId = user.id)
                    flatRepository.update(newFlat)
                }
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.getMessage(applicationContext))
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
                _genericError.postValue(e.getMessage(applicationContext))
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
                _genericError.postValue(e.getMessage(applicationContext))
            }
        }
    }
}
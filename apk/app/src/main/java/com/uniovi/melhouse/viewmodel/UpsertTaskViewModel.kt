package com.uniovi.melhouse.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.Executor
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.TaskPriority
import com.uniovi.melhouse.data.model.TaskStatus
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.data.repository.taskuser.TaskUserRepository
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.exceptions.PersistenceLayerException
import com.uniovi.melhouse.viewmodel.state.TaskState
import com.uniovi.melhouse.preference.Prefs
import com.uniovi.melhouse.utils.validateLength
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class UpsertTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val prefs: Prefs,
    private val userRepository: UserRepository,
    private val taskUserRepository: TaskUserRepository
) : ViewModel() {
    private var task: Task? = null

    private var title: String? = null
    private var description: String? = null

    val startDate: LiveData<LocalDate?>
        get() = _startDate
    private val _startDate = MutableLiveData<LocalDate?>()
    val endDate: LiveData<LocalDate?>
        get() = _endDate
    private val _endDate = MutableLiveData<LocalDate?>()
    val status: LiveData<TaskStatus?>
        get() = _status
    private val _status = MutableLiveData<TaskStatus?>()
    val priority: LiveData<TaskPriority?>
        get() = _priority
    private val _priority = MutableLiveData<TaskPriority?>()

    // Lista para controlar botones seleccionados(true -> en amarillo)
    var asignees: MutableList<Boolean> = mutableListOf()

    // Para cerrar el viewmodel tras finalizar la corrutinas
    val close: LiveData<Boolean>
        get() = _close
    private val _close = MutableLiveData(false)

    //mapea índice del botón -> compañero de piso
    val map: LiveData<MutableList<User>>
        get() = _map
    private val _map = MutableLiveData<MutableList<User>>(mutableListOf())

    val genericError: LiveData<String?>
        get() = _genericError
    private val _genericError = MutableLiveData<String?>(null)

    val titleError: LiveData<String?>
        get() = _titleError
    private val _titleError = MutableLiveData<String?>(null)
    val endDateError: LiveData<String?>
        get() = _endDateError
    private val _endDateError = MutableLiveData<String?>(null)

    fun setTitle(title: String) {
        this.title = title
    }

    fun setDescription(description: String?) {
        this.description = description
    }

    fun setStartDate(startDate: LocalDate?) = _startDate.postValue(startDate)

    fun setEndDate(endDate: LocalDate?) = _endDate.postValue(endDate)

    fun setStatus(status: TaskStatus?) = _status.postValue(status)

    fun setPriority(status: TaskPriority?) = _priority.postValue(status)

    // TODO - Simplify viewModel and move task to assisted injection
    fun onViewCreated(task: Task?) {
        this.task = task
        putAsignees()

        if(task != null) {
            setTitle(task.name)
            setDescription(task.description)
            setStartDate(task.startDate)
            setEndDate(task.endDate)
            setStatus(task.status)
            setPriority(task.priority)
        }
    }

    fun upsertTask(context: Context) {
        var areErrors = false
        val title = title
        if(title.isNullOrEmpty()) {
            _titleError.value = context.getString(R.string.error_task_title_missing)
            areErrors = true
        }
        else if(!title.validateLength()) {
            _titleError.value = context.getString(R.string.error_task_title_length)
            areErrors = true
        }
        if(_endDate.value == null) {
            _endDateError.value = context.getString(R.string.error_task_end_date_missing)
            areErrors = true
        }

        if(areErrors) return

        if (task != null)
            updateTaskState()
        else
            saveTaskState()
    }

    private fun saveTaskState() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Executor.safeCall {
                    val task = generateTask()
                    taskRepository.insert(task)
                    taskUserRepository.insertAsignees(task.id, task.assignees.map { user -> user.id })
                    _close.postValue(true)
                }
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.message)
            }
        }
    }

    private fun updateTaskState() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Executor.safeCall {
                    taskRepository.update(generateTask())
                    taskUserRepository.deleteAllAsignees(task!!.id)
                    taskUserRepository.insertAsignees(
                        task!!.id,
                        task!!.assignees.map { user -> user.id })
                    _close.postValue(true)
                }
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.message)
            }
        }
    }

    private fun generateTask(): Task {
        return task?.copy(
            name = title.orEmpty(), // TODO - Change to assert !!
            description = description,
            status = _status.value,
            priority = _priority.value,
            startDate = _startDate.value,
            endDate = _endDate.value,
        ) ?:
        Task(
            name = title.orEmpty(), // TODO - Change to assert !!
            description = description,
            status = _status.value,
            priority = _priority.value,
            startDate = _startDate.value,
            endDate = _endDate.value,
            flatId = prefs.getFlatId()!!
        )
    }

    private fun putAsignees() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Executor.safeCall {
                    val flatId = prefs.getFlatId()

                    var roomates = emptyList<User>()
                    if(flatId != null)
                        roomates = userRepository.getRoommates(flatId)

                    asignees = MutableList(roomates.size) { false }
                    for ((index, roommate) in roomates.withIndex()) {
                        _map.value!!.add(index, roommate)
                        _map.postValue(_map.value!!.toMutableList())
                    }

                    if(task != null)
                        asignees = map.value!!
                            .map { asignee -> task!!.assignees.contains(asignee) }
                            .toMutableList()
                }
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.message)
            }
        }
    }

    fun changeAsignee(index: Int): Boolean{
        task = generateTask()
        task!!.assignees = getNewAsignees(index)

        asignees[index] = !asignees[index]

        return asignees[index]
    }

    private fun getNewAsignees(index: Int): MutableList<User> {
        val newAsignees = task!!.assignees.toMutableList()

        if (newAsignees.contains(_map.value!![index]))
            newAsignees.remove(_map.value!![index])
        else
            newAsignees.add(_map.value!![index])

        return newAsignees
    }
}
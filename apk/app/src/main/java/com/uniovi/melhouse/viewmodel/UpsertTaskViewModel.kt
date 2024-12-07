package com.uniovi.melhouse.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.Executor
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.TaskPriority
import com.uniovi.melhouse.data.model.TaskStatus
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.data.repository.taskuser.TaskUserRepository
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.viewmodel.state.TaskState
import com.uniovi.melhouse.preference.Prefs
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
    private var taskState: TaskState? = null

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

    fun onViewCreated(taskState: TaskState?) {
        this.taskState = taskState
        putAsignees()

        if(taskState != null) {
            val task = taskState.task
            setTitle(task.name)
            setDescription(task.description)
            setStartDate(task.startDate)
            setEndDate(task.endDate)
            setStatus(task.status)
            setPriority(task.priority)


        }
    }

    fun upsertTask() {
        if (taskState?.isInBD == true)
            updateTaskState()
        else
            saveTaskState()
    }

    private fun saveTaskState(){
        viewModelScope.launch {
            Executor.safeCall {
                val task = generateTask()
                taskRepository.insert(task)
                if(taskState != null)
                    taskUserRepository.insertAsignees(task.id, taskState!!.asignees.map { user -> user.id })
                _close.postValue(true)
            }
        }
    }

    private fun updateTaskState(){
        viewModelScope.launch {
            Executor.safeCall {
                taskRepository.update(generateTask())
                taskUserRepository.deleteAllAsignees(taskState!!.task.id)
                taskUserRepository.insertAsignees(
                    taskState!!.task.id,
                    taskState!!.asignees.map { user -> user.id })
                _close.postValue(true)
            }
        }
    }

    private fun generateTask(): Task {
        return taskState?.task?.copy(
            name = title.orEmpty(),
            description = description,
            status = _status.value,
            priority = _priority.value,
            startDate = _startDate.value,
            endDate = _endDate.value,
        ) ?:
        Task(
            name = title.orEmpty(),
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
            val roommates = userRepository.getRoommates()

            asignees = MutableList(roommates.size) { false }
            for ((index, roommate) in roommates.withIndex()) {
                _map.value!!.add(index, roommate)
                _map.postValue(_map.value!!.toMutableList())
            }

            if(taskState != null) {
                asignees = map.value!!.map { asignee -> taskState!!.asignees.contains(asignee) }.toMutableList()
            }
        }
    }

    fun changeAsignee(index: Int): Boolean{
        taskState = TaskState(generateTask(), asignees = getNewAsignees(index), taskState?.isInBD ?: false)

        asignees[index] = !asignees[index]

        return asignees[index]
    }

    private fun getNewAsignees(index: Int): MutableList<User> {
        val newAsignees = taskState?.asignees?.toMutableList().orEmpty().toMutableList()

        if (newAsignees.contains(_map.value!![index]))
            newAsignees.remove(_map.value!![index])
        else
            newAsignees.add(_map.value!![index])

        return newAsignees
    }
}
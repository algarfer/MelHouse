package com.uniovi.melhouse.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.SupabaseUserSessionFacade
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
    private val taskUserRepository: TaskUserRepository,
    private val userSessionFacade: SupabaseUserSessionFacade
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

    val asignees: LiveData<MutableList<Boolean>?>
        get() = _asignees
    private val _asignees = MutableLiveData<MutableList<Boolean>?>()

    private val _map = MutableLiveData<MutableList<User?>>(mutableListOf(null))

    val map: LiveData<MutableList<User?>> get() = _map


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
        if (taskState == null)
            saveTaskState()
        else
            updateTaskState()
    }

    private fun saveTaskState(){
        saveTask()
        saveAsignees()
    }

    private fun updateTaskState(){
        updateTask()
        updateAsignees()
    }

    private fun saveTask() {
        viewModelScope.launch(Dispatchers.IO) {
            Executor.safeCall {
                taskRepository.insert(generateTask())
            }
        }
    }

    private fun saveAsignees(){
        viewModelScope.launch(Dispatchers.IO) {
            taskUserRepository.insertAsignees(taskState!!.task.id, taskState!!.asignees.map { user -> user.id })
        }
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.update(generateTask())
        }
    }

    private fun updateAsignees(){
        viewModelScope.launch(Dispatchers.IO) {
            taskUserRepository.deleteAllAsignees(taskState!!.task.id)
            taskUserRepository.insertAsignees(taskState!!.task.id, taskState!!.asignees.map { user -> user.id })
        }
    }

    private fun generateTask(): Task {
        Log.d("generate", taskState.toString())
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
            Log.d("roomates", roommates.toString())
            _asignees.postValue(MutableList(roommates.size) { false })
            for ((index, roommate) in roommates.withIndex()) {
                Log.d("putAsignees", "añadeMap")
                _map.value!!.add(index, roommate)
                _map.postValue(_map.value!!.toMutableList())
            }
            Log.i("map", _map.value.toString())
            if(taskState != null) {
                val newAsignees = taskState?.asignees?.toMutableList().orEmpty().toMutableList()
                //changeAsignee(_map.value!!.indexOf(asignee))
                _asignees.postValue(map.value!!.map { asignee -> taskState!!.asignees.contains(asignee) }.toMutableList())
            }
        }
    }

    fun changeAsignee(index: Int): Boolean{
        Log.d("changeAsignee", "1")
        //_asignees.value!![index] = !_asignees.value!![index]
        val newAsignees = taskState?.asignees?.toMutableList().orEmpty().toMutableList()
        Log.d("changeAsignee", "2")
        if(newAsignees.contains(_map.value!![index]))
            newAsignees.remove(_map.value!![index])
        else
            newAsignees.add(_map.value!![index]!!)

        Log.d("changeAsignee", "3")
        taskState = TaskState(generateTask(), asignees = newAsignees)
        Log.d("changeAsignee", "4")
        asignees.value?.set(index, !asignees.value!![index])
        Log.d("changeAsignee", "5")

        return asignees.value?.get(index) ?: false
    }
}
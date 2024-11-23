package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.Executor
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.TaskPriority
import com.uniovi.melhouse.data.model.TaskStatus
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.preference.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class UpsertTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val prefs: Prefs
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

    fun onViewCreated(task: Task) {
        this.task = task
        setTitle(task.name)
        setDescription(task.description)
        setStartDate(task.startDate)
        setEndDate(task.endDate)
        setStatus(task.status)
        setPriority(task.priority)
    }

    fun upsertTask() {
        if (task == null)
            saveTask()
        else
            updateTask()
    }

    private fun saveTask() {
        viewModelScope.launch(Dispatchers.IO) {
            Executor.safeCall {
                taskRepository.insert(generateTask())
            }
        }
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            Executor.safeCall {
                taskRepository.update(generateTask())
            }
        }
    }

    private fun generateTask(): Task {
        return task?.copy(
            name = title!!,
            description = description,
            status = _status.value,
            priority = _priority.value,
            startDate = _startDate.value,
            endDate = _endDate.value,
        ) ?:
        Task(
            name = title!!,
            description = description,
            status = _status.value,
            priority = _priority.value,
            startDate = _startDate.value,
            endDate = _endDate.value,
            flatId = prefs.getFlatId()!!
        )
    }
}

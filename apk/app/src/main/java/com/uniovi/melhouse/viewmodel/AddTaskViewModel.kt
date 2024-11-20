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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

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

    fun saveTask() {
        val task = Task(
            name = title!!,
            description = description,
            status = _status.value,
            priority = _priority.value,
            startDate = _startDate.value,
            endDate = _endDate.value,
            flatId = UUID.randomUUID())

        viewModelScope.launch(Dispatchers.IO) {
            Executor.safeCall {
                taskRepository.insert(task)
            }
        }
    }
}

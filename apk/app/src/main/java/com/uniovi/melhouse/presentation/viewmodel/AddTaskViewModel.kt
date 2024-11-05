package com.uniovi.melhouse.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.database.SQLite
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.TaskPriority
import com.uniovi.melhouse.data.model.TaskStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class AddTaskViewModel : ViewModel() {

    private var title: String? = null
    private var description: String? = null
    val startDate = MutableLiveData<LocalDate?>()
    val endDate = MutableLiveData<LocalDate?>()
    val status = MutableLiveData<TaskStatus?>()
    val priority = MutableLiveData<TaskPriority?>()

    private val taskRepository = SQLite.getTaskRepository()

    fun setTitle(title: String) {
        this.title = title
    }

    fun setDescription(description: String?) {
        this.description = description
    }

    fun setStartDate(startDate: LocalDate?) = this.startDate.postValue(startDate)

    fun setEndDate(endDate: LocalDate?) = this.endDate.postValue(endDate)

    fun setStatus(status: TaskStatus?) = this.status.postValue(status)

    fun setPriority(status: TaskPriority?) = this.priority.postValue(status)

    // TODO - Move to usecase
    fun saveTask() {
        val task = Task(
            name = title!!,
            description = description,
            status = status.value,
            priority = priority.value,
            startDate = startDate.value,
            endDate = endDate.value,
            flatId = UUID.randomUUID())

        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.insert(task)
        }
    }
}

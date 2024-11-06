package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.di.qualifiers.SQLiteDatabaseQualifier
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    @SQLiteDatabaseQualifier private val tasksRepository: TaskRepository
) : ViewModel() {

    val tasks = MutableLiveData<Map<LocalDate?, List<Task>>>()
    val dailyTasks = MutableLiveData<Map<LocalDate?, List<Task>>>()

    fun onCreate() {
        updateTasks()
    }

    fun updateTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            tasks.postValue(tasksRepository
                .findAll()
                .filter { it.endDate != null }
                .groupBy { it.endDate }
            )
        }
    }

    fun updateDailyTasks(date: LocalDate?) {
        viewModelScope.launch(Dispatchers.IO) {
            dailyTasks.postValue(tasksRepository
                .findByDate(date)
                .filter { it.endDate != null }
                .groupBy { it.endDate }
            )
        }
    }
}
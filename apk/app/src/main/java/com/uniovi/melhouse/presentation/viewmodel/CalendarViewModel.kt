package com.uniovi.melhouse.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.database.SQLite
import com.uniovi.melhouse.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class CalendarViewModel : ViewModel() {

    val tasks = MutableLiveData<Map<LocalDate?, List<Task>>>()
    val dailyTasks = MutableLiveData<Map<LocalDate?, List<Task>>>()
    private val tasksRepository = SQLite.getTaskRepository()

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
        dailyTasks.postValue(tasksRepository
            .findByDate(date)
            .filter { it.endDate != null }
            .groupBy { it.endDate }
        )
    }
}
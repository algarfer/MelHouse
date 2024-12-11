package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.Executor
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.exceptions.PersistenceLayerException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val tasksRepository: TaskRepository
) : ViewModel() {

    val tasks: LiveData<Map<LocalDate?, List<Task>>>
        get() = _tasks
    private val _tasks = MutableLiveData<Map<LocalDate?, List<Task>>>()
    val dailyTasks: LiveData<Map<LocalDate?, List<Task>>>
        get() = _dailyTasks
    private val _dailyTasks = MutableLiveData<Map<LocalDate?, List<Task>>>()
    var date: LocalDate? = null
    val genericError: LiveData<String?>
        get() = _genericError
    private val _genericError = MutableLiveData<String?>(null)

    fun onCreate() {
        updateTasks()
    }

    fun updateTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            _tasks.postValue(
                try {
                    Executor.safeCall {
                        tasksRepository
                            .findAll()
                            .filter { it.endDate != null }
                            .groupBy { it.endDate }
                    }
                } catch (e: PersistenceLayerException) {
                    _genericError.postValue(e.message)
                    mapOf()
                }
            )
        }
    }

    fun updateDailyTasks(date: LocalDate?) {
        if (date == null) return
        viewModelScope.launch(Dispatchers.IO) {
            _dailyTasks.postValue(
                try {
                    Executor.safeCall {
                        tasksRepository
                            .findByDate(date)
                            .filter { it.endDate != null }
                            .groupBy { it.endDate }
                    }
                } catch (e: PersistenceLayerException) {
                    _genericError.postValue(e.message)
                    mapOf()
                }
            )
        }
    }
}
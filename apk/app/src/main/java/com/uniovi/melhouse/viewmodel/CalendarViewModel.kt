package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.repository.task.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    tasksRepository: TaskRepository
) : ViewModel() {

    private val _tasks = tasksRepository.findAllAsFlow()
        .map { tasks ->
            tasks
                .filter { it.endDate != null }
                .groupBy { it.endDate }
        }
        .catch { _genericError.postValue(it.message) }
        .shareIn(viewModelScope, started = SharingStarted.Lazily)
    private val _date = MutableSharedFlow<LocalDate>()
    val tasks = _tasks
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    val dailyTasks = _tasks.combine(_date) { tasks, date -> tasks[date] }
        .catch { _genericError.postValue(it.message) }
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    var date = _date
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    val genericError: LiveData<String?>
        get() = _genericError
    private val _genericError = MutableLiveData<String?>(null)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _date.emit(LocalDate.now())
        }
    }

    fun updateDay(date: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            _date.emit(date)
        }
    }
}
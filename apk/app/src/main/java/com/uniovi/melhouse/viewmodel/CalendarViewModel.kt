package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.presentation.fragments.CalendarFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
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
    val today = liveData {
        while (true) {
            emit(LocalDate.now())

            val now = LocalDateTime.now()
            val nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay()
            val delay = Duration.between(now, nextMidnight).toMillis()

            kotlinx.coroutines.delay(delay)
        }
    }
    private var previousSelectedDay: CalendarFragment.DayViewContainer? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _date.emit(LocalDate.now())
        }
    }

    fun selectDay(date: LocalDate, view: CalendarFragment.DayViewContainer? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            _date.emit(date)
        }

        if (view == null) return

        previousSelectedDay?.deselect()
        view.select()
        previousSelectedDay = view
    }
}
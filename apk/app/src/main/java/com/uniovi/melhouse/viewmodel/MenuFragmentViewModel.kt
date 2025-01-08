package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.repository.task.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MenuFragmentViewModel @Inject constructor(
    taskRepository: TaskRepository
) : AbstractViewModel() {

    private val today = flow {
        while (true) {
            emit(LocalDate.now())

            val now = LocalDateTime.now()
            val nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay()
            val delay = Duration.between(now, nextMidnight).toMillis()

            kotlinx.coroutines.delay(delay)
        }
    }
    private val tomorrow = flow {
        while (true) {
            emit(LocalDate.now().plusDays(1))

            val now = LocalDateTime.now()
            val nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay()
            val delay = Duration.between(now, nextMidnight).toMillis()

            kotlinx.coroutines.delay(delay)
        }
    }
    private val _tasks = taskRepository.findAllAsFlow()
        .catch { e -> _genericError.postValue(e.localizedMessage) }
        .shareIn(viewModelScope, started = SharingStarted.Lazily)

    val todayTasks = _tasks.combine(today) { tasks, date ->
        tasks.filter { it.endDate == date }
    }.asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    val tomorrowTasks = _tasks.combine(tomorrow) { tasks, date ->
        tasks.filter { it.endDate == date }
    }.asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
}
package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.utils.today
import com.uniovi.melhouse.utils.tomorrow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class MenuFragmentViewModel @Inject constructor(
    taskRepository: TaskRepository
) : ViewModel() {

    val todayTasks: LiveData<List<Task>> = taskRepository.findAssignedByDateAsFlow(
        today()
    )
        .catch { _genericError.postValue(it.message) }
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    val tomorrowTasks: LiveData<List<Task>> = taskRepository.findAssignedByDateAsFlow(
        tomorrow()
    )
        .catch { _genericError.postValue(it.message) }
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    val genericError: LiveData<String?>
        get() = _genericError
    private val _genericError = MutableLiveData<String?>(null)
}
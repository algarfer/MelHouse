package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.SupabaseUserSessionFacade
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.repository.task.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MenuFragmentViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    val todayTasks: LiveData<List<Task>>
        get() = _todayTasks
    private val _todayTasks = MutableLiveData<List<Task>>()

    val tomorrowTasks: LiveData<List<Task>>
        get() = _tomorrowTasks
    private val _tomorrowTasks = MutableLiveData<List<Task>>()

    fun loadTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            _todayTasks.postValue(taskRepository.findAssignedByDate(LocalDate.now()))
        }
        viewModelScope.launch(Dispatchers.IO) {
            _todayTasks.postValue(taskRepository.findAssignedByDate(LocalDate.now().plusDays(1)))
        }
    }

}
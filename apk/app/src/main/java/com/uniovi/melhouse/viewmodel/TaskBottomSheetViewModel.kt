package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.repository.task.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskBottomSheetViewModel  @Inject constructor(
    private val tasksRepository: TaskRepository
) : ViewModel() {

    val task = MutableLiveData<Task>()
    private var closeTaskBottomSheetDialog: (() -> Unit)? = null
    private var updateTasksViewHolder: (() -> Unit)? = null
    private var updateCalendarViewModel: (() -> Unit)? = null

    fun onCreateView(task: Task, updateCalendarViewModel: (() -> Unit),
                     updateTasksViewHolder: (() -> Unit),
                     closeTaskBottomSheetDialog: (() -> Unit)) {
        this.task.postValue(task)
        this.closeTaskBottomSheetDialog = closeTaskBottomSheetDialog
        this.updateTasksViewHolder = updateTasksViewHolder
        this.updateCalendarViewModel = updateCalendarViewModel
    }

    fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepository.delete(task.value!!.id)
        }

        closeTaskBottomSheetDialog!!()
        updateTasksViewHolder!!()
        updateCalendarViewModel!!()
    }
}
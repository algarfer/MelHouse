package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.Executor
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.factories.viewmodel.TaskBottomSheetViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = TaskBottomSheetViewModelFactory::class)
class TaskBottomSheetViewModel @AssistedInject constructor(
    private val tasksRepository: TaskRepository,
    @Assisted val task: Task,
    @Assisted("close") private var closeTaskBottomSheetDialog: (() -> Unit)? = null,
    @Assisted("updateTasks") private var updateTasksViewHolder: (() -> Unit)? = null,
    @Assisted("updateCalendar") private var updateCalendarViewModel: (() -> Unit)? = null
) : ViewModel() {

    fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            Executor.safeCall {
                tasksRepository.delete(task.id)
            }
        }

        closeTaskBottomSheetDialog!!()
        updateTasksViewHolder!!()
        updateCalendarViewModel!!()
    }
}
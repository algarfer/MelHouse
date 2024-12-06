package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.Executor
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.viewmodel.state.TaskState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskBottomSheetViewModel  @Inject constructor(
    private val tasksRepository: TaskRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val taskState = MutableLiveData<TaskState>()
    private var closeTaskBottomSheetDialog: (() -> Unit)? = null
    private var updateTasksViewHolder: (() -> Unit)? = null
    private var updateCalendarViewModel: (() -> Unit)? = null


    fun onCreateView(task: Task, updateCalendarViewModel: (() -> Unit),
                     updateTasksViewHolder: (() -> Unit),
                     closeTaskBottomSheetDialog: (() -> Unit)) {

        viewModelScope.launch(Dispatchers.IO) {
            val asignees = userRepository.findAsigneesById(task.id)

            this@TaskBottomSheetViewModel.taskState.postValue(TaskState(task, asignees, true))
        }

        this.closeTaskBottomSheetDialog = closeTaskBottomSheetDialog
        this.updateTasksViewHolder = updateTasksViewHolder
        this.updateCalendarViewModel = updateCalendarViewModel
    }

    fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            Executor.safeCall {
                tasksRepository.delete(taskState.value!!.task.id)
            }
        }

        closeTaskBottomSheetDialog!!()
        updateTasksViewHolder!!()
        updateCalendarViewModel!!()
    }
}
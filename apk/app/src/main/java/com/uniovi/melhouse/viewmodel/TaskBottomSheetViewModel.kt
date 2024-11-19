package com.uniovi.melhouse.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskBottomSheetViewModel  @Inject constructor(
    private val tasksRepository: TaskRepository,
) : ViewModel() {

    val task = MutableLiveData<Task>()
    private var closeTaskBottomSheetDialog: (() -> Unit)? = null
    private var updateTasksViewHolder: (() -> Unit)? = null
    private var updateCalendarViewModel: (() -> Unit)? = null
    val asignees: MutableLiveData<List<User>> = MutableLiveData(listOf())

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

    fun setAsignees(task: Task) {
        Log.d("test", "hit")
        viewModelScope.launch(Dispatchers.IO) {
            val asignees = tasksRepository.findAsigneesById(task.id)

            asignees.forEach{
                Log.d("test", it.toString())
            }

            this@TaskBottomSheetViewModel.asignees.postValue(asignees)
        }
    }
}
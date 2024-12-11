package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.Executor
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.exceptions.PersistenceLayerException
import com.uniovi.melhouse.viewmodel.state.TaskState
import com.uniovi.melhouse.factories.viewmodel.TaskBottomSheetViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel(
    assistedFactory = TaskBottomSheetViewModelFactory::class
)
class TaskBottomSheetViewModel @AssistedInject constructor(
    private val tasksRepository: TaskRepository,
    private val userRepository: UserRepository,
    @Assisted val task: Task,
    @Assisted("close") private var closeTaskBottomSheetDialog: (() -> Unit)? = null,
    @Assisted("updateTasks") private var updateTasksViewHolder: (() -> Unit)? = null,
    @Assisted("updateCalendar") private var updateCalendarViewModel: (() -> Unit)? = null
) : ViewModel() {

    val taskState: LiveData<TaskState>
        get() = _taskState
    private val _taskState = MutableLiveData<TaskState>()
    val genericError: LiveData<String?>
        get() = _genericError
    private val _genericError = MutableLiveData<String?>()

    fun onCreateView() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Executor.safeCall {
                    val asignees = userRepository.findAsigneesById(task.id)

                    this@TaskBottomSheetViewModel._taskState.postValue(TaskState(task, asignees, true))
                }
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.message)
            }
        }
    }

    fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Executor.safeCall {
                    tasksRepository.delete(taskState.value!!.task)
                }
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.message)
            }
        }

        closeTaskBottomSheetDialog!!()
        updateTasksViewHolder!!()
        updateCalendarViewModel!!()
    }
}
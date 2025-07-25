package com.uniovi.melhouse.viewmodel

import android.content.Context
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.Executor
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.data.repository.taskuser.TaskUserRepository
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.exceptions.PersistenceLayerException
import com.uniovi.melhouse.factories.viewmodel.TaskBottomSheetViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

@HiltViewModel(
    assistedFactory = TaskBottomSheetViewModelFactory::class
)
class TaskBottomSheetViewModel @AssistedInject constructor(
    private val tasksRepository: TaskRepository,
    userRepository: UserRepository,
    taskUserRepository: TaskUserRepository,
    @Assisted val taskId: UUID,
    @Assisted("close") private var closeTaskBottomSheetDialog: (() -> Unit) = { },
    @ApplicationContext private val applicationContext: Context
) : AbstractViewModel() {

    private val _task = tasksRepository.findByIdAsFlow(taskId)
        .catch { e -> _genericError.postValue(e.localizedMessage) }
    private val _taskUsers = taskUserRepository.findAllAsFlow()
        .map { tupleList -> tupleList.filter { tuple -> tuple.taskId == taskId } }
        .catch { e -> _genericError.postValue(e.localizedMessage) }
    private val _users = userRepository.findAllAsFlow()
        .catch { e -> _genericError.postValue(e.localizedMessage) }

    val task = combine(_task, _taskUsers, _users) { task, taskUsers, users ->
        val assignees = taskUsers
            .filter { tuple -> tuple.taskId == task.id }
            .map { tuple -> users.find { user -> user.id == tuple.userId }!! }
        task.assignees = assignees.toSet()
        task
    }.asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Executor.safeCall {
                    tasksRepository.delete(task.value!!)
                }
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.getMessage(applicationContext))
            }
        }

        closeTaskBottomSheetDialog()
    }
}
package com.uniovi.melhouse.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.Executor
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.TaskPriority
import com.uniovi.melhouse.data.model.TaskStatus
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.data.repository.taskuser.TaskUserRepository
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.exceptions.PersistenceLayerException
import com.uniovi.melhouse.factories.viewmodel.UpsertTaskViewModelFactory
import com.uniovi.melhouse.preference.Prefs
import com.uniovi.melhouse.utils.validateLength
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel(
    assistedFactory = UpsertTaskViewModelFactory::class
)
class UpsertTaskViewModel @AssistedInject constructor(
    private val taskRepository: TaskRepository,
    private val prefs: Prefs,
    private val userRepository: UserRepository,
    private val taskUserRepository: TaskUserRepository,
    @Assisted private val task: Task?,
    @ApplicationContext private val applicationContext: Context
) : ViewModel() {

    var title: String? = null
    var description: String? = null

    val startDate: LiveData<LocalDate?>
        get() = _startDate
    private val _startDate = MutableLiveData<LocalDate?>(task?.startDate)
    val endDate: LiveData<LocalDate?>
        get() = _endDate
    private val _endDate = MutableLiveData<LocalDate?>(task?.endDate)
    val status: LiveData<TaskStatus?>
        get() = _status
    private val _status = MutableLiveData<TaskStatus?>(task?.status)
    val priority: LiveData<TaskPriority?>
        get() = _priority
    private val _priority = MutableLiveData<TaskPriority?>(task?.priority)
    val assignees: LiveData<Set<User>>
        get() = _assignees
    private val _assignees = MutableLiveData(task?.assignees ?: setOf())
    val parters: LiveData<List<User>>
        get() = _partners
    private val _partners = MutableLiveData<List<User>>(emptyList())
    val creationSuccessful: LiveData<Boolean>
        get() = _creationSuccessful
    private val _creationSuccessful = MutableLiveData(false)
    val genericError: LiveData<String?>
        get() = _genericError
    private val _genericError = MutableLiveData<String?>(null)
    val titleError: LiveData<String?>
        get() = _titleError
    private val _titleError = MutableLiveData<String?>(null)
    val endDateError: LiveData<String?>
        get() = _endDateError
    private val _endDateError = MutableLiveData<String?>(null)

    fun setStartDate(startDate: LocalDate?) = _startDate.postValue(startDate)
    fun setEndDate(endDate: LocalDate?) = _endDate.postValue(endDate)
    fun setStatus(status: TaskStatus?) = _status.postValue(status)
    fun setPriority(status: TaskPriority?) = _priority.postValue(status)

    fun setAsignee(user: User) {
        val temp = _assignees.value!!.toMutableSet()
        if(_assignees.value!!.contains(user)) {
            temp.remove(user)
            _assignees.value = temp.toSet()
        } else {
            temp.add(user)
            _assignees.value = temp.toSet()
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getRoommates(prefs.getFlatId()!!).let {
                _partners.postValue(it)
            }
        }
    }

    fun triggerAsigneesObserver() {
        _assignees.postValue(_assignees.value)
    }

    fun upsertTask() {
        var areErrors = false
        val title = title
        if(title.isNullOrEmpty()) {
            _titleError.value = applicationContext.getString(R.string.error_task_title_missing)
            areErrors = true
        }
        else if(!title.validateLength()) {
            _titleError.value = applicationContext.getString(R.string.error_task_title_length)
            areErrors = true
        }
        if(_endDate.value == null) {
            _endDateError.value = applicationContext.getString(R.string.error_task_end_date_missing)
            areErrors = true
        }

        if(areErrors) return

        if (task == null)
            upsert {
                val task = generateTask()
                taskRepository.insert(task)
                taskUserRepository.insertAsignees(task.id, this.assignees.value!!.map { user -> user.id })
            }
        else
            upsert {
                val task = generateTask()
                taskRepository.update(generateTask())
                val oldAssignees = userRepository.findAsigneesById(task.id).toSet()
                val newAssignees = _assignees.value!!.toSet()

                val toDelete = oldAssignees - newAssignees
                val toInsert = newAssignees - oldAssignees

                taskUserRepository.deleteAssignees(task.id, toDelete.map { user -> user.id })
                taskUserRepository.insertAsignees(task.id, toInsert.map { user -> user.id })
            }
    }

    private fun upsert(action: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Executor.safeCall {
                    action()
                    _creationSuccessful.postValue(true)
                }
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.getMessage(applicationContext))
            }
        }
    }

    private fun generateTask(): Task {
        return task?.copy(
            name = title!!,
            description = description,
            status = _status.value,
            priority = _priority.value,
            startDate = _startDate.value,
            endDate = _endDate.value,
        ) ?:
        Task(
            name = title!!,
            description = description,
            status = _status.value,
            priority = _priority.value,
            startDate = _startDate.value,
            endDate = _endDate.value,
            flatId = prefs.getFlatId()!!
        )
    }
}
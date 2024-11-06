package com.uniovi.melhouse.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.di.qualifiers.SQLiteDatabaseQualifier
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskBottomSheetViewModel @Inject constructor(
    @SQLiteDatabaseQualifier private val taskRepository: TaskRepository,
    @SQLiteDatabaseQualifier private val userRepository: UserRepository
) : ViewModel() {

    val asignees: MutableLiveData<List<User>> = MutableLiveData(listOf())

    fun setAsignees(task: Task) {
        Log.d("test", "hit")
        viewModelScope.launch(Dispatchers.IO) {
            val asigneesId = taskRepository.findAsigneesById(task.id)

            val asignees = userRepository.findByIds(asigneesId)

            asignees.forEach{
                Log.d("test", it.toString())
            }

            this@TaskBottomSheetViewModel.asignees.postValue(asignees)
        }
    }
}
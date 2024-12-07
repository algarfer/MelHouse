package com.uniovi.melhouse.viewmodel.state

import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.User

data class TaskState(
    val task: Task,
    val asignees: List<User>,
    val isInBD: Boolean
)
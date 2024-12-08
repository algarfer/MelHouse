package com.uniovi.melhouse.viewmodel.state

import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.User
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class TaskState(
    val task: Task,
    val asignees: List<User>,
    val isInBD: Boolean
)

fun TaskState.toJson() = Json.encodeToString(this)

fun String.toTaskState() = Json.decodeFromString<TaskState>(this)

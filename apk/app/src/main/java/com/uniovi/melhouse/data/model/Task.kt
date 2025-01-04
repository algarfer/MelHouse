package com.uniovi.melhouse.data.model

import android.content.Context
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.serializers.LocalDateSerializer
import com.uniovi.melhouse.data.serializers.TaskPrioritySerializer
import com.uniovi.melhouse.data.serializers.TaskStatusSerializer
import com.uniovi.melhouse.data.serializers.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.util.UUID

@Serializable
data class Task(
    @Serializable(with = UUIDSerializer::class) val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String? = null,
    @Serializable(with = TaskStatusSerializer::class) val status: TaskStatus? = null,
    @Serializable(with = TaskPrioritySerializer::class) val priority: TaskPriority? = null,
    @Serializable(with = LocalDateSerializer::class) @SerialName("start_date") val startDate: LocalDate? = null,
    @Serializable(with = LocalDateSerializer::class) @SerialName("end_date") val endDate: LocalDate? = null,
    @Serializable(with = UUIDSerializer::class) @SerialName("flat_id") val flatId: UUID,
    @Transient var assignees: List<User> = emptyList()
)

enum class TaskStatus(val value: Int) : LocaleEnum {
    DONE(0) {
        override fun getString(context: Context) = context.getString(R.string.task_status_done)
    },
    INPROGRESS(1) {
        override fun getString(context: Context) = context.getString(R.string.task_status_in_progress)
    },
    PENDING(2) {
        override fun getString(context: Context) = context.getString(R.string.task_status_pending)
    },
    CANCELLED(3) {
        override fun getString(context: Context) = context.getString(R.string.task_status_cancelled)
    },
}

enum class TaskPriority(val value: Int) : LocaleEnum {
    LOW(0) {
        override fun getString(context: Context) = context.getString(R.string.task_priority_low)
    },
    MEDIUM(1) {
        override fun getString(context: Context) = context.getString(R.string.task_priority_medium)
    },
    HIGH(2) {
        override fun getString(context: Context) = context.getString(R.string.task_priority_high)
    },
}

fun Task.toJson() = Json.encodeToString(this)

fun String.toTask() = Json.decodeFromString<Task>(this)

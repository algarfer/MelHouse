package com.uniovi.melhouse.data.model

import android.content.ContentValues
import android.content.Context
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.serializers.LocalDateSerializer
import com.uniovi.melhouse.data.serializers.TaskPrioritySerializer
import com.uniovi.melhouse.data.serializers.TaskStatusSerializer
import com.uniovi.melhouse.data.serializers.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
data class Task(
    @Serializable(with = UUIDSerializer::class) val id: UUID = UUID.randomUUID(),
    var name: String,
    var description: String?,
    @Serializable(with = TaskStatusSerializer::class) var status: TaskStatus?,
    @Serializable(with = TaskPrioritySerializer::class) var priority: TaskPriority?,
    @Serializable(with = LocalDateSerializer::class) @SerialName("start_date") var startDate: LocalDate?,
    @Serializable(with = LocalDateSerializer::class) @SerialName("end_date") var endDate: LocalDate?,
    @Serializable(with = UUIDSerializer::class) @SerialName("flat_id") var flatId: UUID,
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

fun Task.toContentValues(): ContentValues {
    return ContentValues().apply {
        put("id", id.toString())
        put("name", name)
        put("description", description)
        put("status", status?.value)
        put("priority", priority?.value)
        put("start_date", startDate?.toString())
        put("end_date", endDate?.toString())
        put("flat_id", flatId.toString())
    }
}

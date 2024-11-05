package com.uniovi.melhouse.data.model

import android.content.ContentValues
import android.content.Context
import com.uniovi.melhouse.R
import java.time.LocalDate
import java.util.UUID

data class Task(
    val id: UUID = UUID.randomUUID(),
    var name: String,
    var description: String?,
    var status: TaskStatus?,
    var priority: TaskPriority?,
    var startDate: LocalDate?,
    var endDate: LocalDate?,
    var flatId: UUID,
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

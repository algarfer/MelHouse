package com.uniovi.melhouse.data.model

import android.content.ContentValues
import java.time.LocalDate
import java.util.UUID

data class Task(
    val id: UUID,
    var name: String,
    var description: String?,
    var status: TaskStatus?,
    var priority: TaskPriority?,
    var startDate: LocalDate?,
    var endDate: LocalDate?,
    var flatId: UUID,
)

enum class TaskStatus(val value: Int) {
    DONE(0),
    INPROGRESS(1),
    PENDING(2),
    CANCELLED(3),
}

enum class TaskPriority(val value: Int) {
    LOW(0),
    MEDIUM(1),
    HIGH(2),
}

fun Task.toContentValues(): ContentValues {
    return ContentValues().apply {
        put("id", id.toString())
        put("name", name)
        put("description", name)
        put("status", status?.value)
        put("priority", priority?.value)
        put("start_date", startDate?.toString())
        put("end_date", endDate?.toString())
        put("flat_id", flatId.toString())
    }
}

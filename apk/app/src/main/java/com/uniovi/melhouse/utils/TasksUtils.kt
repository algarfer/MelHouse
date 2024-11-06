package com.uniovi.melhouse.utils

import android.content.Context
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.TaskPriority
import com.uniovi.melhouse.data.model.TaskStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun TaskStatus.getColor(context: Context): Int {
    return when (this) {
        TaskStatus.DONE -> context.getColorCompat(R.color.task_status_done)
        TaskStatus.INPROGRESS -> context.getColorCompat(R.color.task_status_in_progress)
        TaskStatus.PENDING -> context.getColorCompat(R.color.task_status_pending)
        TaskStatus.CANCELLED -> context.getColorCompat(R.color.task_status_cancelled)
    }
}

fun TaskPriority.getColor(context: Context): Int {
    return when (this) {
        TaskPriority.LOW -> context.getColorCompat(R.color.task_priority_low)
        TaskPriority.MEDIUM -> context.getColorCompat(R.color.task_priority_medium)
        TaskPriority.HIGH -> context.getColorCompat(R.color.task_priority_high)
    }
}

fun Task.getDatesString(): String {
    if (endDate == null) return ""
    if (startDate == null) return endDate.toString()
    return "$startDate\n$endDate"
}

val taskDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE'\n'dd MMM'\n'yyyy")

fun today(): LocalDate {
    return LocalDate.now()
}

fun maxDate(): LocalDate {
    return LocalDate.now().plusYears(5)
}
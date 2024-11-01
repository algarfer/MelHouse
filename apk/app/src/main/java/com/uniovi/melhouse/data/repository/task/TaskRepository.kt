package com.uniovi.melhouse.data.repository.task

import android.database.Cursor
import androidx.core.database.getStringOrNull
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.TaskPriority
import com.uniovi.melhouse.data.model.TaskStatus
import com.uniovi.melhouse.data.repository.Repository
import java.time.LocalDate
import java.util.UUID

interface TaskRepository : Repository<Task> {
    fun findByDate(date: LocalDate?): List<Task>
}

class TaskAssembler {
    companion object {
        fun toTask(cursor : Cursor) : Task? {
            val list = toList(cursor)
            if(list.isEmpty()) return null
            return list.first()
        }

        fun toList(cursor : Cursor) : List<Task> {
            val tasks = mutableListOf<Task>()
            while (cursor.moveToNext()) {
                val idIndex = cursor.getColumnIndex("id")
                val nameIndex = cursor.getColumnIndex("name")
                val descriptionIndex = cursor.getColumnIndex("description")
                val statusIndex = cursor.getColumnIndex("status")
                val priorityIndex = cursor.getColumnIndex("priority")
                val startDateIndex = cursor.getColumnIndex("start_date")
                val endDateIndex = cursor.getColumnIndex("end_date")
                val flatIdIndex = cursor.getColumnIndex("flat_id")

                val id = cursor.getString(idIndex)
                val name = cursor.getString(nameIndex)
                val description = cursor.getStringOrNull(descriptionIndex)
                val status = cursor.getStringOrNull(statusIndex)
                val priority = cursor.getStringOrNull(priorityIndex)
                val startDate = cursor.getStringOrNull(startDateIndex)
                val endDate = cursor.getStringOrNull(endDateIndex)
                val flatId = cursor.getString(flatIdIndex)

                tasks.add(
                    Task(
                        UUID.fromString(id),
                        name,
                        description,
                        status?.let { TaskStatus.entries[it.toInt()] },
                        priority?.let { TaskPriority.entries[it.toInt()] },
                        startDate?.let { LocalDate.parse(it) },
                        endDate?.let { LocalDate.parse(it) },
                        UUID.fromString(flatId)
                    )
                )
            }
            return tasks.toList()
        }
    }
}
package com.uniovi.melhouse.data.repository.task

import android.database.Cursor
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.repository.Repository
import java.util.UUID

interface TaskRepository : Repository<Task>

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
                val id = cursor.getString(idIndex)

                tasks.add(Task(UUID.fromString(id)))
            }
            return tasks
        }
    }
}
package com.uniovi.melhouse.data.repository.task

import android.database.sqlite.SQLiteDatabase
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.toContentValues
import java.time.LocalDate
import java.util.UUID

class TaskRepositorySQLite(
    private val db: SQLiteDatabase
) : TaskRepository {

    private val TABLE_NAME = "tasks"

    override suspend fun insert(entity: Task) {
        db.insertOrThrow(TABLE_NAME, null, entity.toContentValues())
    }

    override suspend fun update(entity: Task) {
        db.update(TABLE_NAME, entity.toContentValues(), "id = ?", arrayOf(entity.id.toString()))
    }

    override suspend fun delete(entity: Task) {
        db.delete(TABLE_NAME, "id = ?", arrayOf(entity.id.toString()))
    }

    override suspend fun findById(id: Any): Task? {
        db.rawQuery("SELECT * FROM $TABLE_NAME WHERE id = ?", arrayOf(id.toString())).use { cursor ->
            return TaskAssembler.toTask(cursor)
        }
    }

    override suspend fun findAll(): List<Task> {
        db.rawQuery("SELECT * FROM $TABLE_NAME", null).use { cursor ->
            return TaskAssembler.toList(cursor)
        }
    }

    override suspend fun findByDate(date: LocalDate?): List<Task> {
        if(date == null) return emptyList()
        db.rawQuery("SELECT * FROM $TABLE_NAME WHERE end_date = '$date'", null).use { cursor ->
            return TaskAssembler.toList(cursor)
        }
    }

    override suspend fun findAsigneesById(taskId: UUID): List<UUID> {
        db.rawQuery("SELECT user_id FROM tasks_users WHERE task_id = ?", arrayOf(taskId.toString())).use {
            val asignees = mutableListOf<UUID>()
            while (it.moveToNext()) {
                val userIdIndex = it.getColumnIndex("user_id")
                val userId = it.getString(userIdIndex)
                asignees.add(UUID.fromString(userId))
            }
            return asignees.toList()
        }
    }
}
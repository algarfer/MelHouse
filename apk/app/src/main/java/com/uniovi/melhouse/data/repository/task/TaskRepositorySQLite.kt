package com.uniovi.melhouse.data.repository.task

import android.database.sqlite.SQLiteDatabase
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.toContentValues
import java.time.LocalDate

class TaskRepositorySQLite(
    private val db: SQLiteDatabase
) : TaskRepository {

    private val TABLE_NAME = "tasks"

    override fun insert(entity: Task) {
        db.insertOrThrow(TABLE_NAME, null, entity.toContentValues())
    }

    override fun update(entity: Task) {
        db.update(TABLE_NAME, entity.toContentValues(), "id = ?", arrayOf(entity.id.toString()))
    }

    override fun delete(entity: Task) {
        db.delete(TABLE_NAME, "id = ?", arrayOf(entity.id.toString()))
    }

    override fun findById(id: Any): Task? {
        db.rawQuery("SELECT * FROM $TABLE_NAME WHERE id = ?", arrayOf(id.toString())).use { cursor ->
            return TaskAssembler.toTask(cursor)
        }
    }

    override fun findAll(): List<Task> {
        db.rawQuery("SELECT * FROM $TABLE_NAME", null).use { cursor ->
            return TaskAssembler.toList(cursor)
        }
    }

    override fun findByDate(date: LocalDate?): List<Task> {
        if(date == null) return emptyList()
        db.rawQuery("SELECT * FROM $TABLE_NAME WHERE end_date = '$date'", null).use { cursor ->
            return TaskAssembler.toList(cursor)
        }
    }
}
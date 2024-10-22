package com.uniovi.melhouse.data.repository.user

import android.database.sqlite.SQLiteDatabase
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.model.toContentValues

class UserRepositorySQLite(
    private val db: SQLiteDatabase
) : UserRepository {

    private val TABLE_NAME = "user"

    override fun insert(entity: User) {
        db.insertOrThrow(TABLE_NAME, null, entity.toContentValues())
    }

    override fun update(entity: User) {
        db.update(TABLE_NAME, entity.toContentValues(), "id = ?", arrayOf(entity.id.toString()))
    }

    override fun delete(entity: User) {
        db.delete(TABLE_NAME, "id = ?", arrayOf(entity.id.toString()))
    }

    override fun findById(id: Any): User? {
        db.rawQuery("SELECT * FROM $TABLE_NAME WHERE id = ?", arrayOf(id.toString())).use { cursor ->
            return UserAssembler.toUser(cursor)
        }
    }

    override fun findAll(): List<User> {
        db.rawQuery("SELECT * FROM $TABLE_NAME", null).use { cursor ->
            return UserAssembler.toList(cursor)
        }
    }
}
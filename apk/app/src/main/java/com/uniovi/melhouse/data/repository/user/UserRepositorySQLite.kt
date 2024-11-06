package com.uniovi.melhouse.data.repository.user

import android.database.sqlite.SQLiteDatabase
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.model.toContentValues
import java.util.UUID

class UserRepositorySQLite(
    private val db: SQLiteDatabase
) : UserRepository {

    private val TABLE_NAME = "users"

    override suspend fun insert(entity: User) {
        db.insertOrThrow(TABLE_NAME, null, entity.toContentValues())
    }

    override suspend fun update(entity: User) {
        db.update(TABLE_NAME, entity.toContentValues(), "id = ?", arrayOf(entity.id.toString()))
    }

    override suspend fun delete(entity: User) {
        db.delete(TABLE_NAME, "id = ?", arrayOf(entity.id.toString()))
    }

    override suspend fun findById(id: Any): User? {
        db.rawQuery("SELECT * FROM $TABLE_NAME WHERE id = ?", arrayOf(id.toString())).use { cursor ->
            return UserAssembler.toUser(cursor)
        }
    }

    override suspend fun findAll(): List<User> {
        db.rawQuery("SELECT * FROM $TABLE_NAME", null).use { cursor ->
            return UserAssembler.toList(cursor)
        }
    }

    override suspend fun findByEmail(email: String): User? {
        db.rawQuery("SELECT * FROM $TABLE_NAME WHERE email = ?", arrayOf(email)).use { cursor ->
            return UserAssembler.toUser(cursor)
        }
    }

    override suspend fun findByIds(ids: List<UUID>): List<User> {
        db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE id IN (${ids.joinToString { "?" }})",
            ids.map { it.toString() }.toTypedArray()
        ).use { cursor ->
            return UserAssembler.toList(cursor)
        }
    }
}
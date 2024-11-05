package com.uniovi.melhouse.data.repository.flat

import android.database.sqlite.SQLiteDatabase
import com.uniovi.melhouse.data.model.Flat
import com.uniovi.melhouse.data.model.toContentValues

class FlatRepositorySQLite(
    private val db: SQLiteDatabase
) : FlatRepository {

    private val TABLE_NAME = "flats"

    override suspend fun insert(entity: Flat) {
        db.insertOrThrow(TABLE_NAME, null, entity.toContentValues())
    }

    override suspend fun update(entity: Flat) {
        db.update(TABLE_NAME, entity.toContentValues(), "id = ?", arrayOf(entity.id.toString()))
    }

    override suspend fun delete(entity: Flat) {
        db.delete(TABLE_NAME, "id = ?", arrayOf(entity.id.toString()))
    }

    override suspend fun findById(id: Any): Flat? {
        db.rawQuery("SELECT * FROM $TABLE_NAME WHERE id = ?", arrayOf(id.toString())).use { cursor ->
            return FlatAssembler.toFlat(cursor)
        }
    }

    override suspend fun findAll(): List<Flat> {
        db.rawQuery("SELECT * FROM $TABLE_NAME", null).use { cursor ->
            return FlatAssembler.toList(cursor)
        }
    }
}
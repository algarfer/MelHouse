package com.uniovi.melhouse.data.repository.flat

import android.database.sqlite.SQLiteDatabase
import com.uniovi.melhouse.data.model.Flat
import com.uniovi.melhouse.data.model.toContentValues

class FlatRepositorySQLite(
    private val db: SQLiteDatabase
) : FlatRepository {

    private val TABLE_NAME = "flat"

    override fun insert(entity: Flat) {
        db.insertOrThrow(TABLE_NAME, null, entity.toContentValues())
    }

    override fun update(entity: Flat) {
        db.update(TABLE_NAME, entity.toContentValues(), "id = ?", arrayOf(entity.id.toString()))
    }

    override fun delete(entity: Flat) {
        db.delete(TABLE_NAME, "id = ?", arrayOf(entity.id.toString()))
    }

    override fun findById(id: Any): Flat? {
        db.rawQuery("SELECT * FROM $TABLE_NAME WHERE id = ?", arrayOf(id.toString())).use { cursor ->
            return FlatAssembler.toFlat(cursor)
        }
    }

    override fun findAll(): List<Flat> {
        db.rawQuery("SELECT * FROM $TABLE_NAME", null).use { cursor ->
            return FlatAssembler.toList(cursor)
        }
    }
}
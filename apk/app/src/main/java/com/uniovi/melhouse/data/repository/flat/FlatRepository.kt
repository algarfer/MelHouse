package com.uniovi.melhouse.data.repository.flat

import android.database.Cursor
import com.uniovi.melhouse.data.model.Flat
import com.uniovi.melhouse.data.repository.Repository
import java.util.UUID

interface FlatRepository : Repository<Flat>

class FlatAssembler {
    companion object {
        fun toFlat(cursor: Cursor): Flat? {
            val list = toList(cursor)
            if (list.isEmpty()) return null
            return list.first()
        }

        fun toList(cursor: Cursor): List<Flat> {
            val flats = mutableListOf<Flat>()
            while (cursor.moveToNext()) {
                val idIndex = cursor.getColumnIndex("id")
                val id = cursor.getString(idIndex)

                flats.add(Flat(UUID.fromString(id)))
            }
            return flats
        }
    }
}
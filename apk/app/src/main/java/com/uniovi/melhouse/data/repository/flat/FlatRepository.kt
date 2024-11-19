package com.uniovi.melhouse.data.repository.flat

import android.database.Cursor
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.uniovi.melhouse.data.model.Flat
import com.uniovi.melhouse.data.repository.Repository
import java.util.UUID

interface FlatRepository : Repository<Flat> {
    suspend fun joinFlat(invitationCode: String)
    suspend fun createFlat(flat: Flat)
}

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
                val nameIndex = cursor.getColumnIndex("name")
                val addressIndex = cursor.getColumnIndex("address")
                val floorIndex = cursor.getColumnIndex("floor")
                val doorIndex = cursor.getColumnIndex("door")
                val stairIndex = cursor.getColumnIndex("stair")
                val invitationCodeIndex = cursor.getColumnIndex("invitation_code")
                val adminIdIndex = cursor.getColumnIndex("admin_id")

                val id = cursor.getString(idIndex)
                val name = cursor.getString(nameIndex)
                val address = cursor.getString(addressIndex)
                val floor = cursor.getIntOrNull(floorIndex)
                val door = cursor.getStringOrNull(doorIndex)
                val stair = cursor.getStringOrNull(stairIndex)
                val invitationCode = cursor.getString(invitationCodeIndex)
                val adminId = cursor.getString(adminIdIndex)

                flats.add(
                    Flat(
                        UUID.fromString(id),
                        name,
                        address,
                        floor,
                        door,
                        stair,
                        invitationCode,
                        UUID.fromString(adminId)
                    )
                )
            }
            return flats.toList()
        }
    }
}
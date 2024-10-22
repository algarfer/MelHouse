package com.uniovi.melhouse.data.repository.user

import android.database.Cursor
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.repository.Repository
import java.util.UUID

interface UserRepository : Repository<User>

class UserAssembler {
    companion object {
        fun toUser(cursor: Cursor): User? {
            val list = toList(cursor)
            if(list.isEmpty()) return null
            return list.first()
        }

        fun toList(cursor: Cursor): List<User> {
            val users = mutableListOf<User>()
            while (cursor.moveToNext()) {
                val idIndex = cursor.getColumnIndex("id")
                val id = cursor.getString(idIndex)

                users.add(User(UUID.fromString(id)))
            }
            return users
        }
    }
}
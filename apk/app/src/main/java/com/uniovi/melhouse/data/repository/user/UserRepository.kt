package com.uniovi.melhouse.data.repository.user

import android.database.Cursor
import androidx.core.database.getStringOrNull
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.repository.Repository
import java.util.UUID

interface UserRepository : Repository<User> {
    suspend fun findByEmail(email: String): User?
    suspend fun findByIds(ids: List<UUID>): List<User>
}

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
                val nameIndex = cursor.getColumnIndex("name")
                val emailIndex = cursor.getColumnIndex("email")
                val flatIdIndex = cursor.getColumnIndex("flat_id")

                val id = cursor.getString(idIndex)
                val name = cursor.getString(nameIndex)
                val email = cursor.getString(emailIndex)
                val flatId = cursor.getStringOrNull(flatIdIndex)

                users.add(
                    User(
                        UUID.fromString(id),
                        name,
                        email,
                        flatId?.let { UUID.fromString(it) }
                    )
                )
            }
            return users.toList()
        }
    }
}
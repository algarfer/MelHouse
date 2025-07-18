package com.uniovi.melhouse.data.repository.user

import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.repository.Repository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface UserRepository : Repository<User> {
    suspend fun findAsigneesById(taskId: UUID): List<User>
    suspend fun findByEmail(email: String): User?
    suspend fun findByIds(ids: List<UUID>): List<User>
    suspend fun getRoommates(flatId: UUID): List<User>
    fun getRoommatesAsFlow(flatId: UUID): Flow<List<User>>
    suspend fun deleteUserForever()
}

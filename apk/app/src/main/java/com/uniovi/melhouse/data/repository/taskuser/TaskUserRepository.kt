package com.uniovi.melhouse.data.repository.taskuser

import com.uniovi.melhouse.data.model.TaskUser
import com.uniovi.melhouse.data.repository.Repository
import java.util.UUID

interface TaskUserRepository : Repository<TaskUser> {
    suspend fun insertAsignees(taskId: UUID, userIds: List<UUID>)
    suspend fun deleteAssignees(taskId: UUID, userIds: List<UUID>)
    suspend fun deleteAllAsignees(id: UUID)
}
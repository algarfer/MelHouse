package com.uniovi.melhouse.data.repository.taskuser

import com.uniovi.melhouse.data.model.TaskUser
import kotlinx.coroutines.flow.Flow
import java.util.UUID

abstract class TaskUserRepositoryAbstract : TaskUserRepository {

    @Deprecated(
        message = "This method is not supported",
        level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("insertAssignees(taskId: UUID, userIds: List<UUID>)")
    )
    final override suspend fun insert(entity: TaskUser) {
        throw UnsupportedOperationException("Method not supported")
    }

    @Deprecated(
        message = "This method is not supported",
        level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("insertAssignees and deleteAssignees")
    )
    final override suspend fun update(entity: TaskUser) {
        throw UnsupportedOperationException("Method not supported")
    }

    @Deprecated(
        message = "This method is not supported",
        level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("deleteAssignees(taskId: UUID, userIds: List<UUID>)")
    )
    final override suspend fun delete(entity: TaskUser) {
        throw UnsupportedOperationException("Method not supported")
    }

    @Deprecated(
        message = "This method is not supported",
        level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("findAllAsFlow()")
    )
    final override suspend fun findById(id: UUID): TaskUser? {
        throw UnsupportedOperationException("Method not supported")
    }

    @Deprecated(
        message = "This method is not supported",
        level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("findAllAsFlow()")
    )
    final override suspend fun findAll(): List<TaskUser> {
        throw UnsupportedOperationException("Method not supported")
    }

    @Deprecated(
        message = "This method is not supported",
        level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("findAllAsFlow()")
    )
    final override fun findByIdAsFlow(id: UUID): Flow<TaskUser> {
        throw UnsupportedOperationException("Method not supported")
    }
}
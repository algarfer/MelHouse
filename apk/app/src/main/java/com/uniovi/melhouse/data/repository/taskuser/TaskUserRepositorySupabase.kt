package com.uniovi.melhouse.data.repository.taskuser

import com.uniovi.melhouse.data.model.TaskUser
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import java.util.UUID
import javax.inject.Inject

class TaskUserRepositorySupabase @Inject constructor(
    private val supabaseClient: SupabaseClient
) : TaskUserRepository {

    companion object {
        private const val TABLE_NAME = "tasks_users"
    }

    override suspend fun insertAsignees(taskId: UUID, userIds: List<UUID>) {
        supabaseClient
            .from(TABLE_NAME)
            .insert(userIds.map { uuid -> TaskUser(uuid, taskId)})
    }

    override suspend fun deleteAllAsignees(id: UUID) {
        supabaseClient
            .from(TABLE_NAME)
            .delete {
                filter {
                    eq("task_id", id)
                }
            }
    }

    override suspend fun insert(entity: TaskUser) {
        TODO("Not yet implemented")
    }

    override suspend fun update(entity: TaskUser) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(entity: TaskUser) {
        TODO("Not yet implemented")
    }

    override suspend fun findById(id: UUID): TaskUser? {
        TODO("Not yet implemented")
    }

    override suspend fun findAll(): List<TaskUser> {
        TODO("Not yet implemented")
    }
}
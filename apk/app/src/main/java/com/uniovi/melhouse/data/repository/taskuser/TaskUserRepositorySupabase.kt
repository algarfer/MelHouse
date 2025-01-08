package com.uniovi.melhouse.data.repository.taskuser

import com.uniovi.melhouse.data.model.TaskUser
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

@OptIn(SupabaseExperimental::class)
class TaskUserRepositorySupabase @Inject constructor(
    private val supabaseClient: SupabaseClient
) : TaskUserRepositoryAbstract() {

    companion object {
        private const val TABLE_NAME = "tasks_users"
    }

    override suspend fun insertAsignees(taskId: UUID, userIds: List<UUID>) {
        supabaseClient
            .from(TABLE_NAME)
            .insert(userIds.map { uuid -> TaskUser(uuid, taskId)})
    }

    override suspend fun deleteAssignees(taskId: UUID, userIds: List<UUID>) {
        supabaseClient
            .from(TABLE_NAME)
            .delete {
                filter {
                    and {
                        isIn("user_id", userIds)
                        eq("task_id", taskId)
                    }
                }
            }
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

    override fun findAllAsFlow(): Flow<List<TaskUser>> {
        return supabaseClient
            .from(TABLE_NAME)
            .selectAsFlow(listOf(TaskUser::taskId, TaskUser::userId))
    }
}
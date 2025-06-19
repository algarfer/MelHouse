package com.uniovi.melhouse.data.repository.user

import com.uniovi.melhouse.data.model.TaskUser
import com.uniovi.melhouse.data.model.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

@OptIn(SupabaseExperimental::class)
class UserRepositorySupabase @Inject constructor(
    private val supabaseClient: SupabaseClient
) : UserRepository {

    companion object {
        private const val TABLE_NAME = "users"
    }

    override suspend fun findByEmail(email: String): User? {
        return supabaseClient
            .from(TABLE_NAME)
            .select {
                filter {
                    eq("email", email)
                }
            }.decodeSingleOrNull()
    }

    override suspend fun findByIds(ids: List<UUID>): List<User> {
        return supabaseClient
            .from(TABLE_NAME)
            .select {
                filter {
                    contains("id", ids)
                }
            }.decodeList()
    }

    override suspend fun getRoommates(flatId: UUID): List<User> {
        return supabaseClient
            .from(TABLE_NAME)
            .select {
                filter {
                    eq("flat_id", flatId)
                }
            }
            .decodeList()
    }

    // FIXME - This only updates the user list only when the logged user is an admin
    override fun getRoommatesAsFlow(flatId: UUID): Flow<List<User>> {
        return supabaseClient
            .from(TABLE_NAME)
            .selectAsFlow(User::id)
            .map { it.filter { user -> user.flatId == flatId } }
    }

    override suspend fun deleteUserForever() {
        supabaseClient
            .postgrest
            .rpc("delete_user_forever")
    }

    override suspend fun insert(entity: User) {
        supabaseClient
            .from(TABLE_NAME)
            .insert(entity)
    }

    override suspend fun update(entity: User) {
        supabaseClient
            .from(TABLE_NAME)
            .update(entity) {
                filter {
                    eq("id", entity.id)
                }
            }
    }

    override suspend fun delete(entity: User) {
        supabaseClient
            .from(TABLE_NAME)
            .delete {
                filter {
                    eq("id", entity.id)
                }
            }
    }

    override suspend fun findById(id: UUID): User? {
        return supabaseClient
            .from(TABLE_NAME)
            .select {
                filter {
                    eq("id", id)
                }
            }.decodeSingleOrNull()
    }

    override suspend fun findAll(): List<User> {
        return supabaseClient
            .from(TABLE_NAME)
            .select()
            .decodeList()
    }

    override fun findAllAsFlow(): Flow<List<User>> {
        return supabaseClient
            .from(TABLE_NAME)
            .selectAsFlow(User::id)
    }

    override fun findByIdAsFlow(id: UUID): Flow<User> {
        return supabaseClient
            .from(TABLE_NAME)
            .selectSingleValueAsFlow(User::id) {
                User::id eq id
            }
    }

    override suspend fun findAsigneesById(taskId: UUID): List<User> {
        val tasksUsers = supabaseClient
            .from("tasks_users")
            .select {
                filter {
                    eq("task_id", taskId)
                }
            }.decodeList<TaskUser>()
        val ids = tasksUsers.map { tu -> tu.userId }
        return supabaseClient
            .from(TABLE_NAME)
            .select {
                filter {
                    isIn("id", ids)
                }
            }.decodeList()
    }
}
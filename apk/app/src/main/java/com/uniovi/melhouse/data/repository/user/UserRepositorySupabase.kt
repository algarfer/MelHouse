package com.uniovi.melhouse.data.repository.user

import com.uniovi.melhouse.data.model.TaskUser
import com.uniovi.melhouse.data.model.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import java.util.UUID
import javax.inject.Inject

class UserRepositorySupabase @Inject constructor(
    private val supabaseClient: SupabaseClient
): UserRepository {

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

    override suspend fun findAsigneesById(taskId: UUID): List<User> {
        val tasksUsers = supabaseClient
            .from("tasks_users")
            .select() {
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
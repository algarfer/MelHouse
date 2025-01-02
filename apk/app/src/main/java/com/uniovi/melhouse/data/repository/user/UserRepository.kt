package com.uniovi.melhouse.data.repository.user

import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.repository.Repository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import java.util.UUID

interface UserRepository : Repository<User> {
    suspend fun findAsigneesById(taskId: UUID) : List<User>
    suspend fun findByEmail(email: String): User?
    suspend fun findByIds(ids: List<UUID>): List<User>
    suspend fun getRoommates(flatId: UUID): List<User>
    fun getRoommatesAsFlow(flatId: UUID): Flow<List<User>>
}

// TODO - Move this to the backend in some way
suspend fun User.loadTasks(supabaseClient: SupabaseClient) {
    val data = supabaseClient
        .from("tasks_users")
        .select(columns = Columns.list("task_id")) {
            filter {
                eq("user_id", id)
            }
        }.data

    val tasksIds = Json
        .parseToJsonElement(data)
        .jsonArray
        .mapNotNull {
            (it as? JsonObject)?.get("task_id")?.jsonPrimitive?.content
        }

    tasks = supabaseClient
        .from("tasks")
        .select {
            filter {
                isIn("id", tasksIds)
            }
        }.decodeList()
}
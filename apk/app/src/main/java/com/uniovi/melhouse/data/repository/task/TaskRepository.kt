package com.uniovi.melhouse.data.repository.task

import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.repository.Repository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import java.time.LocalDate
import java.util.UUID

interface TaskRepository : Repository<Task> {
    suspend fun findByDate(date: LocalDate?): List<Task>
    suspend fun findAssignedByDate(date: LocalDate?): List<Task>
    suspend fun findByFlatId(flatId: UUID): List<Task>
    fun findByFlatIdAsFlow(flatId: UUID): Flow<List<Task>>
    fun findAssignedByDateAsFlow(date: LocalDate?): Flow<List<Task>>
}

// TODO - Move this to the backend in some way
suspend fun Task.loadAssignees(supabaseClient: SupabaseClient) {
    val data = supabaseClient
        .from("tasks_users")
        .select(columns = Columns.list("task_id")) {
            filter {
                eq("task_id", id)
            }
        }.data

    val usersIds = Json
        .parseToJsonElement(data)
        .jsonArray
        .mapNotNull {
            (it as? JsonObject)?.get("user_id")?.jsonPrimitive?.content
        }

    assignees = supabaseClient
        .from("users")
        .select {
            filter {
                isIn("id", usersIds)
            }
        }.decodeList()
}
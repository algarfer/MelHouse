package com.uniovi.melhouse.data.repository.task

import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.repository.Repository
import java.time.LocalDate
import java.util.UUID

interface TaskRepository : Repository<Task> {
    suspend fun findByDate(date: LocalDate?): List<Task>
    suspend fun findByFlatId(flatId: UUID): List<Task>
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

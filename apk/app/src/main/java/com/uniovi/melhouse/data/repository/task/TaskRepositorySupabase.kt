package com.uniovi.melhouse.data.repository.task

import com.uniovi.melhouse.data.model.Task
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@OptIn(SupabaseExperimental::class)
class TaskRepositorySupabase @Inject constructor(
    private val supabaseClient: SupabaseClient
) : TaskRepository {

    companion object {
        private const val TABLE_NAME = "tasks"
    }

    override suspend fun findByDate(date: LocalDate?): List<Task> {
        return supabaseClient
            .from(TABLE_NAME)
            .select {
                filter {
                    eq("end_date", date.toString())
                }
            }.decodeList()
    }

    override suspend fun findByFlatId(flatId: UUID): List<Task> {
        return supabaseClient
            .from(TABLE_NAME)
            .select {
                filter {
                    eq("flat_id", flatId)
                }
            }.decodeList()
    }

    override fun findByFlatIdAsFlow(flatId: UUID): Flow<List<Task>> {
        return supabaseClient
            .from(TABLE_NAME)
            .selectAsFlow(
                Task::id,
                filter = FilterOperation("flat_id", FilterOperator.EQ, flatId)
            )
    }

    override fun findAssignedByDateAsFlow(date: LocalDate?): Flow<List<Task>> {
        return supabaseClient
            .from(TABLE_NAME)
            .selectAsFlow(Task::id)
            .map { it.filter { task -> task.endDate == date } }
    }

    override suspend fun findAssignedByDate(date: LocalDate?): List<Task> {
        return supabaseClient
            .postgrest.rpc("get_user_tasks", buildJsonObject {
                put("p_date", date?.toString())
            }).decodeList()
    }

    override suspend fun insert(entity: Task) {
        supabaseClient
            .from(TABLE_NAME)
            .insert(entity)
    }

    override suspend fun update(entity: Task) {
        supabaseClient
            .from(TABLE_NAME)
            .update(entity) {
                filter {
                    eq("id", entity.id)
                }
            }
    }

    override suspend fun delete(entity: Task) {
        supabaseClient
            .from(TABLE_NAME)
            .delete {
                filter {
                    eq("id", entity.id)
                }
            }
    }

    override suspend fun findById(id: UUID): Task? {
        return supabaseClient
            .from(TABLE_NAME)
            .select {
                filter {
                    eq("id", id)
                }
            }.decodeSingleOrNull()
    }

    override suspend fun findAll(): List<Task> {
        return supabaseClient
            .from(TABLE_NAME)
            .select()
            .decodeList()
    }

    override fun findAllAsFlow(): Flow<List<Task>> {
        return supabaseClient
            .from(TABLE_NAME)
            .selectAsFlow(Task::id)
    }

    override fun findByIdAsFlow(id: UUID): Flow<Task> {
        return supabaseClient
            .from(TABLE_NAME)
            .selectSingleValueAsFlow(Task::id) {
                Task::id eq id
            }
    }
}
package com.uniovi.melhouse.data.repository.task

import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

class TaskRepositorySupabase @Inject constructor(
    private val supabaseClient: SupabaseClient
) : TaskRepository {

    private val TABLE_NAME = "tasks"

    override suspend fun findByDate(date: LocalDate?): List<Task> {
        return supabaseClient
            .from(TABLE_NAME)
            .select {
                filter {
                    eq("end_date", date.toString())
                }
            }.decodeList()
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

    override suspend fun delete(id: UUID) {
        supabaseClient
            .from(TABLE_NAME)
            .delete {
                filter {
                    eq("id", id)
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
}
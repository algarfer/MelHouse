package com.uniovi.melhouse.data.repository.flat

import com.uniovi.melhouse.data.model.Flat
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class FlatRepositorySupabase(
    private val supabaseClient: SupabaseClient
) : FlatRepository {

    private val TABLE_NAME = "flats"

    override suspend fun insert(entity: Flat) {
        supabaseClient
            .from(TABLE_NAME)
            .insert(entity)
    }

    override suspend fun update(entity: Flat) {
        supabaseClient
            .from(TABLE_NAME)
            .update(entity) {
                filter {
                    eq("id", entity.id)
                }
            }
    }

    override suspend fun delete(entity: Flat) {
        supabaseClient
            .from(TABLE_NAME)
            .delete {
                filter {
                    eq("id", entity.id)
                }
            }
    }

    override suspend fun findById(id: Any): Flat? {
        return supabaseClient
            .from(TABLE_NAME)
            .select {
                filter {
                    eq("id", id)
                }
            }.decodeAsOrNull()
    }

    override suspend fun findAll(): List<Flat> {
        return supabaseClient
            .from(TABLE_NAME)
            .select()
            .decodeList()
    }
}
package com.uniovi.melhouse.data.repository.flat

import com.uniovi.melhouse.data.model.Flat
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.util.UUID
import javax.inject.Inject

class FlatRepositorySupabase @Inject constructor(
    private val supabaseClient: SupabaseClient
) : FlatRepository {

    private val TABLE_NAME = "flats"

    override suspend fun joinFlat(invitationCode: String) {
        supabaseClient
            .postgrest
            .rpc("join_flat", buildJsonObject {
                put("p_code", invitationCode)
            })
    }

    override suspend fun createFlat(flat: Flat) {
        supabaseClient
            .postgrest
            .rpc("create_flat", buildJsonObject {
                put("p_id", flat.id.toString())
                put("p_name", flat.name)
                put("p_address", flat.address)
                put("p_floor", flat.floor)
                put("p_door", flat.door)
                put("p_stair", flat.stair)
            })
    }

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

    override suspend fun delete(id: UUID) {
        supabaseClient
            .from(TABLE_NAME)
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }

    override suspend fun findById(id: UUID): Flat? {
        return supabaseClient
            .from(TABLE_NAME)
            .select {
                filter {
                    eq("id", id)
                }
            }.decodeSingleOrNull()
    }

    override suspend fun findAll(): List<Flat> {
        return supabaseClient
            .from(TABLE_NAME)
            .select()
            .decodeList()
    }

    suspend fun findByAdminId(id: UUID): Flat? {
        return supabaseClient
            .from(TABLE_NAME)
            .select {
                filter {
                    eq("admin_id", id)
                }
            }.decodeSingleOrNull()
    }
}
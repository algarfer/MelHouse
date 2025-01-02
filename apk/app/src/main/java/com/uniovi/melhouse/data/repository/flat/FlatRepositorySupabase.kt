package com.uniovi.melhouse.data.repository.flat

import com.uniovi.melhouse.data.model.Flat
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.util.UUID
import javax.inject.Inject

@OptIn(SupabaseExperimental::class)
class FlatRepositorySupabase @Inject constructor(
    private val supabaseClient: SupabaseClient
) : FlatRepository {

    companion object {
        private const val TABLE_NAME = "flats"
    }

    override suspend fun joinFlat(invitationCode: String): Flat {
        return supabaseClient
            .postgrest
            .rpc("join_flat", buildJsonObject {
                put("p_code", invitationCode)
            }).decodeAs()
    }

    override suspend fun createFlat(flat: Flat): Flat {
        return supabaseClient
            .postgrest
            .rpc("create_flat", buildJsonObject {
                put("p_name", flat.name)
                put("p_address", flat.address)
                put("p_floor", flat.floor)
                put("p_door", flat.door)
                put("p_stair", flat.stair)
            }).decodeAs()
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

    override suspend fun delete(entity: Flat) {
        supabaseClient
            .from(TABLE_NAME)
            .delete {
                filter {
                    eq("id", entity.id)
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

    override fun findAllAsFlow(): Flow<List<Flat>> {
        return supabaseClient
            .from(TABLE_NAME)
            .selectAsFlow(Flat::id)
    }

    override fun findByIdAsFlow(id: UUID): Flow<Flat> {
        supabaseClient.realtime
        return supabaseClient
            .from(TABLE_NAME)
            .selectSingleValueAsFlow(Flat::id) {
                Flat::id eq id
            }
    }
}
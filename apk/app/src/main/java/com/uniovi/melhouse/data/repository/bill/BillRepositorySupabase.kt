package com.uniovi.melhouse.data.repository.bill

import com.uniovi.melhouse.data.model.Bill
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

//@OptIn(SupabaseExperimental::class)
class BillRepositorySupabase @Inject constructor(
    private val supabaseClient: SupabaseClient
): BillRepository{

    companion object {
        private const val TABLE_NAME = "bills"
    }

    override suspend fun insert(entity: Bill) {
        supabaseClient
            .from(TABLE_NAME)
            .insert(entity)
    }

    override suspend fun update(entity: Bill) {
        supabaseClient
            .from(TABLE_NAME)
            .update(entity) {
                filter {
                    eq("id", entity.id)
                }
            }
    }

    override suspend fun delete(entity: Bill) {
        supabaseClient
            .from(TABLE_NAME)
            .delete {
                filter {
                    eq("id", entity.id)
                }
            }
    }

    override suspend fun findById(id: UUID): Bill? {
        return supabaseClient
            .from(TABLE_NAME)
            .select {
                filter {
                    eq("id", id)
                }
            }.decodeSingleOrNull()
    }

    override suspend fun findAll(): List<Bill> {
        return supabaseClient
            .from(TABLE_NAME)
            .select()
            .decodeList()
    }

    override fun findAllAsFlow(): Flow<List<Bill>> {
        TODO("Not yet implemented")
    }

    override fun findByIdAsFlow(id: UUID): Flow<Bill> {
        TODO("Not yet implemented")
    }

}
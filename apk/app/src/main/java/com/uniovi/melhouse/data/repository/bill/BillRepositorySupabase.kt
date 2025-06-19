package com.uniovi.melhouse.data.repository.bill

import com.uniovi.melhouse.data.model.Bill
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

@OptIn(SupabaseExperimental::class)
class BillRepositorySupabase @Inject constructor(
    private val supabaseClient: SupabaseClient
) : BillRepository {

    companion object {
        private const val TABLE_NAME = "bills"
    }

    override fun findAllByFlatIdAsFlow(flatId: UUID): Flow<List<Bill>> {
        return supabaseClient
            .from(TABLE_NAME)
            .selectAsFlow(
                Bill::id,
                filter = FilterOperation("flat_id", FilterOperator.EQ, flatId)
            )
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
        return supabaseClient
            .from(TABLE_NAME)
            .selectAsFlow(Bill::id)
    }

    override fun findByIdAsFlow(id: UUID): Flow<Bill> {
        return supabaseClient
            .from(TABLE_NAME)
            .selectSingleValueAsFlow(Bill::id) {
                Bill::id eq id
            }
    }

}
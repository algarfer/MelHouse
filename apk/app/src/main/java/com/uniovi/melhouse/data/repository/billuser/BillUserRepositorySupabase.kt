package com.uniovi.melhouse.data.repository.billuser

import com.uniovi.melhouse.data.model.BillUser
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

//@OptIn(SupabaseExperimental::class)
class BillUserRepositorySupabase @Inject constructor(
    private val supabaseClient: SupabaseClient
): BillUserRepository {

    companion object {
        private const val TABLE_NAME = "bill_users"
    }

    override suspend fun getBillUsers(billId: UUID): List<BillUser> {
        return supabaseClient
            .from(TABLE_NAME)
            .select{
                filter {
                    eq("bill_id", billId)
                }
            }
            .decodeList()
    }

    override suspend fun insert(entity: BillUser) {
        supabaseClient
            .from(TABLE_NAME)
            .insert(entity)
    }

    override suspend fun update(entity: BillUser) {
        supabaseClient
            .from(TABLE_NAME)
            .update(entity) {
                filter {
                    and {
                        eq("bill_id", entity.billId)
                        eq("user_id", entity.userId)
                    }
                }
            }
    }

    override suspend fun delete(entity: BillUser) {
        supabaseClient
            .from(TABLE_NAME)
            .delete {
                filter {
                    and {
                        eq("bill_id", entity.billId)
                        eq("user_id", entity.userId)
                    }
                }
            }
    }

    override suspend fun findById(id: UUID): BillUser? {
        TODO("Not yet implemented")
    }

    override suspend fun findAll(): List<BillUser> {
        return supabaseClient
            .from(TABLE_NAME)
            .select()
            .decodeList()
    }

    override fun findAllAsFlow(): Flow<List<BillUser>> {
        TODO("Not yet implemented")
    }

    override fun findByIdAsFlow(id: UUID): Flow<BillUser> {
        TODO("Not yet implemented")
    }

}
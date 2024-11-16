package com.uniovi.melhouse.data.repository.user

import com.uniovi.melhouse.data.model.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import java.util.UUID

class UserRepositorySupabase(
    private val supabaseClient: SupabaseClient
): UserRepository {

    private val TABLE_NAME = "users"

    override suspend fun findByEmail(email: String): User? {
        return supabaseClient
            .from(TABLE_NAME)
            .select {
                filter {
                    eq("email", email)
                }
            }.decodeAsOrNull()
    }

    override suspend fun insert(entity: User) {
        supabaseClient
            .from(TABLE_NAME)
            .insert(entity)
    }

    override suspend fun update(entity: User) {
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

    override suspend fun findById(id: UUID): User? {
        return supabaseClient
            .from(TABLE_NAME)
            .select {
                filter {
                    eq("id", id)
                }
            }.decodeAsOrNull()
    }

    override suspend fun findAll(): List<User> {
        return supabaseClient
            .from(TABLE_NAME)
            .select()
            .decodeList()
    }
}
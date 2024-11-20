package com.uniovi.melhouse.data

import com.uniovi.melhouse.data.model.Flat
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.repository.flat.FlatRepository
import com.uniovi.melhouse.data.repository.user.UserRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseUserSessionFacade @Inject constructor(
    private val supabase: SupabaseClient,
    private val userRepository: UserRepository,
    private val flatRepository: FlatRepository
) {

    suspend fun loadFromStorage() : Boolean {
        return supabase.auth.loadFromStorage()
    }

    suspend fun signUp(email: String, password: String, name: String) : User {
        supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = password
            data = buildJsonObject {
                put("name", name)
            }
        }

        supabase
            .auth
            .sessionManager
            .saveSession(
                supabase
                    .auth
                    .currentSessionOrNull()!!
            )

        return getUserData()
    }

    suspend fun logIn(email: String, password: String) : User {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }

        supabase
            .auth
            .sessionManager
            .saveSession(
                supabase
                    .auth
                    .currentSessionOrNull()!!
            )

        return getUserData()
    }

    suspend fun getFlat(): Flat? {
        return getUserData().flatId?.let {
            flatRepository.findById(it)
        }
    }

    suspend fun getUserData(): User {
        return userRepository
            .findById(
                UUID
                    .fromString(
                        supabase
                            .auth
                            .currentUserOrNull()!!
                            .id
                    )
            )!!
    }

    fun getUserId(): UUID? {
        return supabase
            .auth
            .currentUserOrNull()?.let {
                UUID.fromString(it.id)
            }
    }

}
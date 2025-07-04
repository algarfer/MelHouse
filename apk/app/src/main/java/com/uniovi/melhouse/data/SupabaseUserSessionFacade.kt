package com.uniovi.melhouse.data

import com.uniovi.melhouse.data.model.Flat
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.repository.flat.FlatRepository
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.preference.Prefs
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseUserSessionFacade @Inject constructor(
    private val supabase: SupabaseClient,
    private val userRepository: UserRepository,
    private val flatRepository: FlatRepository,
    private val prefs: Prefs
) {

    suspend fun loadFromStorage(): Boolean {
        val result = supabase.auth.loadFromStorage()
        if (!result) return false
        val user = supabase.auth.currentUserOrNull()
        if (user == null || userRepository.findById(UUID.fromString(user.id)) == null) {
            supabase.auth.clearSession()
            return false
        }
        return true
    }

    suspend fun signUp(email: String, password: String, name: String, fcmToken: String): User {
        Executor.safeCall {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                data = buildJsonObject {
                    put("name", name)
                    put("fcm_token", fcmToken)
                }
            }
            prefs.setFcmTokenStoredServer(true)
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

    suspend fun clearSession() = supabase.auth.clearSession()

    suspend fun logIn(email: String, password: String): User {
        Executor.safeCall {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
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

        updateFCMToken(prefs.getFcmToken()!!)
        return getUserData()
    }

    suspend fun getFlat(): Flat? {
        return Executor.safeCall {
            getUserData().flatId?.let {
                flatRepository.findById(it)
            }
        }
    }

    suspend fun getUserData(): User {
        return Executor.safeCall {
            userRepository
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
    }

    fun getUserId(): UUID? {
        return supabase
            .auth
            .currentUserOrNull()?.let {
                UUID.fromString(it.id)
            }
    }

    suspend fun isFlatAdmin(): Boolean {
        return Executor.safeCall {
            supabase
                .postgrest
                .rpc("is_admin")
                .decodeAs()
        }
    }

    suspend fun updateFCMToken(token: String?) {
        Executor.safeCall {
            val user = getUserData()
            userRepository.update(user.copy(fcmToken = token))
            prefs.setFcmTokenStoredServer(true)
        }
    }
}
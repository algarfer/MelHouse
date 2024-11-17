package com.uniovi.melhouse.data.database

import com.uniovi.melhouse.preference.Config
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object Supabase : Database<SupabaseClient> {

    @Volatile
    private var instance: SupabaseClient? = null

    fun init() {
        if(instance != null) return

        instance = createSupabaseClient(
            Config.SUPABASE_URL,
            Config.SUPABASE_ANON_KEY
        ) {
            install(Auth)
            install(Postgrest)
        }
    }

    override fun getInstance(): SupabaseClient {
        if(instance == null) {
            throw IllegalStateException("Database not initialized")
        }
        return instance!!
    }
}
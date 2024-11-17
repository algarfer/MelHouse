package com.uniovi.melhouse.di.modules

import com.uniovi.melhouse.data.database.Database
import com.uniovi.melhouse.data.repository.flat.FlatRepository
import com.uniovi.melhouse.data.repository.flat.FlatRepositorySupabase
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.data.repository.task.TaskRepositorySupabase
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.data.repository.user.UserRepositorySupabase
import com.uniovi.melhouse.di.qualifiers.SupabaseDatabaseQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseRepositoryModule : RepositoryModule<SupabaseClient> {

    @Provides
    @Singleton
    @SupabaseDatabaseQualifier
    override fun provideUserRepository(database: Database<SupabaseClient>): UserRepository {
        return UserRepositorySupabase(database.getInstance())
    }

    @Provides
    @Singleton
    @SupabaseDatabaseQualifier
    override fun provideTaskRepository(database: Database<SupabaseClient>): TaskRepository {
        return TaskRepositorySupabase(database.getInstance())
    }

    @Provides
    @Singleton
    @SupabaseDatabaseQualifier
    override fun provideFlatRepository(database: Database<SupabaseClient>): FlatRepository {
        return FlatRepositorySupabase(database.getInstance())
    }
}
package com.uniovi.melhouse.di.modules

import android.database.sqlite.SQLiteDatabase
import com.uniovi.melhouse.data.database.Database
import com.uniovi.melhouse.data.repository.flat.FlatRepository
import com.uniovi.melhouse.data.repository.flat.FlatRepositorySQLite
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.data.repository.task.TaskRepositorySQLite
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.data.repository.user.UserRepositorySQLite
import com.uniovi.melhouse.di.qualifiers.SQLiteDatabaseQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SQLiteRepositoryModule : RepositoryModule<SQLiteDatabase> {

    @Provides
    @Singleton
    @SQLiteDatabaseQualifier
    override fun provideUserRepository(database: Database<SQLiteDatabase>): UserRepository {
        return UserRepositorySQLite(database.getInstance())
    }

    @Provides
    @Singleton
    @SQLiteDatabaseQualifier
    override fun provideFlatRepository(database: Database<SQLiteDatabase>): FlatRepository {
        return FlatRepositorySQLite(database.getInstance())
    }

    @Provides
    @Singleton
    @SQLiteDatabaseQualifier
    override fun provideTaskRepository(database: Database<SQLiteDatabase>): TaskRepository {
        return TaskRepositorySQLite(database.getInstance())
    }
}
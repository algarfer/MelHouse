package com.uniovi.melhouse.di.modules

import android.database.sqlite.SQLiteDatabase
import com.uniovi.melhouse.data.database.Database
import com.uniovi.melhouse.data.database.SQLite
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun getSQLiteDatabase(): Database<SQLiteDatabase> {
        return SQLite
    }

    @Provides
    @Singleton
    fun getSupabaseDatabase(): Any {
        TODO("Not yet implemented")
    }

}
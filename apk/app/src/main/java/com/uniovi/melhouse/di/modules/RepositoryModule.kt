package com.uniovi.melhouse.di.modules

import com.uniovi.melhouse.data.database.Database
import com.uniovi.melhouse.data.repository.flat.FlatRepository
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.data.repository.user.UserRepository

interface RepositoryModule<T> {

    fun provideUserRepository(database: Database<T>): UserRepository
    fun provideTaskRepository(database: Database<T>): TaskRepository
    fun provideFlatRepository(database: Database<T>): FlatRepository
}
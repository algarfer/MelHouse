package com.uniovi.melhouse.di.modules

import com.uniovi.melhouse.data.repository.flat.FlatRepository
import com.uniovi.melhouse.data.repository.flat.FlatRepositorySupabase
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.data.repository.task.TaskRepositorySupabase
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.data.repository.user.UserRepositorySupabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModule {

    @Provides
    @Singleton
    fun provideUserRepository(repositorySupabase: UserRepositorySupabase): UserRepository {
        return repositorySupabase
    }

    @Provides
    @Singleton
    fun provideTaskRepository(repositorySupabase: TaskRepositorySupabase): TaskRepository {
        return repositorySupabase
    }

    @Provides
    @Singleton
    fun provideFlatRepository(repositorySupabase: FlatRepositorySupabase): FlatRepository {
        return repositorySupabase
    }
}
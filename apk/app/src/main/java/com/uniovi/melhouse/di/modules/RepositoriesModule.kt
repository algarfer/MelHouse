package com.uniovi.melhouse.di.modules

import com.uniovi.melhouse.data.repository.bill.BillRepository
import com.uniovi.melhouse.data.repository.bill.BillRepositorySupabase
import com.uniovi.melhouse.data.repository.billuser.BillUserRepository
import com.uniovi.melhouse.data.repository.billuser.BillUserRepositorySupabase
import com.uniovi.melhouse.data.repository.flat.FlatRepository
import com.uniovi.melhouse.data.repository.flat.FlatRepositorySupabase
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.data.repository.task.TaskRepositorySupabase
import com.uniovi.melhouse.data.repository.taskuser.TaskUserRepository
import com.uniovi.melhouse.data.repository.taskuser.TaskUserRepositorySupabase
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
    fun provideUserRepository(repositorySupabase: UserRepositorySupabase): UserRepository =
        repositorySupabase

    @Provides
    @Singleton
    fun provideTaskRepository(repositorySupabase: TaskRepositorySupabase): TaskRepository =
        repositorySupabase

    @Provides
    @Singleton
    fun provideFlatRepository(repositorySupabase: FlatRepositorySupabase): FlatRepository =
        repositorySupabase

    @Provides
    @Singleton
    fun provideTaskUserRepository(repositorySupabase: TaskUserRepositorySupabase): TaskUserRepository =
        repositorySupabase

    @Provides
    @Singleton
    fun provideBillRepository(repositorySupabase: BillRepositorySupabase): BillRepository =
        repositorySupabase

    @Provides
    @Singleton
    fun provideBillUserRepository(repositorySupabase: BillUserRepositorySupabase): BillUserRepository =
        repositorySupabase
}
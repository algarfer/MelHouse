package com.uniovi.melhouse.widget.service

import android.content.Intent
import android.widget.RemoteViewsService
import com.uniovi.melhouse.data.SupabaseUserSessionFacade
import com.uniovi.melhouse.data.repository.task.TaskRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@AndroidEntryPoint
class NextTasksService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        val appContext = applicationContext

        val entryPoint = EntryPointAccessors.fromApplication(
            appContext,
            TasksRemoteViewsFactoryEntryPoint::class.java
        )

        return TasksRemoteViewsFactory(
            appContext,
            entryPoint.session(),
            entryPoint.taskRepository()
        )
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface TasksRemoteViewsFactoryEntryPoint {
    fun session(): SupabaseUserSessionFacade
    fun taskRepository(): TaskRepository
}

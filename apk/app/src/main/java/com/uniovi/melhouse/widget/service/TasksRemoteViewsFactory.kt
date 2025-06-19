package com.uniovi.melhouse.widget.service

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.SupabaseUserSessionFacade
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.repository.task.TaskRepository
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

class TasksRemoteViewsFactory (
    private val context: Context,
    private val session: SupabaseUserSessionFacade,
    private val taskRepository: TaskRepository
) : RemoteViewsService.RemoteViewsFactory {

    private var tasks: List<Task> = listOf()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        runBlocking {
            tasks = if (session.loadFromStorage())
                taskRepository.findAll().filter { it.endDate == LocalDate.now() }
            else
                emptyList()
        }
    }
    
    override fun onDestroy() {}

    override fun getCount(): Int = tasks.size

    override fun getViewAt(position: Int): RemoteViews {
        val task = tasks[position]
        val views = RemoteViews(context.packageName, R.layout.widget_next_task_item)

        views.setTextViewText(R.id.tvTaskTitle, task.name)
        views.setTextViewText(R.id.tvTaskDescription, task.description)

        return views
    }

    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount(): Int = 1
    override fun getItemId(position: Int): Long = position.toLong()
    override fun hasStableIds(): Boolean = true
}
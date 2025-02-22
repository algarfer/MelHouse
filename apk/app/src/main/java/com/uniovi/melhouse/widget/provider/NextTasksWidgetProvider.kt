package com.uniovi.melhouse.widget.provider

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.uniovi.melhouse.R
import com.uniovi.melhouse.widget.service.NextTasksService

class NextTasksWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_next_tasks)

        val intent = Intent(context, NextTasksService::class.java)
        views.setRemoteAdapter(R.id.widget_list, intent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}

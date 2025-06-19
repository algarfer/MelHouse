package com.uniovi.melhouse.widget.provider

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.uniovi.melhouse.R
import com.uniovi.melhouse.presentation.activities.SplashScreenActivity
import com.uniovi.melhouse.widget.service.NextTasksService

class NextTasksWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_next_tasks)

        val serviceIntent = Intent(context, NextTasksService::class.java)
        views.setRemoteAdapter(R.id.widget_list, serviceIntent)

        val openAppIntent = Intent(context, SplashScreenActivity::class.java)
        openAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        views.setOnClickPendingIntent(R.id.widget_title, pendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    companion object WidgetUpdateHelper {
        fun updateTaskWidgets(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(context, NextTasksWidgetProvider::class.java)
            )

            val updateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            updateIntent.component = ComponentName(context, NextTasksWidgetProvider::class.java)
            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            context.sendBroadcast(updateIntent)

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list)
        }
    }
}

package com.uniovi.melhouse.utils

import android.content.Context
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.TaskStatus
import com.uniovi.melhouse.data.model.User

fun List<User>.toAsigneesPieChart(context: Context): PieData {
    val entries = mutableListOf<PieEntry>()
    val totalTasks = this.sumOf { it.tasks.size }

    this.forEach {user ->
        val percentage = (user.tasks.size.toFloat() / totalTasks) * 100
        if (percentage > 0) {
            entries.add(PieEntry(percentage, user.name))
        }
    }

    val set = PieDataSet(entries, context.getString(R.string.chart_pie_asignees))
    set.setColors(*context.resources.getIntArray(R.array.chart_colors))
    return PieData(set)
}

fun List<Task>.toStatusBarChartData(context: Context): Pair<BarData, List<LegendEntry>> {
    val entries = mutableListOf<BarEntry>()
    val legendEntries = mutableListOf<LegendEntry>()
    val colors = mutableListOf<Int>()

    val values = this
        .groupBy { it.status }
        .mapValues { it.value.size }

    values.entries.forEachIndexed { i, entry ->
        val label = entry.key?.getString(context) ?: context.getString(R.string.chart_not_available_short)
        val qty = entry.value

        entries.add(BarEntry(i.toFloat(), qty.toFloat()))
        val color = when(entry.key) {
            TaskStatus.DONE -> context.getColorCompat(R.color.task_status_done)
            TaskStatus.INPROGRESS -> context.getColorCompat(R.color.task_status_in_progress)
            TaskStatus.PENDING -> context.getColorCompat(R.color.task_status_pending)
            TaskStatus.CANCELLED -> context.getColorCompat(R.color.task_status_cancelled)
            else -> context.getColorCompat(R.color.task_status_no_status)
        }
        colors.add(color)
        legendEntries.add(LegendEntry(label, Legend.LegendForm.SQUARE, 10f, 10f, null, color))
    }

    val set = BarDataSet(entries, context.getString(R.string.chart_bar_tasks_status))
    set.colors = colors
    val data = BarData(set)
    data.barWidth = 0.9f

    return Pair(data, legendEntries)
}

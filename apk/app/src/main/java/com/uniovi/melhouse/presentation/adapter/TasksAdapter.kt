package com.uniovi.melhouse.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.presentation.diffutil.TasksDiffUtil
import com.uniovi.melhouse.presentation.viewholder.TasksViewHolder

class TasksAdapter(list: List<Task>) : AbstractAdapter<Task, TasksViewHolder>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        return TasksViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.calendar_task_layout, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun updateList(newList: List<Task>) {
        val taskDiff = TasksDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(taskDiff)
        list = newList
        result.dispatchUpdatesTo(this)
    }
}
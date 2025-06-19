package com.uniovi.melhouse.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.factories.presentation.viewholder.NextTasksViewHolderFactory
import com.uniovi.melhouse.presentation.adapters.diffutil.TasksDiffUtil
import com.uniovi.melhouse.presentation.viewholder.NextTasksViewHolder
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class NextTasksAdapter @AssistedInject constructor(
    @Assisted list: List<Task>,
    private val nextTasksViewHolderFactory: NextTasksViewHolderFactory
) : AbstractAdapter<Task, NextTasksViewHolder>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextTasksViewHolder {
        return nextTasksViewHolderFactory.create(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.next_task_layout, parent, false)
        )
    }

    override fun updateList(newList: List<Task>) {
        val taskDiff = TasksDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(taskDiff)
        list = newList
        result.dispatchUpdatesTo(this)
    }
}
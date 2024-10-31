package com.uniovi.melhouse.presentation.viewholder

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.taskDateFormatter
import com.uniovi.melhouse.databinding.CalendarTaskLayoutBinding
import com.uniovi.melhouse.presentation.utils.makeGone
import com.uniovi.melhouse.presentation.utils.makeVisible

class TasksViewHolder(view: View) : AbstractViewHolder<Task>(view) {

    private val binding = CalendarTaskLayoutBinding.bind(view)

    override fun render(item: Task) {
        binding.taskDate.text = taskDateFormatter.format(item.endDate)
        binding.taskTitle.text = item.name

        if(item.description.isNullOrEmpty()) {
            binding.taskDescription.text = ""
            binding.taskDescription.makeGone()
        } else {
            binding.taskDescription.text = item.description
            binding.taskDescription.makeVisible()
        }

        itemView.setOnClickListener {
            Toast.makeText(itemView.context,
                "TODO: Show task ${item.name} details",
                Toast.LENGTH_SHORT).show()
        }
    }
}
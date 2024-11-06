package com.uniovi.melhouse.presentation.viewholder

import android.view.View
import androidx.fragment.app.FragmentManager
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.databinding.CalendarTaskLayoutBinding
import com.uniovi.melhouse.presentation.fragments.TaskBottomSheetDialog
import com.uniovi.melhouse.utils.makeGone
import com.uniovi.melhouse.utils.makeVisible
import com.uniovi.melhouse.utils.taskDateFormatter

class TasksViewHolder(view: View, private val taskpressedHandler: (Task) -> Unit) : AbstractViewHolder<Task>(view) {

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
            taskpressedHandler(item)
        }
    }
}


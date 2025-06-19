package com.uniovi.melhouse.presentation.viewholder

import android.view.View
import androidx.fragment.app.FragmentManager
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.databinding.CalendarTaskLayoutBinding
import com.uniovi.melhouse.presentation.fragments.TaskBottomSheetDialog
import com.uniovi.melhouse.utils.adaptTextToSize
import com.uniovi.melhouse.utils.makeGone
import com.uniovi.melhouse.utils.makeVisible
import com.uniovi.melhouse.utils.taskDateFormatter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.UUID

class TasksViewHolder @AssistedInject constructor(
    @Assisted view: View,
    @Assisted private val taskpressedHandler: (Task) -> Unit
) : AbstractViewHolder<Task>(view) {

    private val binding = CalendarTaskLayoutBinding.bind(view)

    override fun render(item: Task) {
        binding.taskDate.text = taskDateFormatter.format(item.endDate)
        binding.taskTitle.text = item.name.adaptTextToSize()

        binding.taskDescription.text = item.description?.adaptTextToSize()

        if (item.description.isNullOrEmpty())
            binding.taskDescription.makeGone()
        else
            binding.taskDescription.makeVisible()

        itemView.setOnClickListener {
            taskpressedHandler(item)
        }
    }
}

// TODO - Refactor this
fun taskPressedHandler(fragmentManager: FragmentManager, taskId: UUID) {
    val modal = TaskBottomSheetDialog.create(taskId)
    fragmentManager.let { modal.show(it, TaskBottomSheetDialog.TAG) }
}

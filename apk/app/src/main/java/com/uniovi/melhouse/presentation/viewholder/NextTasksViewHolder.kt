package com.uniovi.melhouse.presentation.viewholder

import android.view.View
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.databinding.NextTaskLayoutBinding
import com.uniovi.melhouse.utils.taskDateFormatter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class NextTasksViewHolder @AssistedInject constructor(
    @Assisted view: View,
) : AbstractViewHolder<Task>(view) {

    private val binding = NextTaskLayoutBinding.bind(view)

    override fun render(item: Task) {
        binding.taskDate.text = taskDateFormatter.format(item.endDate)
        binding.taskTitle.text = item.name
    }

}
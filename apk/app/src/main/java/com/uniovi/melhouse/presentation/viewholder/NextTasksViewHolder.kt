package com.uniovi.melhouse.presentation.viewholder

import android.view.View
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.databinding.NextTaskLayoutBinding
import com.uniovi.melhouse.utils.adaptTextToSize
import com.uniovi.melhouse.utils.getColorCompat
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class NextTasksViewHolder @AssistedInject constructor(
    @Assisted private val view: View
) : AbstractViewHolder<Task>(view) {

    private val binding = NextTaskLayoutBinding.bind(view)

    override fun render(item: Task) {
//        binding.taskDate.text = taskDateFormatter.format(item.endDate)
        binding.taskTitle.text = item.name.adaptTextToSize()
        binding.taskTitle.setTextColor(view.context.getColorCompat(R.color.on_surface))
        binding.taskDescription.text = item.description?.adaptTextToSize(30)
        binding.taskDescription.setTextColor(view.context.getColorCompat(R.color.on_surface))
    }

}
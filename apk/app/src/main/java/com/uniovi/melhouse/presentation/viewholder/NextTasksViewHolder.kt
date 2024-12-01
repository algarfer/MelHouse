package com.uniovi.melhouse.presentation.viewholder

import android.view.View
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.databinding.NextTaskLayoutBinding
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class NextTasksViewHolder @AssistedInject constructor(
    @Assisted view: View,
) : AbstractViewHolder<Task>(view) {

    private val binding = NextTaskLayoutBinding.bind(view)

    override fun render(item: Task) {
//        binding.taskDate.text = taskDateFormatter.format(item.endDate)
        if(item.description != null && item.description!!.isNotEmpty()){
            binding.taskDescription.text = item.description
        }else{
            binding.taskDescription.text = "Sin descripción"
        }
        binding.taskTitle.text = item.name
    }

}
package com.uniovi.melhouse.factories.presentation.viewholder

import android.view.View
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.presentation.viewholder.TasksViewHolder
import dagger.assisted.AssistedFactory

@AssistedFactory
interface TasksViewHolderFactory {

    fun create(view: View, taskPressedHandler: (Task) -> Unit): TasksViewHolder
}
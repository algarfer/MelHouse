package com.uniovi.melhouse.factories.presentation.adapter

import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.presentation.adapters.TasksAdapter
import dagger.assisted.AssistedFactory

@AssistedFactory
interface TasksAdapterFactory {

    fun create(list: List<Task>, taskHandler: (Task) -> Unit): TasksAdapter
}
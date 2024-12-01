package com.uniovi.melhouse.factories.presentation.adapter

import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.presentation.adapters.NextTasksAdapter
import dagger.assisted.AssistedFactory

@AssistedFactory
interface NextTasksAdapterFactory {

    fun create(list: List<Task>): NextTasksAdapter

}
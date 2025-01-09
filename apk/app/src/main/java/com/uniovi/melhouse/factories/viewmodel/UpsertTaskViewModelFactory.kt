package com.uniovi.melhouse.factories.viewmodel

import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.viewmodel.UpsertTaskViewModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface UpsertTaskViewModelFactory {

    fun create(task: Task?): UpsertTaskViewModel
}
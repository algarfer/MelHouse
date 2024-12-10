package com.uniovi.melhouse.factories.viewmodel

import com.uniovi.melhouse.data.model.Flat
import com.uniovi.melhouse.viewmodel.UpsertFlatViewModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface UpsertFlatViewModelFactory {

    fun create(flat: Flat?): UpsertFlatViewModel
}
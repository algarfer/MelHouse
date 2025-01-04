package com.uniovi.melhouse.factories.presentation.adapter

import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.presentation.adapters.PartnersAdapter
import com.uniovi.melhouse.viewmodel.FlatFragmentViewModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface PartnersAdapterFactory {

    fun create(list: List<User>, viewModel: FlatFragmentViewModel, user: User): PartnersAdapter
}
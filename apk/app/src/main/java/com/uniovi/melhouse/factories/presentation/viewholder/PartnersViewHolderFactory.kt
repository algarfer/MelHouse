package com.uniovi.melhouse.factories.presentation.viewholder

import android.view.View
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.presentation.viewholder.PartnersViewHolder
import com.uniovi.melhouse.viewmodel.FlatFragmentViewModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface PartnersViewHolderFactory {

    fun create(view: View, viewModel: FlatFragmentViewModel, user: User): PartnersViewHolder
}
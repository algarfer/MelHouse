package com.uniovi.melhouse.factories.presentation.viewholder

import android.view.View
import com.uniovi.melhouse.presentation.viewholder.BillUserViewHolder
import dagger.assisted.AssistedFactory

@AssistedFactory
interface BillUserViewHolderFactory {
    fun create(inflate: View): BillUserViewHolder
}
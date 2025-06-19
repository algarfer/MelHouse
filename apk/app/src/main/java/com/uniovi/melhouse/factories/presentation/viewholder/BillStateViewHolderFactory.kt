package com.uniovi.melhouse.factories.presentation.viewholder

import android.view.View
import com.uniovi.melhouse.presentation.viewholder.BillStateViewHolder
import com.uniovi.melhouse.states.BillState
import com.uniovi.melhouse.viewmodel.BillsFragmentViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface BillStateViewHolderFactory {
    fun create(
        inflate: View,
        @Assisted("billHandler") billHandler: (BillState) -> Unit,
        @Assisted("removeBill") removeBill: (BillState) -> Unit,
        viewModel: BillsFragmentViewModel
    ): BillStateViewHolder
}

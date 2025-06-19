package com.uniovi.melhouse.factories.presentation.adapter

import com.uniovi.melhouse.presentation.adapters.BillsAdapter
import com.uniovi.melhouse.states.BillState
import com.uniovi.melhouse.viewmodel.BillsFragmentViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface BillStateAdapterFactory {

    fun create(
        listOf: List<BillState>,
        @Assisted("billHandler") billHandler: (BillState) -> Unit,
        @Assisted("removeBill") removeBill: (BillState) -> Unit,
        @Assisted viewModel: BillsFragmentViewModel,
    ): BillsAdapter
}
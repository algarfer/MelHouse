package com.uniovi.melhouse.factories.viewmodel

import com.uniovi.melhouse.data.model.Bill
import com.uniovi.melhouse.viewmodel.UpsertBillViewModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface UpsertBillViewModelFactory {

    fun create(bill: Bill?): UpsertBillViewModel
}
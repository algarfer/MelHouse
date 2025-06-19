package com.uniovi.melhouse.factories.presentation.adapter

import com.uniovi.melhouse.presentation.adapters.UserBillSpinnerAdapter
import dagger.assisted.AssistedFactory

@AssistedFactory
interface UserBillAdapterFactory {

    fun create(
        userBills: List<Pair<String, Double>>,
        onValueChanged: (String, Double) -> Unit
    ): UserBillSpinnerAdapter

}
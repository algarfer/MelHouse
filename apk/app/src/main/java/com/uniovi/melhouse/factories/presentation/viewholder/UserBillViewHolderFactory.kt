package com.uniovi.melhouse.factories.presentation.viewholder

import android.view.View
import com.uniovi.melhouse.presentation.viewholder.UserBillViewHolder
import dagger.assisted.AssistedFactory

@AssistedFactory
interface UserBillViewHolderFactory {
    fun create(view: View, onValueChanged: (String, Double) -> Unit): UserBillViewHolder
}
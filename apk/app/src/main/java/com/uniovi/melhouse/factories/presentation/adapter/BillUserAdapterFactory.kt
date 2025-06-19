package com.uniovi.melhouse.factories.presentation.adapter

import com.uniovi.melhouse.data.model.BillUser
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.presentation.adapters.BillUsersAdapter
import dagger.assisted.AssistedFactory

@AssistedFactory
interface BillUserAdapterFactory {

    fun create(mapUserToBillUser: List<Pair<User, BillUser>>): BillUsersAdapter
}
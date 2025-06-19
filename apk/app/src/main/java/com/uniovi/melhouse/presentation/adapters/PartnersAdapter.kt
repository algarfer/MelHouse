package com.uniovi.melhouse.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.factories.presentation.viewholder.PartnersViewHolderFactory
import com.uniovi.melhouse.presentation.adapters.diffutil.PartnersDiffUtil
import com.uniovi.melhouse.presentation.viewholder.PartnersViewHolder
import com.uniovi.melhouse.viewmodel.FlatFragmentViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class PartnersAdapter @AssistedInject constructor(
    @Assisted list: List<User>,
    @Assisted private val viewModel: FlatFragmentViewModel,
    @Assisted private val user: User,
    private val partnersViewHolderFactory: PartnersViewHolderFactory
) : AbstractAdapter<User, PartnersViewHolder>(list) {

    override fun updateList(newList: List<User>) {
        val userDiff = PartnersDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(userDiff)
        list = newList
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnersViewHolder {
        return partnersViewHolderFactory.create(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.flat_partner_layout, parent, false), viewModel, user
        )
    }
}
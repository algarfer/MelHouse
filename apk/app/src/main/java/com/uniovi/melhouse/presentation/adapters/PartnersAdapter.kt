package com.uniovi.melhouse.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.presentation.adapters.diffutil.PartnersDiffUtil
import com.uniovi.melhouse.presentation.viewholder.PartnersViewHolder
import com.uniovi.melhouse.viewmodel.FlatFragmentViewModel

class PartnersAdapter(
    list: List<User>,
    private val viewModel: FlatFragmentViewModel
) : AbstractAdapter<User, PartnersViewHolder>(list) {

    private lateinit var viewHolder: PartnersViewHolder

    override fun updateList(newList: List<User>) {
        val userDiff = PartnersDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(userDiff)
        list = newList
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnersViewHolder {
        viewHolder = PartnersViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.flat_partner_layout, parent, false), viewModel)

        return viewHolder
    }
}
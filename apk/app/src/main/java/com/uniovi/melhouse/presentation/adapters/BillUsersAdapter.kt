package com.uniovi.melhouse.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.model.BillUser
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.factories.presentation.viewholder.BillUserViewHolderFactory
import com.uniovi.melhouse.presentation.adapters.diffutil.BillUserDiffUtil
import com.uniovi.melhouse.presentation.viewholder.BillUserViewHolder
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class BillUsersAdapter @AssistedInject constructor(
    @Assisted list: List<Pair<User, BillUser>>,
    private val billUserViewHolderFactory: BillUserViewHolderFactory
) : AbstractAdapter<Pair<User, BillUser>, BillUserViewHolder>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillUserViewHolder {
        return billUserViewHolderFactory.create(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.bill_user_layout, parent, false)
        )
    }

    override fun updateList(newList: List<Pair<User, BillUser>>) {
        val billUserDiff = BillUserDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(billUserDiff)
        list = newList
        result.dispatchUpdatesTo(this)
    }
}
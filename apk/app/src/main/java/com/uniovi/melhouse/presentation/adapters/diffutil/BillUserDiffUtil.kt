package com.uniovi.melhouse.presentation.adapters.diffutil

import com.uniovi.melhouse.data.model.BillUser
import com.uniovi.melhouse.data.model.User

class BillUserDiffUtil(
    oldList: List<Pair<User, BillUser>>,
    newList: List<Pair<User, BillUser>>
) : AbstractDiffUtil<Pair<User, BillUser>>(oldList, newList) {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].first.id == newList[newItemPosition].first.id &&
                oldList[oldItemPosition].second.billId == newList[newItemPosition].second.billId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].first.id == newList[newItemPosition].first.id &&
                oldList[oldItemPosition].second.billId == newList[newItemPosition].second.billId
    }
}
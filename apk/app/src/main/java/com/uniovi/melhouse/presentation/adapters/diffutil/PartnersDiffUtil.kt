package com.uniovi.melhouse.presentation.adapters.diffutil

import com.uniovi.melhouse.data.model.User

class PartnersDiffUtil(
    oldList: List<User>,
    newList: List<User>
) : AbstractDiffUtil<User>(oldList, newList) {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
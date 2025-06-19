package com.uniovi.melhouse.presentation.adapters.diffutil

import com.uniovi.melhouse.states.BillState

class BillStateDiffUtil(
    oldList: List<BillState>,
    newList: List<BillState>
) : AbstractDiffUtil<BillState>(oldList, newList) {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val b = oldList[oldItemPosition].bill.id == newList[newItemPosition].bill.id &&
                oldList[oldItemPosition].mapUserToBillUser == newList[newItemPosition].mapUserToBillUser
        return b
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val b = oldList[oldItemPosition].bill.id == newList[newItemPosition].bill.id &&
                oldList[oldItemPosition].mapUserToBillUser == newList[newItemPosition].mapUserToBillUser
        return b
    }
}
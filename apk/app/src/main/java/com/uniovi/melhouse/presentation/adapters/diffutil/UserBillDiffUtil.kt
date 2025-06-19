package com.uniovi.melhouse.presentation.adapters.diffutil

class UserBillDiffUtil(
    oldList: List<Pair<String, Double>>,
    newList: List<Pair<String, Double>>
) : AbstractDiffUtil<Pair<String, Double>>(oldList, newList) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].first == newList[newItemPosition].first &&
                oldList[oldItemPosition].second == newList[newItemPosition].second

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        areItemsTheSame(oldItemPosition, newItemPosition)

}
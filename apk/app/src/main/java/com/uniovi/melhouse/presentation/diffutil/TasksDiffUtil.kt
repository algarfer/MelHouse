package com.uniovi.melhouse.presentation.diffutil

import com.uniovi.melhouse.data.model.Task

class TasksDiffUtil(
    oldList: List<Task>,
    newList: List<Task>
) : AbstractDiffUtil<Task>(oldList, newList) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

}
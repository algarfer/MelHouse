package com.uniovi.melhouse.presentation.diffutil

import androidx.recyclerview.widget.DiffUtil

abstract class AbstractDiffUtil<T>(
    protected val oldList: List<T>,
    protected val newList: List<T>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
}
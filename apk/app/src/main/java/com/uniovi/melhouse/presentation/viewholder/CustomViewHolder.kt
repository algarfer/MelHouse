package com.uniovi.melhouse.presentation.viewholder

interface CustomViewHolder<T> {
    fun render(item: T)
    fun onViewRecycled()
}
package com.uniovi.melhouse.presentation.adapters

import androidx.recyclerview.widget.RecyclerView
import com.uniovi.melhouse.presentation.viewholder.AbstractViewHolder

abstract class AbstractAdapter<T, S : AbstractViewHolder<T>>(
    protected var list: List<T>
) : CustomAdapter<T>, RecyclerView.Adapter<S>() {

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: S, position: Int) {
        holder.render(list[position])
    }

    override fun onViewRecycled(holder: S) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }
}
package com.uniovi.melhouse.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractViewHolder<T>(view: View) : CustomViewHolder<T>, RecyclerView.ViewHolder(view)
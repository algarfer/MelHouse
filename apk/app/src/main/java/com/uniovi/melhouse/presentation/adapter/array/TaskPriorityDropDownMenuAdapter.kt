package com.uniovi.melhouse.presentation.adapter.array

import android.content.Context
import androidx.annotation.LayoutRes
import com.uniovi.melhouse.data.model.TaskPriority

class TaskPriorityDropDownMenuAdapter(context: Context, list: List<TaskPriority>,
                                      @LayoutRes layout: Int = android.R.layout.simple_list_item_1)
    : AbstractDropDownMenuAdapter<TaskPriority>(context, layout, list)
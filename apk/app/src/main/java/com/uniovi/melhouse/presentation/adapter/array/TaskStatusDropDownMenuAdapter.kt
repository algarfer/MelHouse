package com.uniovi.melhouse.presentation.adapter.array

import android.content.Context
import androidx.annotation.LayoutRes
import com.uniovi.melhouse.data.model.TaskStatus

class TaskStatusDropDownMenuAdapter(context: Context, list: List<TaskStatus>,
                                    @LayoutRes private val layout: Int = android.R.layout.simple_list_item_1)
    : AbstractDropDownMenuAdapter<TaskStatus>(context, layout, list)
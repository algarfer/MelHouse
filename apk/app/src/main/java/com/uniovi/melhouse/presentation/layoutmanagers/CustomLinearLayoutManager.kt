package com.uniovi.melhouse.presentation.layoutmanagers

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class CustomLinearLayoutManager(
    context: Context,
    direction: Int,
    attachToParent: Boolean
) : LinearLayoutManager(context, direction, attachToParent) {

    private var isScrollEnabled = true

    fun setScrollEnabled(flag: Boolean) {
        isScrollEnabled = flag
    }

    override fun canScrollVertically(): Boolean {
        return super.canScrollVertically() && isScrollEnabled
    }
}
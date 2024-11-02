package com.uniovi.melhouse.presentation.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.uniovi.melhouse.presentation.observers.StatusBarColorLifecycleObserver

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

internal fun Context.getColorCompat(@ColorRes color: Int) =
    ContextCompat.getColor(this, color)

internal fun TextView.setTextColorRes(@ColorRes color: Int) =
    setTextColor(context.getColorCompat(color))

fun Fragment.addStatusBarColorUpdate(@ColorRes colorRes: Int) {
    view?.findViewTreeLifecycleOwner()?.lifecycle?.addObserver(
        StatusBarColorLifecycleObserver(
            requireActivity(),
            requireContext().getColorCompat(colorRes),
        ),
    )
}

internal fun Int.lighterColor(color: Int = Color.WHITE) = ColorUtils.blendARGB(this, color, 0.5f)

fun showWipToast(context: Context) {
    Toast.makeText(context, "WIP", Toast.LENGTH_SHORT).show()
}

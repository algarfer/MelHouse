package com.uniovi.melhouse.utils

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.google.android.material.snackbar.Snackbar
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

fun String.toEditable(): Editable {
    return Editable
        .Factory
        .getInstance()
        .newEditable(this)
}

fun getWarningSnackbar(view: View, text: String) = Snackbar.make(view, text, Snackbar.LENGTH_SHORT)

fun String.ifEmptyNull() = ifEmpty { null }

fun String.adaptTextToSize(size: Int = 30): String {
    if (this.isEmpty()) return ""
    val needTrim = this.contains("\n") || this.length > size
    val d = this.split("\n")[0]
    return if (needTrim) "${d.substring(0, size - 3)}..." else d
}
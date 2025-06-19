package com.uniovi.melhouse.presentation.adapters.array

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.uniovi.melhouse.data.model.LocaleEnum

abstract class AbstractDropDownMenuAdapter<T : LocaleEnum>(
    context: Context,
    @LayoutRes private val layout: Int,
    list: List<T>
) : ArrayAdapter<T>(context, layout, list) {

    var onItemClickListener: AdapterView.OnItemClickListener? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView

        if (row == null) {
            val inflater = LayoutInflater.from(context)
            row = inflater.inflate(layout, parent, false)
        }

        val tv = row!!.findViewById<TextView>(android.R.id.text1)
        tv!!.text = getItem(position)?.getString(context)

        return row
    }
}
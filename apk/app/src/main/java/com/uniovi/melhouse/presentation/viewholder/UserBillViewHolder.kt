package com.uniovi.melhouse.presentation.viewholder

import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.uniovi.melhouse.databinding.UserBillBindingBinding
import com.uniovi.melhouse.viewmodel.UpsertBillViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class UserBillViewHolder @AssistedInject constructor(
    @Assisted private val view: View,
    @Assisted private val onValueChanged: (String, Double) -> Unit
) : AbstractViewHolder<Pair<String, Double>>(view) {

    private val binding = UserBillBindingBinding.bind(view)

    override fun render(item: Pair<String, Double>) {
        binding.tvItemLabel.text = item.first
        binding.etValue.setText(item.second.toString())

        binding.etValue.addTextChangedListener {
            val value = it.toString().toDoubleOrNull() ?: 0.0
            onValueChanged(item.first, value)
        }
    }
}
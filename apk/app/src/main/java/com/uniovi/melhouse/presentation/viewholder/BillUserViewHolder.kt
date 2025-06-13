package com.uniovi.melhouse.presentation.viewholder

import android.view.View
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.model.BillUser
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.databinding.BillUserLayoutBinding
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class BillUserViewHolder @AssistedInject constructor(
    @Assisted var view: View
) : AbstractViewHolder<Pair<User, BillUser>>(view) {
    private val binding = BillUserLayoutBinding.bind(view)

    override fun render(item: Pair<User, BillUser>) {
        binding.textViewName.text = item.first.name
        binding.textViewState.text =
            if (item.second.paid) view.context.getString(R.string.paid) else view.context.getString(
                R.string.pending
            )
        binding.root.setBackgroundColor(
            if (item.second.paid) view.context.getColor(R.color.chart_color_green)
            else view.context.getColor(R.color.chart_color_red)
        )


    }

}

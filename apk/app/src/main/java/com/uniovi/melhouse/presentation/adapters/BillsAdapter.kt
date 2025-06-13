package com.uniovi.melhouse.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.uniovi.melhouse.R
import com.uniovi.melhouse.factories.presentation.viewholder.BillStateViewHolderFactory
import com.uniovi.melhouse.presentation.adapters.diffutil.BillStateDiffUtil
import com.uniovi.melhouse.presentation.viewholder.BillStateViewHolder
import com.uniovi.melhouse.states.BillState
import com.uniovi.melhouse.viewmodel.BillsFragmentViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class BillsAdapter @AssistedInject constructor(
    @Assisted list: List<BillState>,
    @Assisted("billHandler") private val billHandler: (BillState) -> Unit,
    @Assisted("removeBill") private val removeBill: (BillState) -> Unit,
    @Assisted private val viewModel: BillsFragmentViewModel,
    private val billStateViewHolderFactory: BillStateViewHolderFactory,
) : AbstractAdapter<BillState, BillStateViewHolder>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillStateViewHolder {
        return billStateViewHolderFactory.create(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.bill_state_layout, parent, false),
            billHandler,
            removeBill,
            viewModel,
        )
    }

    override fun updateList(newList: List<BillState>) {
        val billStateDiff = BillStateDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(billStateDiff)
        list = newList
        result.dispatchUpdatesTo(this)
    }
}
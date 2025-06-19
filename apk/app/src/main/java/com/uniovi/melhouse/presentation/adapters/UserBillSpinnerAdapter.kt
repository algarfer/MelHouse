package com.uniovi.melhouse.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.uniovi.melhouse.R
import com.uniovi.melhouse.factories.presentation.viewholder.UserBillViewHolderFactory
import com.uniovi.melhouse.presentation.adapters.diffutil.UserBillDiffUtil
import com.uniovi.melhouse.presentation.viewholder.UserBillViewHolder
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class UserBillSpinnerAdapter @AssistedInject constructor(
    @Assisted list: List<Pair<String, Double>>,
    private val userBillViewHolderFactory: UserBillViewHolderFactory,
    @Assisted private val onValueChanged: (String, Double) -> Unit
) : AbstractAdapter<Pair<String, Double>, UserBillViewHolder>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserBillViewHolder {
        return userBillViewHolderFactory.create(
            LayoutInflater
            .from(parent.context)
            .inflate(R.layout.user_bill_binding, parent, false), onValueChanged)
    }

    override fun updateList(newList: List<Pair<String, Double>>) {
        val taskDiff = UserBillDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(taskDiff)
        list = newList
        result.dispatchUpdatesTo(this)
    }
}
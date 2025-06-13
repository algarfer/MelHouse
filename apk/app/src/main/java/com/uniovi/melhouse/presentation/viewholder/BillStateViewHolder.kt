package com.uniovi.melhouse.presentation.viewholder

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uniovi.melhouse.databinding.BillStateLayoutBinding
import com.uniovi.melhouse.factories.presentation.adapter.BillUserAdapterFactory
import com.uniovi.melhouse.presentation.adapters.BillUsersAdapter
import com.uniovi.melhouse.states.BillState
import com.uniovi.melhouse.utils.makeGone
import com.uniovi.melhouse.utils.makeVisible
import com.uniovi.melhouse.viewmodel.BillsFragmentViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

class BillStateViewHolder @AssistedInject constructor(
    @Assisted view: View,
    @Assisted("billHandler") var billHandler: (BillState) -> Unit,
    @Assisted("removeBill") var removeBill: (BillState) -> Unit,
    @Assisted private val viewModel: BillsFragmentViewModel,
    @Inject @JvmField var userBillAdapterFactory: BillUserAdapterFactory,
) : AbstractViewHolder<BillState>(view) {

    private val binding = BillStateLayoutBinding.bind(view)
    private lateinit var billUsersAdapter: BillUsersAdapter
    private lateinit var observer: Observer<Boolean>

    override fun render(item: BillState) {
        binding.textViewConcept.text = item.bill.concept
        binding.textViewAmount.text = buildString {
            append(item.bill.amount.toString())
            append("€")
        }

        billUsersAdapter = userBillAdapterFactory.create(item.mapUserToBillUser)
        binding.rvBillUsers.apply {
            val manager = GridLayoutManager(
                context,
                item.mapUserToBillUser.size,
                RecyclerView.VERTICAL,
                false
            )
            layoutManager = manager
            adapter = billUsersAdapter
        }

        binding.btnDeleteBill.setOnClickListener {
            removeBill(item)
        }

        binding.root.setOnClickListener {
            billHandler(item)
        }

        observer = Observer {
            if (it) {
                binding.btnDeleteBill.makeVisible()
            } else {
                binding.btnDeleteBill.makeGone()
            }
        }

        viewModel.isAdmin.observeForever(observer)

        if (viewModel.userHasToPay(item)) {
            binding.btnPayBill.setOnClickListener {
                viewModel.payActiveUserBill(item)
            }
        } else
            binding.btnPayBill.makeGone()


    }
}

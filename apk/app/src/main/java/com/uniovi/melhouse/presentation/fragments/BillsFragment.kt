package com.uniovi.melhouse.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.model.Bill
import com.uniovi.melhouse.databinding.FragmentBillsBinding
import com.uniovi.melhouse.factories.presentation.adapter.BillStateAdapterFactory
import com.uniovi.melhouse.presentation.adapters.BillsAdapter
import com.uniovi.melhouse.utils.getWarningSnackbar
import com.uniovi.melhouse.viewmodel.BillsFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BillsFragment : Fragment() {
    private lateinit var binding: FragmentBillsBinding
    private val viewModel: BillsFragmentViewModel by viewModels()


    @Inject
    lateinit var userBillStateAdapterFactory: BillStateAdapterFactory
    private lateinit var userBillAdapter: BillsAdapter

    companion object {
        const val TAG = "BillsFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentBillsBinding.inflate(inflater, container, false)

        userBillAdapter = userBillStateAdapterFactory.create(
            listOf(),
            { bs -> handleBill(bs.bill) },
            ({ bs -> deleteBill(bs.bill) }),
            viewModel
        )
        binding.recyclerBills.apply {
            layoutManager = LinearLayoutManager(
                requireContext(), RecyclerView.VERTICAL, false
            )
            adapter = userBillAdapter
        }

        binding.addBillFab.visibility = View.GONE

        binding.addBillFab.setOnClickListener {
            handleBill(null)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clearAllErrors()

        viewModel.billStates.observe(viewLifecycleOwner) { billStates ->
            if (billStates.isNullOrEmpty()) {
                binding.recyclerBills.visibility = View.GONE
                binding.tvNoBills.visibility = View.VISIBLE
            } else {
                binding.recyclerBills.visibility = View.VISIBLE
                binding.tvNoBills.visibility = View.GONE
                userBillAdapter.updateList(billStates)
            }
        }

        viewModel.genericError.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            getWarningSnackbar(requireView(), it).show()
            viewModel.clearGenericError()
        }

        viewModel.isAdmin.observe(viewLifecycleOwner) { isAdmin ->
            binding.addBillFab.visibility = if (isAdmin) View.VISIBLE else View.GONE
        }
    }

    private fun handleBill(bill: Bill?) {
        val fragment = UpsertBillFragment.create(bill)
        parentFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.menuOptionsFragment, fragment, UpsertBillFragment.TAG)
            .addToBackStack(null)
            .commit()
    }

    private fun deleteBill(bill: Bill) {
        viewModel.deleteBill(bill)
    }
}
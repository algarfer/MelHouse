package com.uniovi.melhouse.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.formatter.PercentFormatter
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.model.Bill
import com.uniovi.melhouse.data.model.toBill
import com.uniovi.melhouse.data.model.toJson
import com.uniovi.melhouse.databinding.UpsertBillBinding
import com.uniovi.melhouse.factories.presentation.adapter.UserBillAdapterFactory
import com.uniovi.melhouse.factories.viewmodel.UpsertBillViewModelFactory
import com.uniovi.melhouse.presentation.adapters.UserBillSpinnerAdapter
import com.uniovi.melhouse.presentation.layoutmanagers.CustomLinearLayoutManager
import com.uniovi.melhouse.utils.getColorCompat
import com.uniovi.melhouse.utils.getWarningSnackbar
import com.uniovi.melhouse.viewmodel.UpsertBillViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import javax.inject.Inject

@AndroidEntryPoint
class UpsertBillFragment : Fragment() {
    private var bill: Bill? = null
    private lateinit var userBillAdapter: UserBillSpinnerAdapter

    @Inject
    lateinit var userBillAdapterFactory: UserBillAdapterFactory

    private lateinit var binding: UpsertBillBinding
    private val viewModel: UpsertBillViewModel by viewModels(extrasProducer = {
        defaultViewModelCreationExtras.withCreationCallback<UpsertBillViewModelFactory> { factory ->
            factory.create(bill)
        }
    })

    companion object {
        const val TAG = "UpsertBillFragment"

        private const val BILL_PARAMETER = "bill_state_json"

        fun create(bill: Bill? = null): UpsertBillFragment {
            return UpsertBillFragment().apply {
                arguments = Bundle().apply {
                    putString(BILL_PARAMETER, bill?.toJson())
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = UpsertBillBinding.inflate(inflater, container, false)

        bill = arguments?.getString(BILL_PARAMETER)?.toBill()

        Log.i("concept", "concept: ${bill?.concept}, amount: ${bill?.amount}")

        bill?.let {
            binding.editTextText.setText(it.concept)
            binding.editTextNumberDecimal.setText(it.amount.toString())
        }

        viewModel.genericError.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            getWarningSnackbar(requireView(), it).show()
            viewModel.clearGenericError()
        }

        viewModel.partners.observe(viewLifecycleOwner) { _ ->
            updateRecycler()
        }

        viewModel.billusers.observe(viewLifecycleOwner) { _ ->
            updateRecycler()
        }

        viewModel.shares.observe(viewLifecycleOwner) { _ ->


            updatePieChart()
        }

        binding.btnAdd.setOnClickListener {
            viewModel.concept = binding.editTextText.text.toString()
            if (viewModel.isBillValid()) {
                viewModel.upsertBill()
            }
        }

        viewModel.creationSuccessful.observe(viewLifecycleOwner) {
            if (it)
                requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewModel.amount.observe(viewLifecycleOwner) { _ ->
            updatePieChart()
        }

        binding.editTextNumberDecimal.doOnTextChanged { text, _, _, _ ->
            val amount = text.toString()
            viewModel.setAmount(amount)
        }

        return binding.root
    }

    private fun updateRecycler() {
        viewModel.updateShares()
        Log.i("debug", "va a crearse el adapter, ${viewModel.shares.value}")
        userBillAdapter = userBillAdapterFactory.create(
            viewModel.shares.value ?: emptyList()
        ) { s: String, d: Double ->
            viewModel.changeShare(s, d)
        }

        binding.recyclerDynamicList.apply {
            val manager = CustomLinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            manager.setScrollEnabled(false)
            layoutManager = manager
            adapter = userBillAdapter
        }
    }

    private fun updatePieChart() {
        val data = viewModel.getPieChartData(requireContext())
        binding.pcTaskAssignee.apply {
            this.data = data
            description.isEnabled = false
            legend.isEnabled = false
            setHoleColor(requireContext().getColorCompat(R.color.background))
            setCenterTextColor(requireContext().getColorCompat(R.color.on_background))
            setCenterTextSize(24f)
            centerText = (viewModel.amount.value ?: 0.0).toString()
            setUsePercentValues(true)
            data.setValueFormatter(PercentFormatter(this))
            data.setValueTextSize(16f)
            data.setValueTextColor(requireContext().getColorCompat(R.color.chart_text_black))
            setEntryLabelTextSize(16f)
            setEntryLabelColor(requireContext().getColorCompat(R.color.chart_text_black))
            isHighlightPerTapEnabled = false
            animateY(800)
        }
        binding.pcTaskAssignee.invalidate()
    }
}
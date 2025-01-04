package com.uniovi.melhouse.presentation.fragments

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.uniovi.melhouse.R
import com.uniovi.melhouse.databinding.FragmentFlatBinding
import com.uniovi.melhouse.factories.presentation.adapter.PartnersAdapterFactory
import com.uniovi.melhouse.presentation.activities.MenuActivity
import com.uniovi.melhouse.presentation.adapters.PartnersAdapter
import com.uniovi.melhouse.presentation.layoutmanagers.CustomLinearLayoutManager
import com.uniovi.melhouse.utils.getColorCompat
import com.uniovi.melhouse.utils.getWarningSnackbar
import com.uniovi.melhouse.utils.makeGone
import com.uniovi.melhouse.utils.makeVisible
import com.uniovi.melhouse.utils.toAsigneesPieChart
import com.uniovi.melhouse.utils.toStatusBarChartData
import com.uniovi.melhouse.viewmodel.FlatFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FlatFragment : Fragment() {

    private lateinit var binding: FragmentFlatBinding
    private val viewModel: FlatFragmentViewModel by viewModels()
    private lateinit var partnersAdapter: PartnersAdapter
    @Inject lateinit var partnersAdapterFactory: PartnersAdapterFactory

    companion object {
        const val TAG = "FlatFragment"
    }

    override fun onResume() {
        super.onResume()
        viewModel.onCreate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.flat.observe(this) { flat ->
            if(flat == null) {
                binding.btnClipboard.makeGone()
                return@observe
            }

            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(flat.invitationCode, BarcodeFormat.QR_CODE, 512, 512)

            binding.apply {
                tvNameFlat.text = flat.name
                ivQRCode.setImageBitmap(bitmap)
                tvQRCode.text = getString(R.string.flat_invitation_code, flat.invitationCode)
            }

            binding.btnEdit.setOnClickListener {
                val fragment = UpsertFlatFragment.create(flat)
                parentFragmentManager
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.menuOptionsFragment, fragment, UpsertFlatFragment.TAG)
                    .addToBackStack(null)
                    .commit()
            }

            binding.btnLeave.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(resources.getString(R.string.flat_leave_dialog_title))
                    .setMessage(resources.getString(R.string.flat_leave_dialog_supporting_text, flat.name))
                    .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
                    .setPositiveButton(resources.getString(R.string.continuar)) { _, _ ->
                        viewModel.leave()
                    }
                    .show()
            }

            binding.btnClipboard.setOnClickListener {
                val clipboard = getSystemService(requireContext(), ClipboardManager::class.java)
                val clip = ClipData.newPlainText(label.toString(), flat.invitationCode)
                clipboard?.setPrimaryClip(clip)
                Toast.makeText(requireContext(), R.string.flat_invitation_code_copied, Toast.LENGTH_SHORT).show()
            }

            binding.btnClipboard.makeVisible()
        }

        viewModel.hasLeaved.observe(this) {
            if(!it) return@observe
            val intent = Intent(requireContext(), MenuActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        viewModel.isAdmin.observe(this) {
            it?.let {
                if(it)
                    binding.btnEdit.makeVisible()
                else
                    binding.btnEdit.makeGone()
            }
        }

        viewModel.partners.observe(this) {
            it?.let {
                partnersAdapter.updateList(it)

                val data = it.toAsigneesPieChart(requireContext())

                binding.pcTaskAssignee.apply {
                    this.data = data
                    description.isEnabled = false
                    legend.isEnabled = false
                    setHoleColor(requireContext().getColorCompat(R.color.background))
                    setCenterTextColor(requireContext().getColorCompat(R.color.on_background))
                    setCenterTextSize(24f)
                    centerText = getString(R.string.chart_pie_asignees)
                    setUsePercentValues(true)
                    data.setValueFormatter(PercentFormatter(this))
                    data.setValueTextSize(16f)
                    data.setValueTextColor(requireContext().getColorCompat(R.color.chart_text_black))
                    setEntryLabelTextSize(16f)
                    setEntryLabelColor(requireContext().getColorCompat(R.color.chart_text_black))
                    isHighlightPerTapEnabled = false
                    animateY(800)
                }
                binding.pcTaskAssignee.invalidate() //refresh
            }
        }

        viewModel.tasks.observe(this) {
            if(it.isEmpty()) return@observe

            val (data, customLegend) = it.toStatusBarChartData(requireContext())

            binding.bcTaskStatus.apply {
                this.data = data
                description.isEnabled = false
                axisLeft.setDrawLabels(false)
                axisLeft.setDrawGridLines(false)
                axisLeft.setDrawAxisLine(false)
                axisRight.granularity = (it.size / 10).toFloat()
                xAxis.setDrawLabels(false)
                xAxis.setDrawGridLines(false)
                isHighlightPerTapEnabled = false
                setFitBars(true)
                animateY(800)
                setTouchEnabled(false)
                legend.setCustom(customLegend)
                legend.orientation = Legend.LegendOrientation.VERTICAL
                legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                legend.setDrawInside(true)
                legend.isWordWrapEnabled = true
            }
            binding.bcTaskStatus.invalidate() // refresh
        }

        viewModel.genericError.observe(this) {
            if(it == null) return@observe

            getWarningSnackbar(requireView(), it).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFlatBinding.inflate(inflater, container, false)

        partnersAdapter = partnersAdapterFactory.create(listOf(), viewModel)

        binding.rvFlatMembers.apply {
            val manager = CustomLinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            manager.setScrollEnabled(false)
            layoutManager = manager
            adapter = partnersAdapter
        }
        return binding.root
    }
}
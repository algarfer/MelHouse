package com.uniovi.melhouse.presentation.fragments

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.model.getFullAddress
import com.uniovi.melhouse.databinding.FragmentFlatBinding
import com.uniovi.melhouse.factories.presentation.adapter.PartnersAdapterFactory
import com.uniovi.melhouse.presentation.adapters.PartnersAdapter
import com.uniovi.melhouse.presentation.layoutmanagers.CustomLinearLayoutManager
import com.uniovi.melhouse.utils.makeGone
import com.uniovi.melhouse.utils.makeVisible
import com.uniovi.melhouse.viewmodel.FlatFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FlatFragment @Inject constructor() : Fragment() {

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
                tvFlatName.text = flat.name
                tvFlatAddress.text = flat.getFullAddress()
                ivQRCode.setImageBitmap(bitmap)
                tvQRCode.text = getString(R.string.flat_invitation_code, flat.invitationCode)
            }

            binding.btnClipboard.makeVisible()
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
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFlatBinding.inflate(inflater, container, false)
        binding.btnClipboard.setOnClickListener {
            val clipboard = getSystemService(requireContext(), ClipboardManager::class.java)
            val clip = ClipData.newPlainText(label.toString(), viewModel.flat.value?.invitationCode)
            clipboard?.setPrimaryClip(clip)
        }

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
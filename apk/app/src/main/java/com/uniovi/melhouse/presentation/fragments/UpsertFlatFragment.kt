package com.uniovi.melhouse.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.uniovi.melhouse.R
import com.uniovi.melhouse.databinding.FragmentUpsertFlatBinding
import com.uniovi.melhouse.viewmodel.UpsertFlatViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UpsertFlatFragment @Inject constructor() : Fragment() {

    private lateinit var binding: FragmentUpsertFlatBinding
    private val viewModel: UpsertFlatViewModel by viewModels()

    companion object {
        const val TAG = "CreateFlatFragment"
    }

    private fun setupListeners() {
        binding.btnContinuar.setOnClickListener {
            viewModel.createFlat(
                binding.etFlatName.text.toString(),
                binding.etAddress.text.toString(),
                binding.etFloor.text.toString(),
                binding.etDoor.text.toString(),
                binding.etFlatStair.text.toString(),
                binding.etFlatBedrooms.text.toString(),
                requireContext()
            )
        }
        binding.btnCancelar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupObservers() {
        viewModel.nameError.observe(viewLifecycleOwner) {
            binding.flatNameLayout.error = it
        }
        viewModel.addressError.observe(viewLifecycleOwner) {
            binding.flatAddressLayout.error = it
        }
        viewModel.creationSuccessful.observe(viewLifecycleOwner) {
            if(!it) return@observe
            parentFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.menuOptionsFragment, MenuFragment(), MenuFragment.TAG)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpsertFlatBinding.bind(view)

        setupListeners()
        setupObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return layoutInflater.inflate(R.layout.fragment_upsert_flat, container, false)
    }

}
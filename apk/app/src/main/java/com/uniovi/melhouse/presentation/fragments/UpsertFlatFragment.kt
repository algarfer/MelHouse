package com.uniovi.melhouse.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.uniovi.melhouse.data.model.Flat
import com.uniovi.melhouse.data.model.toFlat
import com.uniovi.melhouse.data.model.toJson
import com.uniovi.melhouse.databinding.FragmentUpsertFlatBinding
import com.uniovi.melhouse.factories.viewmodel.UpsertFlatViewModelFactory
import com.uniovi.melhouse.utils.getWarningSnackbar
import com.uniovi.melhouse.utils.toEditable
import com.uniovi.melhouse.viewmodel.UpsertFlatViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback

@AndroidEntryPoint
class UpsertFlatFragment : Fragment() {

    private lateinit var binding: FragmentUpsertFlatBinding
    private val viewModel: UpsertFlatViewModel by viewModels(extrasProducer = {
        defaultViewModelCreationExtras.withCreationCallback<UpsertFlatViewModelFactory> { factory ->
            factory.create(flat)
        }
    })
    private var flat: Flat? = null

    companion object {
        const val TAG = "UpsertFlatFragment"
        private const val FLAT_PARAMETER = "flat_json"

        fun create(flat: Flat? = null) : UpsertFlatFragment {
            return UpsertFlatFragment().apply {
                arguments = Bundle().apply {
                    putString(FLAT_PARAMETER, flat?.toJson())
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnContinuar.setOnClickListener {
            viewModel.upsertFlat(requireContext())
        }
        binding.btnCancelar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.etFlatName.doOnTextChanged { text, _, _, _ ->
            viewModel.name = text.toString()
        }
        binding.etAddress.doOnTextChanged { text, _, _, _ ->
            viewModel.address = text.toString()
        }
        binding.etFloor.doOnTextChanged { text, _, _, _ ->
            viewModel.floor = text.toString().toIntOrNull()
        }
        binding.etDoor.doOnTextChanged { text, _, _, _ ->
            viewModel.door = text.toString()
        }
        binding.etFlatStair.doOnTextChanged { text, _, _, _ ->
            viewModel.stair = text.toString()
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
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        viewModel.genericError.observe(viewLifecycleOwner) {
            if(it == null) return@observe
            getWarningSnackbar(requireView(), it).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()

        val flat = flat
        if(flat != null) {
            binding.etFlatName.text = flat.name.toEditable()
            binding.etAddress.text = flat.address.toEditable()
            binding.etFloor.text = flat.floor?.toString()?.toEditable()
            binding.etDoor.text = flat.door?.toEditable()
            binding.etFlatStair.text = flat.stair?.toEditable()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpsertFlatBinding.inflate(inflater, container, false)
        flat = arguments?.getString(FLAT_PARAMETER)?.toFlat()
        return binding.root
    }
}
package com.uniovi.melhouse.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.uniovi.melhouse.databinding.FragmentNoFlatBinding
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Activity
import androidx.fragment.app.viewModels
import com.uniovi.melhouse.R
import com.uniovi.melhouse.viewmodel.NoFlatFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NoFlatFragment @Inject constructor() : Fragment() {

    private lateinit var binding: FragmentNoFlatBinding
    private val viewModel: NoFlatFragmentViewModel by viewModels()

    companion object {
        const val TAG = "NoFlatFragment"
    }

    private val qrScanLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val scanResult = IntentIntegrator.parseActivityResult(result.resultCode, intent)
            if (scanResult != null) {
                if (scanResult.contents != null) {
                    viewModel.joinFlat(scanResult.contents, requireContext())
                } else {
                    Toast.makeText(requireContext(), R.string.flat_qr_cancelled, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), R.string.flat_qr_not_read, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoFlatBinding.inflate(inflater, container, false)
        setupObservers()
        binding.btnJoinFlat.setOnClickListener {
            viewModel.joinFlat(binding.etFlatCode.text.toString(), requireContext())
        }
        binding.btnJoinQRFlat.setOnClickListener {
            initFlatQRScan()
        }
        binding.btnCreateFlat.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.menuOptionsFragment, UpsertFlatFragment(), UpsertFlatFragment.TAG)
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

    private fun setupObservers() {
        viewModel.flatCodeError.observe(viewLifecycleOwner) { errorMessage ->
            binding.flatCodeLayout.error = errorMessage
        }

        viewModel.joinFlatSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), R.string.flat_welcome, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.snackBarMsg.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initFlatQRScan() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setPrompt(getString(R.string.flat_qr_join))
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        qrScanLauncher.launch(integrator.createScanIntent())
    }
}

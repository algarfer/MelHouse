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
import com.uniovi.melhouse.R

class NoFlatFragment : Fragment() {

    private lateinit var binding: FragmentNoFlatBinding

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
                    Toast.makeText(requireContext(), scanResult.contents, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoFlatBinding.inflate(inflater, container, false)
        binding.btnJoinQRFlat.setOnClickListener {
            initFlatQRScan()
        }
        binding.btnCreateFlat.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.menuOptionsFragment, CreateFlatFragment(), CreateFlatFragment.TAG)
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

    private fun initFlatQRScan() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setPrompt("Scan a QR code")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(true)
        qrScanLauncher.launch(integrator.createScanIntent())
    }
}

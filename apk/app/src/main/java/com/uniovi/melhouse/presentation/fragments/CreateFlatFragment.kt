package com.uniovi.melhouse.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uniovi.melhouse.databinding.FragmentCreateFlatBinding

class CreateFlatFragment : Fragment() {

    private lateinit var binding: FragmentCreateFlatBinding

    companion object {
        const val TAG = "CreateFlatFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateFlatBinding.inflate(inflater, container, false)
        binding.btnCancelar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        return binding.root
    }

}
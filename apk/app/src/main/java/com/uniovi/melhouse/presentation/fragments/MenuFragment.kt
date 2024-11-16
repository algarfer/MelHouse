package com.uniovi.melhouse.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uniovi.melhouse.R
import com.uniovi.melhouse.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnFlats.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.menuOptionsFragment, NoFlatFragment(), NoFlatFragment.TAG)
                .addToBackStack(null)
                .commit()
        }

        return view
    }

}
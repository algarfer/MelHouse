package com.uniovi.melhouse.presentation.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.uniovi.melhouse.R
import com.uniovi.melhouse.databinding.FragmentSettingsBinding
import com.uniovi.melhouse.presentation.activities.NotRegisteredActivity
import com.uniovi.melhouse.viewmodel.SettingViewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingViewModel by viewModels()

    companion object {
        const val TAG = "SettingsFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.isLogged.observe(this) {
            if(it) return@observe

            val activity = requireActivity()
            activity.startActivity(Intent(activity, NotRegisteredActivity::class.java))
            activity.finish()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_settings, container, false)
    }

}
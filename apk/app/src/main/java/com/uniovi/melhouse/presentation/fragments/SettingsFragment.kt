package com.uniovi.melhouse.presentation.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.uniovi.melhouse.R
import com.uniovi.melhouse.databinding.FragmentSettingsBinding
import com.uniovi.melhouse.presentation.activities.NotRegisteredActivity
import com.uniovi.melhouse.utils.getWarningSnackbar
import com.uniovi.melhouse.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    companion object {
        const val TAG = "SettingsFragment"
    }

    override fun onResume() {
        super.onResume()

        viewModel.goToStart.observe(this) {
            if(!it) return@observe
            val intent = Intent(requireContext(), NotRegisteredActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        viewModel.genericError.observe(this) {
            if (it == null) return@observe

            getWarningSnackbar(requireView(), it).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.switchDarkMode.isChecked = viewModel.getDarkMode()
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setDarkMode(isChecked)
        }
        binding.btnDeleteUserProfile.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.settings_delete_data_dialog_title))
                .setMessage(resources.getString(R.string.settings_delete_data_dialog_supporting_text))
                .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(resources.getString(R.string.continuar)) { _, _ ->
                    viewModel.deleteUserProfile()
                }
                .show()
        }

        return binding.root
    }

}
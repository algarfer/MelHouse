package com.uniovi.melhouse.presentation.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uniovi.melhouse.databinding.ActivitySignupBinding
import com.uniovi.melhouse.utils.getWarningSnackbar
import com.uniovi.melhouse.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : AbstractActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val viewModel: SignUpViewModel by viewModels()

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnSignup.setOnClickListener {
            viewModel.signup(
                binding.etName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etConfirmPassword.text.toString()
            )
        }
    }

    private fun setupObservers() {
        viewModel.clearAllErrors()

        viewModel.nameError.observe(this) {
            binding.nameLayout.error = it
        }

        viewModel.emailError.observe(this) {
            binding.emailLayout.error = it
        }

        viewModel.passwordError.observe(this) {
            binding.passwordLayout.error = it
        }

        viewModel.password2Error.observe(this) {
            binding.confirmPasswordLayout.error = it
        }

        viewModel.signupSuccessfull.observe(this) {
            if (!it) return@observe
            startActivity(Intent(this, MenuActivity::class.java))

            setResult(RESULT_OK)
            finish()
        }

        viewModel.genericError.observe(this) {
            if (it.isNullOrEmpty()) return@observe
            getWarningSnackbar(binding.root, it).show()
            viewModel.clearGenericError()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
        setupObservers()
    }
}
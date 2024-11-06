package com.uniovi.melhouse.presentation.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uniovi.melhouse.databinding.ActivitySignupBinding
import com.uniovi.melhouse.presentation.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val viewModel: SignUpViewModel by viewModels()

    private fun setupListeners() {
        binding.btnReturn.setOnClickListener {
            finish()
        }

        binding.btnSignup.setOnClickListener {
            viewModel.signup(
                binding.etName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etConfirmPassword.text.toString(),
                this
            )
        }
    }

    private fun setupObservers() {
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
            if(!it) return@observe
            startActivity(Intent(this, MenuActivity::class.java))

            setResult(RESULT_OK)
            finish()
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
package com.uniovi.melhouse.presentation.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uniovi.melhouse.databinding.ActivityLoginBinding
import com.uniovi.melhouse.utils.getWarningSnackbar
import com.uniovi.melhouse.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AbstractActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
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

        viewModel.loginSuccessfull.observe(this) {
            if(!it) return@observe
            startActivity(Intent(this, MenuActivity::class.java))

            setResult(RESULT_OK)
            finish()
        }

        viewModel.snackBarMsg.observe(this) {
            if(it.isNullOrEmpty()) return@observe
            getWarningSnackbar(binding.root, it).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
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
package com.uniovi.melhouse.presentation.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uniovi.melhouse.R
import com.uniovi.melhouse.databinding.ActivityNotRegisteredBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotRegisteredActivity : AbstractActivity() {

    private lateinit var binding: ActivityNotRegisteredBinding

    private fun setup() {
        val launcher =
            registerForActivityResult((ActivityResultContracts.StartActivityForResult())) {
                when (it.resultCode) {
                    RESULT_OK -> {
                        finish()
                    }
                }
            }

        binding = ActivityNotRegisteredBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            launcher.launch(Intent(this, LoginActivity::class.java))
        }
        binding.signUpButton.setOnClickListener {
            launcher.launch(Intent(this, SignupActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_not_registered)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setup()
    }

}
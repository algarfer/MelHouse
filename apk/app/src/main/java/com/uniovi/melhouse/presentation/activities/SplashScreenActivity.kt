package com.uniovi.melhouse.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uniovi.melhouse.R
import com.uniovi.melhouse.network.InternetConnectionObserver
import com.uniovi.melhouse.viewmodel.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenActivity @Inject constructor() : AbstractActivity() {
    private val viewModel: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Wait for the app to start
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (viewModel.isReady.value == true) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )

        // Determine the next activity to start
        viewModel.isReady.observe(this) {
            if(!it) return@observe

            if(!InternetConnectionObserver.hasConnection()) {
                showConnectionLostDialog()
                return@observe
            }

            val intent = if(viewModel.isLogged) {
                Intent(this, MenuActivity::class.java)
            } else {
                Intent(this, NotRegisteredActivity::class.java)
            }

            startActivity(intent)
            finish()
        }
        viewModel.initApp()
    }
}
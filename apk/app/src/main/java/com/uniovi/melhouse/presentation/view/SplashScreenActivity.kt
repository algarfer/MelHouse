package com.uniovi.melhouse.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uniovi.melhouse.R
import com.uniovi.melhouse.presentation.viewmodel.SplashScreenViewModel

class SplashScreenActivity : AppCompatActivity() {
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

            val intent = if(viewModel.isLogged) {
                // TODO - Change to MenuActivity when implemented
                // intent = Intent(this, MenuActivity::class.java)
                Intent(this, CalendarViewActivity::class.java)
            } else {
                Intent(this, NotRegisteredActivity::class.java)
            }

            startActivity(intent)
            finish()
        }
        viewModel.initApp()
    }
}
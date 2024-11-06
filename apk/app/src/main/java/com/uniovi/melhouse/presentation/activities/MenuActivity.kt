package com.uniovi.melhouse.presentation.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.uniovi.melhouse.R
import com.uniovi.melhouse.databinding.ActivityMenuBinding
import dagger.hilt.android.AndroidEntryPoint
import com.uniovi.melhouse.preference.Prefs
import com.uniovi.melhouse.presentation.fragments.MenuFragment
import com.uniovi.melhouse.presentation.fragments.SettingsFragment

@AndroidEntryPoint
class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private var androidOsBarColor: Int? = null

    private fun setup(){
        binding.menuNavigationBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuFragment -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.menuOptionsFragment, MenuFragment())
                    }
                    true
                }
                R.id.calendarFragment -> {
                    val intent = Intent(this, CalendarViewActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.settingsFragment -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.menuOptionsFragment, SettingsFragment())
                    }
                    true
                }
                else -> false
            }
        }
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.menuOptionsFragment, MenuFragment())
        }
    }

    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMenuBinding.inflate(layoutInflater)
        binding.ivProfile.tvProfile.text = Prefs.getEmail().substring(0,1).uppercase()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        androidOsBarColor = window.navigationBarColor
        // Review - This may not work with dark mode
        window.navigationBarColor = getColor(com.google.android.material.R.color.m3_ref_palette_neutral94)

        setup()
    }

    override fun onPause() {
        super.onPause()
        window.navigationBarColor = androidOsBarColor!!
    }
}
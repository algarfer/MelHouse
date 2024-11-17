package com.uniovi.melhouse.presentation.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
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
    private lateinit var drawerLayout: DrawerLayout

    private fun setup(){
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
        setContentView(binding.root)

        val headerView = binding.navigationView.getHeaderView(0)
        var userInitial = Prefs.getEmail().substring(0,1).uppercase()
        headerView.findViewById<TextView>(R.id.tvProfile).text = userInitial
        headerView.findViewById<TextView>(R.id.tvUsername).text = Prefs.getEmail()

        drawerLayout = binding.drawerLayout
        binding.btnMenuLines.setOnClickListener {
            if (drawerLayout.isDrawerOpen(binding.navigationView)) {
                drawerLayout.closeDrawer(binding.navigationView)
            } else {
                drawerLayout.openDrawer(binding.navigationView)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setup()
    }

}
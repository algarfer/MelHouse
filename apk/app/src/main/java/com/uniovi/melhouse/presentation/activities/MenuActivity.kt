package com.uniovi.melhouse.presentation.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.navigation.NavigationView
import com.uniovi.melhouse.R
import com.uniovi.melhouse.databinding.ActivityMenuBinding
import dagger.hilt.android.AndroidEntryPoint
import com.uniovi.melhouse.presentation.fragments.MenuFragment
import com.uniovi.melhouse.presentation.fragments.NoFlatFragment
import com.uniovi.melhouse.presentation.fragments.SettingsFragment
import com.uniovi.melhouse.viewmodel.MenuViewModel

@AndroidEntryPoint
class MenuActivity : AbstractActivity(), NavigationView.OnNavigationItemSelectedListener  {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var drawerLayout: DrawerLayout
    private val viewModel: MenuViewModel by viewModels()

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

        viewModel.onCreate()

        val headerView = binding.navigationView.getHeaderView(0)

        viewModel.user.observe(this) {
            if (it == null) return@observe
            headerView.findViewById<TextView>(R.id.tvProfile).text = it.name.substring(0,1).uppercase()
            headerView.findViewById<TextView>(R.id.tvUsername).text = it.email
        }

        observeFlat()

        drawerLayout = binding.drawerLayout
        binding.btnMenuLines.setOnClickListener {
            if (drawerLayout.isDrawerOpen(binding.navigationView)) {
                drawerLayout.closeDrawer(binding.navigationView)
            } else {
                drawerLayout.openDrawer(binding.navigationView)
            }
        }

        binding.navigationView.setNavigationItemSelectedListener(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setup()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment: Fragment? = when (item.itemId) {
            R.id.navigation_home -> {
                observeFlat()
                return true
            }
            R.id.navigation_calendar -> {
                val intent = Intent(this, CalendarViewActivity::class.java)
                startActivity(intent)
                drawerLayout.closeDrawer(binding.navigationView)
                return true
            }
//            R.id.navigation_flat -> FlatFragment()
//            R.id.navigation_account -> AccountFragment()
            R.id.navigation_settings -> SettingsFragment()
            R.id.navigation_logout -> {
                viewModel.logout()
                startActivity(Intent(this, NotRegisteredActivity::class.java))
                finish()
                return true
            }
            else -> null
        }

        if (fragment != null) {
            loadFragment(fragment)
            return true
        }

        return false
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.menuOptionsFragment, fragment)
        }
        drawerLayout.closeDrawer(binding.navigationView)
    }

    private fun observeFlat() {
        viewModel.flat.observe(this) {
            if (it == null) {
                loadFragment(NoFlatFragment())
                return@observe
            }
            loadFragment(MenuFragment())
        }
    }

}
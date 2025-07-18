package com.uniovi.melhouse.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.navigation.NavigationView
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.model.getInitials
import com.uniovi.melhouse.databinding.ActivityMenuBinding
import com.uniovi.melhouse.presentation.fragments.BillsFragment
import com.uniovi.melhouse.presentation.fragments.FlatFragment
import com.uniovi.melhouse.presentation.fragments.MenuFragment
import com.uniovi.melhouse.presentation.fragments.NoFlatFragment
import com.uniovi.melhouse.presentation.fragments.SettingsFragment
import com.uniovi.melhouse.utils.getWarningSnackbar
import com.uniovi.melhouse.viewmodel.DrawerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuActivity : AbstractActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var drawerLayout: DrawerLayout
    private val viewModel: DrawerViewModel by viewModels()
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    private fun setup() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.menuOptionsFragment, MenuFragment())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.onCreate()
        val headerView = binding.navigationView.getHeaderView(0)
        viewModel.clearAllErrors()

        viewModel.user.observe(this) {
            if (it == null) return@observe
            headerView.findViewById<TextView>(R.id.tvProfile).text = it.getInitials()
            headerView.findViewById<TextView>(R.id.tvUsername).text = it.email
        }

        viewModel.isLogged.observe(this) {
            if (it) return@observe

            startActivity(Intent(this, NotRegisteredActivity::class.java))
            finish()
        }

        viewModel.genericError.observe(this) {
            if (it == null) return@observe
            getWarningSnackbar(headerView, it).show()
            viewModel.clearGenericError()
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

        notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)

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

            R.id.navigation_flat -> FlatFragment()
            R.id.navigation_settings -> SettingsFragment()
            R.id.navigation_logout -> {
                viewModel.logout()
                return true
            }

            R.id.navigation_bills -> BillsFragment()
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
                binding.navigationView.menu.findItem(R.id.navigation_calendar).isVisible = false
                binding.navigationView.menu.findItem(R.id.navigation_flat).isVisible = false
                binding.navigationView.menu.findItem(R.id.navigation_bills).isVisible = false
                loadFragment(NoFlatFragment())
                return@observe
            }
            binding.navigationView.menu.findItem(R.id.navigation_calendar).isVisible = true
            binding.navigationView.menu.findItem(R.id.navigation_flat).isVisible = true
            binding.navigationView.menu.findItem(R.id.navigation_bills).isVisible = true
            loadFragment(MenuFragment())
        }
    }

}
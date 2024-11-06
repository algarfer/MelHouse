package com.uniovi.melhouse.presentation.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import androidx.fragment.app.add
import com.uniovi.melhouse.R
import com.uniovi.melhouse.databinding.ActivityCalendarViewBinding
import com.uniovi.melhouse.presentation.fragments.CalendarFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarViewBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.calendar_main_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<CalendarFragment>(R.id.calendar_fragment_container)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> onBackPressedDispatcher.onBackPressed().let { true }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
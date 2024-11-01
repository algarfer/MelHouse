package com.uniovi.melhouse.presentation.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.uniovi.melhouse.R

interface HasToolbar {
    val toolbar: Toolbar?
}

interface HasBackButton

abstract class BaseFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (this is MenuProvider) {
            requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.CREATED)
        }
    }

    override fun onStart() {
        super.onStart()
        if (this is HasToolbar) {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

            toolbar?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary))
            toolbar?.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.on_primary))

            (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        }

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        if (this is HasBackButton) {
            actionBar?.setDisplayHomeAsUpEnabled(true)
            val backArrow = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_chevron_left_24)
            backArrow?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.on_primary), PorterDuff.Mode.SRC_ATOP)
            actionBar?.setHomeAsUpIndicator(backArrow)
        } else {
            actionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }

    override fun onStop() {
        super.onStop()

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        if (this is HasBackButton) {
            actionBar?.title = "Title"
        }
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }
}

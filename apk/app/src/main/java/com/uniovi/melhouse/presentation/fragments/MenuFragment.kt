package com.uniovi.melhouse.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uniovi.melhouse.databinding.FragmentMenuBinding
import com.uniovi.melhouse.factories.presentation.adapter.NextTasksAdapterFactory
import com.uniovi.melhouse.presentation.adapters.NextTasksAdapter
import com.uniovi.melhouse.viewmodel.MenuFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding
    private val viewModel: MenuFragmentViewModel by viewModels()
    private lateinit var todayTasksAdapter: NextTasksAdapter
    private lateinit var tomorrowTasksAdapter: NextTasksAdapter
    @Inject lateinit var nextTasksAdapterFactory: NextTasksAdapterFactory

    companion object {
        const val TAG = "MenuFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)

        todayTasksAdapter = nextTasksAdapterFactory.create(listOf())
        binding.recyclerTodayTasks.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = todayTasksAdapter
        }

        tomorrowTasksAdapter = nextTasksAdapterFactory.create(listOf())
        binding.recyclerTomorrowTasks.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = tomorrowTasksAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.todayTasks.observe(viewLifecycleOwner) { tasks ->
            if (tasks.isNullOrEmpty()) {
                binding.recyclerTodayTasks.visibility = View.GONE
                binding.tvNoTodayTasks.visibility = View.VISIBLE
            } else {
                binding.recyclerTodayTasks.visibility = View.VISIBLE
                binding.tvNoTodayTasks.visibility = View.GONE
                todayTasksAdapter.updateList(tasks)
            }
        }

        viewModel.tomorrowTasks.observe(viewLifecycleOwner) { tasks ->
            if (tasks.isNullOrEmpty()) {
                binding.recyclerTomorrowTasks.visibility = View.GONE
                binding.tvNoTomorrowTasks.visibility = View.VISIBLE
            } else {
                binding.recyclerTomorrowTasks.visibility = View.VISIBLE
                binding.tvNoTomorrowTasks.visibility = View.GONE
                tomorrowTasksAdapter.updateList(tasks)
            }
        }

        viewModel.loadTasks()
    }

}
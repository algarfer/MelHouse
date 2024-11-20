package com.uniovi.melhouse.presentation.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.databinding.TaskDetailsBottomSheetLayoutBinding
import com.uniovi.melhouse.utils.getColor
import com.uniovi.melhouse.utils.getDatesString
import com.uniovi.melhouse.utils.makeGone
import com.uniovi.melhouse.utils.showWipToast
import com.uniovi.melhouse.viewmodel.TaskBottomSheetViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskBottomSheetDialog(val task: Task, private val updateCalendarViewModel: () -> Unit, private val updateTasksViewHolder: () -> Unit) : BottomSheetDialogFragment() {
    private lateinit var binding : TaskDetailsBottomSheetLayoutBinding
    private val viewModel: TaskBottomSheetViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = TaskDetailsBottomSheetLayoutBinding.inflate(inflater, container, false)

        viewModel.onCreateView(task, updateCalendarViewModel, updateTasksViewHolder) { dismiss() }

        viewModel.task.observe(this){
            updateTask()
        }

        binding.btnDeleteTask.setOnClickListener {
            showConfirmDialog()
        }

        binding.btnEditTask.setOnClickListener {
            dismiss()

            val fragment = UpsertTaskFragment(viewModel.task.value!!)
            parentFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true) //
                .replace(R.id.calendar_fragment_container, fragment, UpsertTaskFragment.TAG)
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    private fun showConfirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.task_elimination_dialog_title))
            .setMessage(resources.getString(R.string.task_elimination_dialog_supporting_text, task.name))
            .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.continuar)) { _, _ ->
                viewModel.deleteTask()
            }
            .show()
    }

    private fun updateTask() {
        // Update name
        binding.tvTaskTitle.text = viewModel.task.value!!.name

        updatePriority()

        updateStatus()

        // Update description
        binding.tvTaskDescription.text = viewModel.task.value!!.description.orEmpty()

        updateTaskDays()
    }

    private fun updateTaskDays() {
        if (viewModel.task.value!!.endDate == null) {
            binding.taskDaysLayout.makeGone()
        } else {
            binding.tvTaskDates.text = viewModel.task.value!!.getDatesString()
        }
    }

    private fun updateStatus() {
        val status = viewModel.task.value!!.status

        if (status == null) {
            binding.badgeTaskStatus.root.makeGone()
        } else {
            binding.badgeTaskStatus.tvStatus.text = status.getString(requireContext())
            binding.badgeTaskStatus.ivStatus.setColorFilter(status.getColor(requireContext()))
        }
    }

    private fun updatePriority() {
        val priority = viewModel.task.value!!.priority

        if (priority == null) {
            binding.badgeTaskPriority.root.makeGone()
        } else {
            binding.badgeTaskPriority.tvStatus.text = priority.getString(requireContext())
            binding.badgeTaskPriority.ivStatus.setColorFilter(priority.getColor(requireContext()))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog?.setOnShowListener { it ->
            val d = it as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                // TODO - Configure to expand to max height
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    companion object {
        const val TAG = "TaskBottomSheetDialog"
    }
}
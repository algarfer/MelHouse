package com.uniovi.melhouse.presentation.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.databinding.TaskDetailsBottomSheetLayoutBinding
import com.uniovi.melhouse.presentation.utils.getColor
import com.uniovi.melhouse.presentation.utils.getDatesString
import com.uniovi.melhouse.presentation.utils.getString
import com.uniovi.melhouse.presentation.utils.makeGone
import com.uniovi.melhouse.presentation.utils.showWipToast

class TaskBottomSheetDialog(val task: Task) : BottomSheetDialogFragment() {
    private lateinit var binding : TaskDetailsBottomSheetLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = TaskDetailsBottomSheetLayoutBinding.inflate(inflater, container, false)

        binding.tvTaskTitle.text = task.name

        val priority = task.priority
        val status = task.status

        if(priority == null) {
            binding.badgeTaskPriority.root.makeGone()
        } else {
            binding.badgeTaskPriority.tvStatus.text = priority.getString(requireContext())
            binding.badgeTaskPriority.ivStatus.setColorFilter(priority.getColor(requireContext()))
        }

        if(status == null) {
            binding.badgeTaskStatus.root.makeGone()
        } else {
            binding.badgeTaskStatus.tvStatus.text = status.getString(requireContext())
            binding.badgeTaskStatus.ivStatus.setColorFilter(status.getColor(requireContext()))
        }

        binding.tvTaskDescription.text = task.description.orEmpty()

        if(task.endDate == null) {
            binding.taskDaysLayout.makeGone()
        } else {
            binding.tvTaskDates.text = task.getDatesString()
        }

        binding.btnEditTask.setOnClickListener {
            // TODO - Edit Task View
            showWipToast(requireContext())
        }

        return binding.root
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
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
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.databinding.TaskAsigneeDisplayLayoutBinding
import com.uniovi.melhouse.databinding.TaskDetailsBottomSheetLayoutBinding
import com.uniovi.melhouse.utils.getColor
import com.uniovi.melhouse.utils.getDatesString
import com.uniovi.melhouse.utils.makeGone
import com.uniovi.melhouse.utils.makeVisible
import com.uniovi.melhouse.utils.showWipToast
import com.uniovi.melhouse.viewmodel.TaskBottomSheetViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TaskBottomSheetDialog @Inject constructor() : BottomSheetDialogFragment() {
    private lateinit var binding : TaskDetailsBottomSheetLayoutBinding
    private val viewModel: TaskBottomSheetViewModel by viewModels()

    private var task: Task? = null

    fun setTask(task: Task) {
        this.task = task
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = TaskDetailsBottomSheetLayoutBinding.inflate(inflater, container, false)

        val task = task!!

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

        viewModel.setAsignees(task)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.asignees.observe(viewLifecycleOwner) { asignees ->
            if(asignees.isEmpty()) {
                binding.taskAsigneeLayout.makeGone()
                return@observe
            }

            binding.taskAsigneeLayout.makeVisible()
            binding.taskAsigneeLayout.removeAllViews()

            asignees.forEach {
                val asigneeView = LayoutInflater.from(context).inflate(R.layout.task_asignee_display_layout, binding.taskAsigneeLayout, false)
                val binding = TaskAsigneeDisplayLayoutBinding.bind(asigneeView)
                binding.tvAsigneeName.text = it.name
                binding.ivAsignee.tvProfile.text = it.name.first().toString()
                this.binding.taskAsigneeLayout.addView(asigneeView)
            }
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
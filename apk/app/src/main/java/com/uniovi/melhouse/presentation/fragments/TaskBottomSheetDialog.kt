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
import com.uniovi.melhouse.databinding.TaskAsigneeDisplayLayoutBinding
import com.uniovi.melhouse.databinding.TaskDetailsBottomSheetLayoutBinding
import com.uniovi.melhouse.factories.viewmodel.TaskBottomSheetViewModelFactory
import com.uniovi.melhouse.utils.adaptTextToSize
import com.uniovi.melhouse.utils.getColor
import com.uniovi.melhouse.utils.getDatesString
import com.uniovi.melhouse.utils.getWarningSnackbar
import com.uniovi.melhouse.utils.makeGone
import com.uniovi.melhouse.utils.makeVisible
import com.uniovi.melhouse.viewmodel.TaskBottomSheetViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import java.util.UUID

@AndroidEntryPoint
class TaskBottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var binding : TaskDetailsBottomSheetLayoutBinding
    private val viewModel: TaskBottomSheetViewModel by viewModels(extrasProducer = {
        defaultViewModelCreationExtras
            .withCreationCallback<TaskBottomSheetViewModelFactory> { factory ->
            factory.create(taskId) { dismiss() }
        }
    })
    private lateinit var taskId: UUID

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = TaskDetailsBottomSheetLayoutBinding.inflate(inflater, container, false)
        taskId = UUID.fromString(arguments?.getString(TASK_ID_PARAMETER))

        viewModel.clearAllErrors()

        viewModel.task.observe(this) { task ->
            if(task == null) return@observe

            // Set asignees
            if(task.assignees.isEmpty()) {
                binding.taskAsigneeLayout.makeGone()
                binding.tvTaskAsignee.makeGone()
            } else {
                binding.taskAsigneeLayout.makeVisible()
                binding.tvTaskAsignee.makeVisible()
                binding.taskAsigneeLayout.removeAllViews()

                task.assignees.forEach {
                    val asigneeView = LayoutInflater
                        .from(context)
                        .inflate(
                            R.layout.task_asignee_display_layout,
                            binding.taskAsigneeLayout,
                            false
                        )
                    val binding = TaskAsigneeDisplayLayoutBinding.bind(asigneeView)
                    binding.tvAsigneeName.text = it.name
                    binding.ivAsignee.tvProfile.text = it.name.first().toString()
                    this.binding.taskAsigneeLayout.addView(asigneeView)
                }
            }

            // Set priority
            val priority = task.priority
            if (priority == null) {
                binding.badgeTaskPriority.root.makeGone()
            } else {
                binding.badgeTaskPriority.tvStatus.text = priority.getString(requireContext())
                binding.badgeTaskPriority.ivStatus.setColorFilter(priority.getColor(requireContext()))
            }

            // Set status
            val status = task.status
            if (status == null) {
                binding.badgeTaskStatus.root.makeGone()
            } else {
                binding.badgeTaskStatus.tvStatus.text = status.getString(requireContext())
                binding.badgeTaskStatus.ivStatus.setColorFilter(status.getColor(requireContext()))
            }

            // Set dates
            if (task.endDate == null) {
                binding.taskDaysLayout.makeGone()
            } else {
                binding.tvTaskDates.text = task.getDatesString()
            }

            // Set title and description
            binding.tvTaskTitle.text = task.name.adaptTextToSize()
            binding.tvTaskDescription.text = task.description?.adaptTextToSize()

            // Set buttons
            binding.btnDeleteTask.setOnClickListener {
                showConfirmDialog(task.name)
            }

            binding.btnEditTask.setOnClickListener {
                dismiss()

                val fragment = UpsertTaskFragment.create(task)
                parentFragmentManager
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.calendar_fragment_container, fragment, UpsertTaskFragment.TAG)
                    .addToBackStack(null)
                    .commit()
            }
        }

        viewModel.genericError.observe(this) {
            if (it == null) return@observe

            getWarningSnackbar(requireView(), it).show()
            viewModel.clearGenericError()
        }

        return binding.root
    }

    private fun showConfirmDialog(name: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.task_elimination_dialog_title))
            .setMessage(resources.getString(R.string.task_elimination_dialog_supporting_text, name))
            .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.continuar)) { _, _ ->
                viewModel.deleteTask()
            }
            .show()
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
        private const val TASK_ID_PARAMETER = "task_id"

        fun create(taskId: UUID): TaskBottomSheetDialog {
            return TaskBottomSheetDialog().apply {
                arguments = Bundle().apply {
                    putString(TASK_ID_PARAMETER, taskId.toString())
                }
            }
        }
    }
}
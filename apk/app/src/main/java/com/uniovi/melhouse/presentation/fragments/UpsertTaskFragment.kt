package com.uniovi.melhouse.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.allViews
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.TaskPriority
import com.uniovi.melhouse.data.model.TaskStatus
import com.uniovi.melhouse.data.model.toJson
import com.uniovi.melhouse.data.model.toTask
import com.uniovi.melhouse.databinding.CalendarUpsertTaskFragmentBinding
import com.uniovi.melhouse.factories.viewmodel.UpsertTaskViewModelFactory
import com.uniovi.melhouse.presentation.adapters.array.TaskPriorityDropDownMenuAdapter
import com.uniovi.melhouse.presentation.adapters.array.TaskStatusDropDownMenuAdapter
import com.uniovi.melhouse.presentation.buttons.AssigneeMaterialButton
import com.uniovi.melhouse.utils.addStatusBarColorUpdate
import com.uniovi.melhouse.utils.getWarningSnackbar
import com.uniovi.melhouse.utils.makeGone
import com.uniovi.melhouse.utils.makeVisible
import com.uniovi.melhouse.utils.maxDate
import com.uniovi.melhouse.utils.toEditable
import com.uniovi.melhouse.utils.today
import com.uniovi.melhouse.viewmodel.UpsertTaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import java.time.LocalDate

@AndroidEntryPoint
class UpsertTaskFragment : Fragment() {

    private var task: Task? = null
    private val viewModel: UpsertTaskViewModel by viewModels(extrasProducer = {
        defaultViewModelCreationExtras.withCreationCallback<UpsertTaskViewModelFactory> { factory ->
            factory.create(task)
        }
    })
    private lateinit var binding: CalendarUpsertTaskFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CalendarUpsertTaskFragmentBinding.inflate(inflater, container, false)
        task = arguments?.getString(TASK_PARAMETER)?.toTask(withTransientFields = true)
        return binding.root
    }

    private fun setupObservers() {
        viewModel.startDate.observe(viewLifecycleOwner) {
            if(it != null) {
                binding.btnClearStartDate.makeVisible()
                binding.etStartDate.text = it.toString().toEditable()
            } else {
                binding.btnClearStartDate.makeGone()
                binding.etStartDate.text = "".toEditable()
            }
        }

        viewModel.endDate.observe(viewLifecycleOwner) {
            if(it != null) {
                binding.btnClearEndDate.makeVisible()
                binding.etEndDate.text = it.toString().toEditable()
            } else {
                binding.btnClearEndDate.makeGone()
                binding.etEndDate.text = "".toEditable()
            }
        }

        viewModel.status.observe(viewLifecycleOwner) {
            if(it == null) {
                binding.dmStatus.tvDropdownField.setText("", false)
                binding.btnClearStatus.makeGone()
            } else {
                binding.dmStatus.tvDropdownField.setText(it.getString(requireContext()), false)
                binding.btnClearStatus.makeVisible()
            }
        }

        viewModel.priority.observe(viewLifecycleOwner) {
            if(it == null) {
                binding.dmPriority.tvDropdownField.setText("", false)
                binding.btnClearPriority.makeGone()
            } else {
                binding.dmPriority.tvDropdownField.setText(it.getString(requireContext()), false)
                binding.btnClearPriority.makeVisible()
            }
        }

        viewModel.assignees.observe(viewLifecycleOwner) { asignees ->
            if (asignees == null || viewModel.parters.value.isNullOrEmpty()) return@observe

            binding.asigneesBtnGroup.allViews.forEachIndexed { i, btn ->
                if(i == 0) return@forEachIndexed // Skips the view itself
                val button = btn as AssigneeMaterialButton
                applyColor(asignees.contains(button.asignee), button)
            }
        }

        viewModel.parters.observe(viewLifecycleOwner) {
            if(it.isNullOrEmpty()) return@observe

            binding.asigneesBtnGroup.removeAllViews()

            it.forEach { mate ->
                mate.let {
                    val button = AssigneeMaterialButton(requireContext(), mate).apply {
                        text = it.name
                        strokeWidth = 2
                        setStrokeColorResource(R.color.secondary)
                    }
                    button.setOnClickListener{
                        viewModel.setAsignee(button.asignee)
                    }
                    binding.asigneesBtnGroup.addView(button)
                }
            }

            viewModel.triggerAsigneesObserver()
        }

        viewModel.creationSuccessful.observe(viewLifecycleOwner) {
            if(it)
                requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewModel.genericError.observe(viewLifecycleOwner) {
            if(it == null) return@observe
            getWarningSnackbar(requireView(), it).show()
        }

        viewModel.titleError.observe(viewLifecycleOwner) {
            binding.etTaskTitle.error = it
        }

        viewModel.endDateError.observe(viewLifecycleOwner) {
            binding.etEndDate.error = it
        }
    }

    private fun applyColor(condition: Boolean, button: MaterialButton) {
        if (condition)
            button.apply {
                setBackgroundColor(getColor(context, R.color.secondary_container))
                setTextColor(getColor(context, R.color.on_secondary_container))
            }
        else
            button.apply {
                setBackgroundColor(getColor(context, R.color.background))
                setTextColor(getColor(context, R.color.on_background))
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        val task = task
        if(task != null) {
            binding.etTaskTitle.text = task.name.toEditable()
            binding.etTaskDescription.text = task.description?.toEditable()
        }

        binding.dmStatus.dropdownLayout.hint = getString(R.string.task_status)
        binding.dmPriority.dropdownLayout.hint = getString(R.string.task_priority)

        binding.btnBack.setOnClickListener {
            requireActivity()
                .onBackPressedDispatcher
                .onBackPressed()
        }

        binding.etStartDate.setOnClickListener {
            showDatePickerDialog(requireContext().getString(R.string.task_select_start_date), today(), viewModel.endDate.value ?: maxDate()) {
                viewModel.setStartDate(it)
            }
        }

        binding.etEndDate.setOnClickListener {
            showDatePickerDialog(requireContext().getString(R.string.task_select_end_date), viewModel.startDate.value ?: today(), maxDate()) {
                viewModel.setEndDate(it)
            }
        }

        binding.etTaskTitle.doOnTextChanged { _, _, _, _ ->
            viewModel.title = binding.etTaskTitle.text.toString()
        }
        binding.etTaskDescription.doOnTextChanged { _, _, _, _ ->
            viewModel.description = binding.etTaskDescription.text.toString()
        }

        binding.dmStatus.tvDropdownField.apply {
            isFocusable = false
            isClickable = true

            val defaultStatus = TaskStatus.PENDING
            setText(defaultStatus.getString(requireContext()), false)
            viewModel.setStatus(defaultStatus)

            setAdapter(
                TaskStatusDropDownMenuAdapter(
                requireContext(),
                TaskStatus.entries)
            )
            setOnClickListener {
                showDropDown()
            }
            setOnItemClickListener { adapterView, _, position, _ ->
                val item = adapterView.getItemAtPosition(position) as TaskStatus
                setText(item.getString(requireContext()), false)
                viewModel.setStatus(item)
            }
        }

        binding.dmPriority.tvDropdownField.apply {
            isFocusable = false
            isClickable = true

            val defaultPriority = TaskPriority.MEDIUM
            setText(defaultPriority.getString(requireContext()), false)
            viewModel.setPriority(defaultPriority)

            setAdapter(
                TaskPriorityDropDownMenuAdapter(
                requireContext(),
                TaskPriority.entries)
            )
            setOnClickListener {
                showDropDown()
            }
            setOnItemClickListener { adapterView, _, position, _ ->
                val item = adapterView.getItemAtPosition(position) as TaskPriority
                setText(item.getString(requireContext()), false)
                viewModel.setPriority(item)
            }
        }

        binding.btnSave.setOnClickListener {
            viewModel.upsertTask()
        }

        binding.btnClearStartDate.setOnClickListener {
            viewModel.setStartDate(null)
        }

        binding.btnClearEndDate.setOnClickListener {
            viewModel.setEndDate(null)
        }

        binding.btnClearStatus.setOnClickListener {
            viewModel.setStatus(null)
        }

        binding.btnClearPriority.setOnClickListener {
            viewModel.setPriority(null)
        }

        addStatusBarColorUpdate(R.color.background)

        if(task != null) {
            binding.etTaskTitle.text = task.name.toEditable()
            binding.etTaskDescription.text = task.description?.toEditable()
            binding.etStartDate.text = task.startDate?.toString()?.toEditable()
            binding.etEndDate.text = task.endDate?.toString()?.toEditable()
            binding.dmStatus.tvDropdownField.text = task.status?.getString(requireContext())?.toEditable()
            binding.dmPriority.tvDropdownField.text = task.priority?.getString(requireContext())?.toEditable()
        }
    }

    private fun showDatePickerDialog(title: String, startDate: LocalDate, endDate: LocalDate, listener: (LocalDate) -> Unit) {
        val validatorComposite = CompositeDateValidator.allOf(listOf(
            DateValidatorPointBackward.before(endDate.toEpochDay() * MILLIS_PER_DAY),
            DateValidatorPointForward.from(startDate.toEpochDay() * MILLIS_PER_DAY),
        ))

        val constraintsBuilder = CalendarConstraints.Builder()
            .setStart(MaterialDatePicker.todayInUtcMilliseconds())
            .setValidator(validatorComposite)
            .setEnd(maxDate().toEpochDay() * MILLIS_PER_DAY)

        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraintsBuilder.build())
            .setTitleText(title)
            .build()

        datePicker.addOnPositiveButtonClickListener {
            listener(LocalDate.ofEpochDay(it / MILLIS_PER_DAY))
        }

        datePicker.show(parentFragmentManager, "DatePickerFragment")
    }

    companion object {
        const val TAG = "UpsertTaskFragment"

        private const val MILLIS_PER_DAY = 86400000
        private const val TASK_PARAMETER = "task_state_json"

        fun create(task: Task? = null) : UpsertTaskFragment {
            return UpsertTaskFragment().apply {
                arguments = Bundle().apply {
                    putString(TASK_PARAMETER, task?.toJson(withTransientFields = true))
                }
            }
        }
    }
}
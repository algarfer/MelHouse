package com.uniovi.melhouse.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.TaskPriority
import com.uniovi.melhouse.data.model.TaskStatus
import com.uniovi.melhouse.databinding.CalendarUpsertTaskFragmentBinding
import com.uniovi.melhouse.presentation.adapters.array.TaskPriorityDropDownMenuAdapter
import com.uniovi.melhouse.presentation.adapters.array.TaskStatusDropDownMenuAdapter
import com.uniovi.melhouse.utils.addStatusBarColorUpdate
import com.uniovi.melhouse.utils.makeGone
import com.uniovi.melhouse.utils.makeVisible
import com.uniovi.melhouse.utils.maxDate
import com.uniovi.melhouse.utils.toEditable
import com.uniovi.melhouse.utils.today
import com.uniovi.melhouse.viewmodel.UpsertTaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json
import java.time.LocalDate

@AndroidEntryPoint
class UpsertTaskFragment : Fragment() {

    private var task: Task? = null
    private val viewModel: UpsertTaskViewModel by viewModels()
    private lateinit var binding: CalendarUpsertTaskFragmentBinding
    private val MILLIS_PER_DAY = 86400000
    private val TASK_PARAMETER = "task_json"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CalendarUpsertTaskFragmentBinding.inflate(inflater, container, false)
        task = arguments?.getString(TASK_PARAMETER)?.let {
            Json.decodeFromString<Task>(it)
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.startDate.observe(this) {
            if(it != null) {
                binding.btnClearStartDate.makeVisible()
                binding.etStartDate.text = it.toString().toEditable()
            } else {
                binding.btnClearStartDate.makeGone()
                binding.etStartDate.text = "".toEditable()
            }
        }

        viewModel.endDate.observe(this) {
            if(it != null) {
                binding.btnClearEndDate.makeVisible()
                binding.etEndDate.text = it.toString().toEditable()
            } else {
                binding.btnClearEndDate.makeGone()
                binding.etEndDate.text = "".toEditable()
            }
        }

        viewModel.status.observe(this) {
            if(it == null) {
                binding.dmStatus.tvDropdownField.setText("", false)
                binding.btnClearStatus.makeGone()
            } else {
                binding.dmStatus.tvDropdownField.setText(it.getString(requireContext()), false)
                binding.btnClearStatus.makeVisible()
            }
        }

        viewModel.priority.observe(this) {
            if(it == null) {
                binding.dmPriority.tvDropdownField.setText("", false)
                binding.btnClearPriority.makeGone()
            } else {
                binding.dmPriority.tvDropdownField.setText(it.getString(requireContext()), false)
                binding.btnClearPriority.makeVisible()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val task = task
        if(task != null) {
            viewModel.onViewCreated(task)
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
            viewModel.setTitle(binding.etTaskTitle.text.toString())
        }
        binding.etTaskDescription.doOnTextChanged { _, _, _, _ ->
            viewModel.setDescription(binding.etTaskDescription.text.toString())
        }


        binding.dmStatus.tvDropdownField.apply {
            isFocusable = false
            isClickable = true
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
            requireActivity().onBackPressedDispatcher.onBackPressed()
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

        fun create(taskJson: String? = null) : UpsertTaskFragment {
            return UpsertTaskFragment().apply {
                arguments = Bundle().apply {
                    putString(TASK_PARAMETER, taskJson)
                }
            }
        }
    }
}
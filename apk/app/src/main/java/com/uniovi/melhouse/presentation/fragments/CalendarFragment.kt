package com.uniovi.melhouse.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.uniovi.melhouse.R
import com.uniovi.melhouse.databinding.CalendarDayLayoutBinding
import com.uniovi.melhouse.databinding.CalendarFragmentBinding
import com.uniovi.melhouse.databinding.CalendarHeaderLayoutBinding
import com.uniovi.melhouse.factories.presentation.adapter.TasksAdapterFactory
import com.uniovi.melhouse.presentation.adapters.TasksAdapter
import com.uniovi.melhouse.utils.addStatusBarColorUpdate
import com.uniovi.melhouse.utils.displayText
import com.uniovi.melhouse.utils.getColorCompat
import com.uniovi.melhouse.utils.lighterColor
import com.uniovi.melhouse.utils.setTextColorRes
import com.uniovi.melhouse.presentation.viewholder.taskPressedHandler
import com.uniovi.melhouse.utils.getWarningSnackbar
import com.uniovi.melhouse.viewmodel.CalendarViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.YearMonth
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class CalendarFragment : BaseFragment(R.layout.calendar_fragment), HasToolbar, HasBackButton {
    override val toolbar: Toolbar get() = binding.calendarViewAppBar

    @Inject lateinit var tasksAdapterFactory: TasksAdapterFactory
    private lateinit var tasksAdapter: TasksAdapter
    private val viewModel: CalendarViewModel by viewModels()

    private lateinit var binding: CalendarFragmentBinding

    override fun onResume() {
        super.onResume()

        viewModel.genericError.observe(this) {
            if(it == null) return@observe

            getWarningSnackbar(requireView(), it).show()
        }

        viewModel.date.observe(this) {
            binding.calendarView.scrollToMonth(it.yearMonth)
            binding.calendarView.notifyDateChanged(it)
        }

        viewModel.dailyTasks.observe(this) {
            tasksAdapter.updateList(it.orEmpty())
        }

        viewModel.tasks.observe(this) {
            binding.calendarView.notifyCalendarChanged()
            configureBinders(daysOfWeek(firstDayOfWeekFromLocale(Locale.getDefault())))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CalendarFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addStatusBarColorUpdate(R.color.primary)

        tasksAdapter = tasksAdapterFactory.create(listOf()) { task ->
            taskPressedHandler(parentFragmentManager, task.id)
        }

        binding.tasksView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = tasksAdapter
        }

        val daysOfWeek = daysOfWeek(firstDayOfWeekFromLocale(Locale.getDefault()))
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(200)
        val endMonth = currentMonth.plusMonths(200)

        configureBinders(daysOfWeek)

        binding.calendarView.setup(startMonth, endMonth, daysOfWeek.first())
        binding.calendarView.scrollToMonth(currentMonth)

        binding.calendarView.monthScrollListener = { month ->
            binding.calendarViewCurrentMonth.text = month.yearMonth.displayText()
            viewModel.date.value?.let {
                binding.calendarView.notifyDateChanged(it)
                viewModel.updateDay(it)
            }
        }

        binding.addTaskFab.setOnClickListener {
            val fragment = UpsertTaskFragment.create()
            parentFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true) //
                .replace(R.id.calendar_fragment_container, fragment, UpsertTaskFragment.TAG)
                .addToBackStack(null)
                .commit()
        }
    }


    private fun configureBinders(daysOfWeek: List<DayOfWeek>) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val binding = CalendarDayLayoutBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
                        if (viewModel.date.value != day.date) {
                            val oldDate = viewModel.date.value
                            viewModel.updateDay(day.date)
                            val binding = this@CalendarFragment.binding
                            binding.calendarView.notifyDateChanged(day.date)
                            oldDate?.let { binding.calendarView.notifyDateChanged(it) }
                        }
                    }
                }
            }
        }
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val context = container.binding.root.context
                val textView = container.binding.calendarDayText
                val layout = container.binding.calendarDayLayout
                textView.text = data.date.dayOfMonth.toString()

                val taskIndicator = container.binding.calendarDayIndicator
                taskIndicator.background = null
                val tasks = viewModel.tasks.value

                if (tasks != null && !tasks[data.date].isNullOrEmpty()) {
                    val color = if (data.position != DayPosition.MonthDate)
                        context.getColorCompat(R.color.tertiary).lighterColor()
                    else
                        context.getColorCompat(R.color.tertiary)
                    taskIndicator.setBackgroundColor(color)
                }

                if (data.position != DayPosition.MonthDate) {
                    textView.setTextColorRes(R.color.on_primary_container)
                    layout.setBackgroundColor(context.getColorCompat(R.color.primary_container).lighterColor())
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = CalendarHeaderLayoutBinding.bind(view).calendarHeaderLayout.root
        }

        binding.calendarView.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    if (container.legendLayout.tag == null) {
                        container.legendLayout.tag = true
                        container.legendLayout.children.map { it as TextView }
                            .forEachIndexed { index, tv ->
                                tv.text = daysOfWeek[index].displayText(uppercase = true)
                            }
                    }
                }
            }
    }
}

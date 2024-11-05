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
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.uniovi.melhouse.R
import com.uniovi.melhouse.databinding.CalendarDayLayoutBinding
import com.uniovi.melhouse.databinding.CalendarFragmentBinding
import com.uniovi.melhouse.databinding.CalendarHeaderLayoutBinding
import com.uniovi.melhouse.presentation.adapter.TasksAdapter
import com.uniovi.melhouse.presentation.utils.addStatusBarColorUpdate
import com.uniovi.melhouse.presentation.utils.displayText
import com.uniovi.melhouse.presentation.utils.getColorCompat
import com.uniovi.melhouse.presentation.utils.lighterColor
import com.uniovi.melhouse.presentation.utils.setTextColorRes
import com.uniovi.melhouse.presentation.viewholder.taskPressedHandler
import com.uniovi.melhouse.presentation.viewmodel.CalendarViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale

class CalendarFragment : BaseFragment(R.layout.calendar_fragment), HasToolbar, HasBackButton {
    override val toolbar: Toolbar get() = binding.calendarViewAppBar

    private var selectedDate: LocalDate? = null
    private val tasksAdapter = TasksAdapter(listOf()) { taskPressedHandler(parentFragmentManager, it) }
    private val viewModel: CalendarViewModel by viewModels()

    private lateinit var binding: CalendarFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onCreate()

        viewModel.dailyTasks.observe(this) {
            tasksAdapter.updateList(it[selectedDate].orEmpty())
        }

        viewModel.tasks.observe(this) {
            binding.calendarView.notifyCalendarChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateTasks()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.calendar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addStatusBarColorUpdate(R.color.primary)
        binding = CalendarFragmentBinding.bind(view)

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

            selectedDate?.let {
                selectedDate = null
                binding.calendarView.notifyDateChanged(it)
                viewModel.updateDailyTasks(null)
            }
        }

        binding.addTaskFab.setOnClickListener {
            val fragment = AddTaskFragment()
            parentFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true) //
                .replace(R.id.calendar_fragment_container, fragment, AddTaskFragment.TAG)
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
                        if (selectedDate != day.date) {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            val binding = this@CalendarFragment.binding
                            binding.calendarView.notifyDateChanged(day.date)
                            oldDate?.let { binding.calendarView.notifyDateChanged(it) }
                            viewModel.updateDailyTasks(day.date)
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

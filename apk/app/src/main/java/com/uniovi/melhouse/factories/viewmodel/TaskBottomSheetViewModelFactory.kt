package com.uniovi.melhouse.factories.viewmodel

import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.viewmodel.TaskBottomSheetViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import java.util.UUID

@AssistedFactory
interface TaskBottomSheetViewModelFactory {

    fun create(
        taskId: UUID,
        @Assisted("close") closeTaskBottomSheetDialog: (() -> Unit)? = null,
        @Assisted("updateTasks") updateTasksViewHolder: (() -> Unit)? = null,
        @Assisted("updateCalendar") updateCalendarViewModel: (() -> Unit)? = null
    ): TaskBottomSheetViewModel
}
package com.uniovi.melhouse.data.repository.task

import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.repository.Repository
import java.time.LocalDate

interface TaskRepository : Repository<Task> {
    suspend fun findByDate(date: LocalDate?): List<Task>
}

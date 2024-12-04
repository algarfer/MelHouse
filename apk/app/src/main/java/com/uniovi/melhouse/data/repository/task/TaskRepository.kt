package com.uniovi.melhouse.data.repository.task

import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.repository.Repository
import java.time.LocalDate
import java.util.UUID

interface TaskRepository : Repository<Task> {
    suspend fun findByDate(date: LocalDate?): List<Task>
}
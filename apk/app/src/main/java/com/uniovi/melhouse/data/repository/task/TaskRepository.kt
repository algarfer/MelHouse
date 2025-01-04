package com.uniovi.melhouse.data.repository.task

import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.repository.Repository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.UUID

interface TaskRepository : Repository<Task> {
    suspend fun findByDate(date: LocalDate?): List<Task>
    suspend fun findAssignedByDate(date: LocalDate?): List<Task>
    suspend fun findByFlatId(flatId: UUID): List<Task>
    fun findByFlatIdAsFlow(flatId: UUID): Flow<List<Task>>
    fun findAssignedByDateAsFlow(date: LocalDate?): Flow<List<Task>>
}

package com.uniovi.melhouse.data.repository

import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface Repository<T> {

    suspend fun insert(entity: T)
    suspend fun update(entity: T)
    suspend fun delete(entity: T)
    suspend fun findById(id: UUID): T?
    suspend fun findAll(): List<T>
    fun findAllAsFlow(): Flow<List<T>>
    fun findByIdAsFlow(id: UUID): Flow<T>
}
package com.uniovi.melhouse.data.repository

import java.util.UUID

interface Repository<T> {

    suspend fun insert(entity: T)
    suspend fun update(entity: T)
    suspend fun delete(entity: T)
    suspend fun findById(id: UUID): T?
    suspend fun findAll(): List<T>
}
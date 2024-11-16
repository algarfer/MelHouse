package com.uniovi.melhouse.data.repository

interface Repository<T> {

    suspend fun insert(entity: T)
    suspend fun update(entity: T)
    // TODO - Change to delete(id: UUID)
    suspend fun delete(entity: T)
    suspend fun findById(id: Any): T?
    suspend fun findAll(): List<T>
}
package com.uniovi.melhouse.data.repository

interface Repository<T> {

    suspend fun insert(entity: T)
    suspend fun update(entity: T)
    suspend fun delete(entity: T)
    suspend fun findById(id: Any): T?
    suspend fun findAll(): List<T>
}
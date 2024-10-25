package com.uniovi.melhouse.data.repository

interface Repository<T> {

    fun insert(entity: T)
    fun update(entity: T)
    fun delete(entity: T)
    fun findById(id: Any): T?
    fun findAll(): List<T>
}
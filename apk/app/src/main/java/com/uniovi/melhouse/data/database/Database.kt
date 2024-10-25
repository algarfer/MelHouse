package com.uniovi.melhouse.data.database

import com.uniovi.melhouse.data.repository.flat.FlatRepository
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.data.repository.user.UserRepository

interface Database<T> {
    fun getInstance(): T

    // Repository factory
    fun getUserRepository(): UserRepository
    fun getTaskRepository(): TaskRepository
    fun getFlatRepository(): FlatRepository
}
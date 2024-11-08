package com.uniovi.melhouse.data.database

interface Database<T> {
    fun getInstance(): T
}
package com.uniovi.melhouse.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.uniovi.melhouse.data.repository.flat.FlatRepository
import com.uniovi.melhouse.data.repository.flat.FlatRepositorySQLite
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.data.repository.task.TaskRepositorySQLite
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.data.repository.user.UserRepositorySQLite
import java.io.BufferedReader
import java.io.InputStreamReader

object SQLite : Database<SQLiteDatabase> {

    @Volatile
    private var instance: SQLiteDatabase? = null

    fun init(context: Context) {
        if(instance != null) return
        instance = SQLiteDatabase.openOrCreateDatabase(":memory:", null)
        configureDatabase(context)
    }

    override fun getInstance(): SQLiteDatabase {
        if(instance == null) {
            throw IllegalStateException("Database not initialized")
        }
        return instance!!
    }

    private fun configureDatabase(context: Context) {
        executeSqlFile(context, "schema.sql")
        executeSqlFile(context, "data.sql")
    }

    private fun executeSqlFile(context: Context, fileName: String) {
        val inputStream = context.assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val db = instance!!

        db.beginTransaction()
        try {
            reader.use {
                val sqlScript = it.readText()
                val sqlStatements = sqlScript.split(";")
                for (statement in sqlStatements) {
                    val trimmedStatement = statement.trim()
                    if (trimmedStatement.isNotEmpty()) {
                        db.execSQL(trimmedStatement)
                    }
                }
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }
}

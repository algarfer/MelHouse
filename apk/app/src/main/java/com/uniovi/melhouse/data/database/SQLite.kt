package com.uniovi.melhouse.data.database

import android.database.sqlite.SQLiteDatabase

class SQLite : Database {

    companion object {
        @Volatile
        private var instance: SQLiteDatabase? = null

        fun getInstance(): SQLiteDatabase {
            if(instance == null) {
                instance = SQLiteDatabase.openOrCreateDatabase(":memory:", null)
                configureDatabase()
            }
            return instance!!
        }

        private fun configureDatabase() {
            TODO()
        }
    }
}
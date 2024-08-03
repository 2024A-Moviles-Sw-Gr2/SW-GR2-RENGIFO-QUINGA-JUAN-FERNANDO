package com.example.deber02

import android.content.Context

class DataBase {

    companion object {
        var tables: SqliteHelper? = null

        fun initialize(context: Context) {
            if (tables == null) {
                tables = SqliteHelper(context)
            }
        }

        fun getInstance(): SqliteHelper {
            return tables ?: throw IllegalStateException("Database not initialized")
        }
    }
}
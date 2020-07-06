package com.example.githubuserfinalbfaa.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.githubuserfinalbfaa.db.DatabaseContract.GitColumns.Companion.AVATAR
import com.example.githubuserfinalbfaa.db.DatabaseContract.GitColumns.Companion.LOGIN_NAME
import com.example.githubuserfinalbfaa.db.DatabaseContract.GitColumns.Companion.TABLE_NAME
import com.example.githubuserfinalbfaa.db.DatabaseContract.GitColumns.Companion._ID

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {

        private const val DATABASE_NAME = "dbgituser"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAV = "CREATE TABLE $TABLE_NAME" +
                "($_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$LOGIN_NAME TEXT NOT NULL UNIQUE," +
                "$AVATAR TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAV)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}
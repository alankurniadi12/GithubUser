package com.example.githubuserfinalbfaa.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.githubuserfinalbfaa.db.DatabaseContract.GitColumns.Companion.AVATAR
import com.example.githubuserfinalbfaa.db.DatabaseContract.GitColumns.Companion.LOGIN_NAME
import com.example.githubuserfinalbfaa.db.DatabaseContract.GitColumns.Companion.TABLE_NAME
import com.example.githubuserfinalbfaa.db.DatabaseContract.GitColumns.Companion._ID
import com.example.githubuserfinalbfaa.model.UserModel
import java.sql.SQLException

class GitHelper (context: Context) {

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: GitHelper? = null
        fun getInstance(context: Context): GitHelper = INSTANCE?: synchronized(this){
            INSTANCE?: GitHelper(context)
        }
    }

    /*init {
        dataBaseHelper = DatabaseHelper(context)
    }*/

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(): ArrayList<UserModel> {
        val arrayList = ArrayList<UserModel>()
        val cursor = database.query(DATABASE_TABLE, null, null, null, null, null, "$_ID ASC")
        cursor.moveToFirst()
        var userModel: UserModel
        if (cursor.count > 0){
            do {
                userModel = UserModel()
                userModel.id = cursor.getInt(cursor.getColumnIndex(_ID))
                userModel.login = cursor.getString(cursor.getColumnIndex(LOGIN_NAME))
                userModel.avatar = cursor.getString(cursor.getColumnIndex(AVATAR))
                arrayList.add(userModel)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }
        return arrayList
    }

   /* fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE, null, null, null, null, null, "$_ID ASC"
        )
    }*/

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE, null, "$_ID = ?", arrayOf(id), null, null, null, null
        )
    }
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }
    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }
}
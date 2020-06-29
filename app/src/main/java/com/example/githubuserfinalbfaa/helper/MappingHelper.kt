package com.example.githubuserfinalbfaa.helper

import android.database.Cursor
import android.util.Log
import com.example.githubuserfinalbfaa.db.DatabaseContract
import com.example.githubuserfinalbfaa.model.UserModel

object MappingHelper {
    val TAG = MappingHelper::class.java.simpleName
    fun mapCursorToArrayList(gitCursor: Cursor?): ArrayList<UserModel> {
        val gitList = ArrayList<UserModel>()
        gitCursor?.apply {
            while (moveToNext()) {
                val userModel = UserModel()
                userModel.id = getInt(getColumnIndexOrThrow(DatabaseContract.GitColumns._ID))
                userModel.login = getString(getColumnIndexOrThrow(DatabaseContract.GitColumns.LOGIN_NAME))
                userModel.avatar = getString(getColumnIndexOrThrow(DatabaseContract.GitColumns.AVATAR))
                gitList.add(userModel)
                Log.d(TAG, userModel.toString())
            }
        }
        return gitList
    }


    fun mapCursorToObject(gitFavorite: Cursor?): UserModel {
        var userModel = UserModel()
        gitFavorite?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.GitColumns._ID))
            val loginName = getString(getColumnIndexOrThrow(DatabaseContract.GitColumns.LOGIN_NAME))
            val avatar = getString(getColumnIndexOrThrow(DatabaseContract.GitColumns.AVATAR))
            userModel = UserModel(id, loginName, avatar)
        }
        return userModel
    }

}

package com.example.githubuserfinalbfaa.db

import android.net.Uri
import android.provider.BaseColumns
import com.example.githubuserfinalbfaa.db.DatabaseContract.GitColumns.Companion.TABLE_NAME

object DatabaseContract {
    const val AUTHORITY = "com.example.githubuserfinalbfaa"
    private const val SCHEME = "content"

    class GitColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "git_user"
            const val _ID = "_id"
            const val LOGIN_NAME = "login_name"
            const val AVATAR = "avatar"
        }
    }

    //content://com.example.githubuserfinalbfaa/git_user
    val CONTENT_URI : Uri = Uri.Builder().scheme(SCHEME)
        .authority(AUTHORITY)
        .appendPath(TABLE_NAME)
        .build()
}
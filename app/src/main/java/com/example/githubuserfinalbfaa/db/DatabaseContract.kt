package com.example.githubuserfinalbfaa.db

import android.provider.BaseColumns

internal class DatabaseContract {
    internal class GitColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "git_user"
            const val _ID = "_id"
            const val LOGIN_NAME = "login_name"
            const val AVATAR = "avatar"
        }
    }
}
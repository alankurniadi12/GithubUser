package com.example.githubuserfinalbfaa.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.githubuserfinalbfaa.db.DatabaseContract.AUTHORITY
import com.example.githubuserfinalbfaa.db.DatabaseContract.CONTENT_URI
import com.example.githubuserfinalbfaa.db.DatabaseContract.GitColumns.Companion.TABLE_NAME
import com.example.githubuserfinalbfaa.db.GitHelper

class MyContentProvider : ContentProvider() {

    companion object {
        private const val GIT = 1
        private const val GIT_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var gitHelper: GitHelper

        init {
            //com.example.githubuserfinalbfaa/git_user
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, GIT)
            //com.example.githubuserfinalbfaa/git_user/id
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", GIT_ID)
        }
    }

    override fun onCreate(): Boolean {
        gitHelper = GitHelper.getInstance(context as Context)
        gitHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?
        when (sUriMatcher.match(uri)) {
            GIT -> cursor = gitHelper.queryAll()
            GIT_ID -> cursor = gitHelper.queryById(uri.lastPathSegment.toString())
            else -> cursor = null
        }
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (GIT) {
            sUriMatcher.match(uri) -> gitHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when (GIT_ID) {
            sUriMatcher.match(uri) -> gitHelper.update(uri.lastPathSegment.toString(), values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (GIT_ID) {
            sUriMatcher.match(uri) -> gitHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }










}

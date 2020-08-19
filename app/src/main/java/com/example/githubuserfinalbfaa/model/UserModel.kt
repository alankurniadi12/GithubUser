package com.example.githubuserfinalbfaa.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel (
    var id: Int? = null,
    var login: String? = null,
    var name: String? = null,
    var avatar: String? = null,
    var company: String? = null,
    var location: String? = null,
    var blog: String? = null,
    var repository: String? = null,
    var follower: Int = 0,
    var following: Int = 0
): Parcelable

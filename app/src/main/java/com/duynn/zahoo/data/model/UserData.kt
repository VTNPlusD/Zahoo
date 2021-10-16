package com.duynn.zahoo.data.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

/**
 *Created by duynn100198 on 10/04/21.
 */
@Parcelize
@Entity(tableName = "users")
data class UserData(
    @NonNull
    @PrimaryKey
    @Json(name = "id")
    var id: String = "",
    @Json(name = "name")
    var name: String? = "",
    @Json(name = "status")
    var status: String? = "",
    @Json(name = "image")
    var image: String? = "",
    @Json(name = "nameInPhone")
    var nameInPhone: String = "",
    @Ignore
    @Json(name = "selected")
    var selected: Boolean = false,
    @Json(name = "online")
    var online: Boolean = false,
    @Json(name = "typing")
    var typing: Boolean = false,
    @Ignore
    @Json(name = "inviteAble")
    var inviteAble: Boolean = false
) : BaseData(), Parcelable

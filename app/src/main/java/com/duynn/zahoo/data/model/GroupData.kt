package com.duynn.zahoo.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

/**
 * Created by duynn100198 on 10/12/21.
 */
@Parcelize
@Entity(tableName = "groups")
data class GroupData(
    @PrimaryKey
    @Json(name = "id")
    var id: String = "",
    @Json(name = "name")
    var name: String = "",
    @Json(name = "status")
    var status: String = "",
    @Json(name = "image")
    var image: String = "",
    @Exclude
    @Ignore
    @Json(name = "userIds")
    var userIds: List<String> = emptyList()
) : BaseData(), Parcelable

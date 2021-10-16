package com.duynn.zahoo.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

/**
 * Created by duynn100198 on 10/12/21.
 */
@Parcelize
@Entity(tableName = "chats")
data class ChatData(
    @PrimaryKey(autoGenerate = true)
    @Json(name = "id")
    var id: Int = 0,
    @Json(name = "myId")
    var myId: String = "",
    @Json(name = "chatId")
    var chatId: String = "",
    @Json(name = "chatName")
    var chatName: String = "",
    @Json(name = "chatImage")
    var chatImage: String = "",
    @Json(name = "chatStatus")
    var chatStatus: String = "",
    @Json(name = "lastMessage")
    var lastMessage: String = "",
    @Json(name = "read")
    var read: Boolean = false,
    @Json(name = "group")
    var group: Boolean = false,
    @Json(name = "timeUpdated")
    var timeUpdated: Long = 0L,
    @Ignore
    @Json(name = "selected")
    var selected: Boolean = false
) : BaseData(), Parcelable

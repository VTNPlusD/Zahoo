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
@Entity(tableName = "messages")
data class MessageData(
    @PrimaryKey
    @Json(name = "id")
    var id: String = "",
    @Json(name = "chatId")
    var chatId: String? = null,
    @Json(name = "senderName")
    var senderName: String = "",
    @Json(name = "senderImage")
    var senderImage: String = "",
    @Json(name = "senderStatus")
    var senderStatus: String = "",
    @Json(name = "recipientName")
    var recipientName: String = "",
    @Json(name = "recipientImage")
    var recipientImage: String = "",
    @Json(name = "recipientStatus")
    var recipientStatus: String = "",
    @Json(name = "body")
    var body: String = "",
    @Json(name = "recipientId")
    var recipientId: String = "",
    @Json(name = "senderId")
    var senderId: String = "",
    @Json(name = "date")
    var date: Long = 0L,
    @Json(name = "delivered")
    var delivered: Boolean = false,
    @Json(name = "sent")
    var sent: Boolean = false,
    @Json(name = "attachmentType")
    var attachmentType: Int = 0,
    @Json(name = "attachment")
    var attachment: AttachmentData? = null,
    @Json(name = "recipientGroupIds")
    var recipientGroupIds: List<String>? = null,
    @Ignore
    var selected: Boolean = false
) : BaseData(), Parcelable

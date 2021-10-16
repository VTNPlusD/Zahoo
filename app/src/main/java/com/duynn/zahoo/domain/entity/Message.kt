package com.duynn.zahoo.domain.entity

import android.os.Parcelable
import androidx.room.Ignore
import kotlinx.parcelize.Parcelize

/**
 * Created by duynn100198 on 10/12/21.
 */
@Parcelize
data class Message(
    val senderName: String,
    val senderImage: String,
    val senderStatus: String,
    val recipientName: String,
    val recipientImage: String,
    val recipientStatus: String,
    val body: String,
    val id: String,
    val recipientId: String,
    val senderId: String,
    val date: Long,
    val delivered: Boolean,
    val sent: Boolean,
    val attachmentType: Int,
    val attachment: Attachment?,
    @Ignore
    val selected: Boolean,
    val recipientGroupIds: List<String>? = null
) : BaseEntity(), Parcelable

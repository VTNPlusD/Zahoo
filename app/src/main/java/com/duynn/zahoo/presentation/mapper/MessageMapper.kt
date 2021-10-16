package com.duynn.zahoo.presentation.mapper

import com.duynn.zahoo.data.model.MessageData
import com.duynn.zahoo.domain.entity.Message

/**
 * Created by duynn100198 on 10/6/21.
 */
class MessageMapper(private val attachmentMapper: AttachmentMapper) :
    BaseMapper<MessageData, Message>() {
    override fun map(data: MessageData): Message {
        return data.run {
            Message(
                senderName = senderName,
                senderImage = senderImage,
                senderStatus = senderImage,
                recipientName = recipientName,
                recipientImage = recipientImage,
                recipientStatus = recipientStatus,
                body = body,
                id = id,
                recipientId = recipientId,
                senderId = senderId,
                date = date,
                delivered = delivered,
                sent = sent,
                attachmentType = attachmentType,
                attachment = attachmentMapper.nullableMap(attachment),
                selected = selected,
                recipientGroupIds = recipientGroupIds
            )
        }
    }
}

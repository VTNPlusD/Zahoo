package com.duynn.zahoo.data.repository.source.local.api.db

import androidx.room.TypeConverter
import com.duynn.zahoo.data.model.AttachmentData
import com.duynn.zahoo.data.model.MessageData
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * Created by duynn100198 on 10/12/21.
 */
class Converters {
    @TypeConverter
    fun <T> jsonFormAttachment(data: AttachmentData): String {
        return Moshi.Builder().build().adapter(AttachmentData::class.java).toJson(data)
    }

    @TypeConverter
    fun attachmentFromJson(value: String): AttachmentData? {
        return Moshi.Builder().build().adapter(AttachmentData::class.java).fromJson(value)
    }

    @TypeConverter
    fun jsonFromListString(list: List<String>?): String {
        return Moshi.Builder().build().adapter<List<String>>(
            Types.newParameterizedType(
                MutableList::class.java,
                String::class.java
            )
        ).toJson(list)
    }

    @TypeConverter
    fun listStringFromJson(value: String): List<String>? {
        return Moshi.Builder().build()
            .adapter<List<String>>(Types.newParameterizedType(MutableList::class.java,
                String::class.java))
            .fromJson(value)
    }

    @TypeConverter
    fun jsonFromListMessage(list: List<MessageData>?): String {
        return Moshi.Builder().build().adapter<List<MessageData>>(
            Types.newParameterizedType(
                MutableList::class.java,
                MessageData::class.java
            )
        ).toJson(list)
    }

    @TypeConverter
    fun listMessageFromJson(value: String): List<MessageData>? {
        return Moshi.Builder().build()
            .adapter<List<MessageData>>(Types.newParameterizedType(MutableList::class.java,
                MessageData::class.java))
            .fromJson(value)
    }
}

package com.duynn.zahoo.data.model

/**
 * Created by duynn100198 on 10/12/21.
 */
enum class AttachmentTypes(val number: Int, val value: String) {
    CONTACT(0, "Contact"),
    VIDEO(1, "Video"),
    IMAGE(2, "Image"),
    AUDIO(3, "Audio"),
    LOCATION(4, "Location"),
    DOCUMENT(5, "Document"),
    NONE_TEXT(6, "none_text"),
    NONE_TYPING(7, "none_typing"),
    RECORDING(8, "Recording"),
    NONE(-1, "none")
}

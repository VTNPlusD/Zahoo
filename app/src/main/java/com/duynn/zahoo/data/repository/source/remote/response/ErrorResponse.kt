package com.duynn.zahoo.data.repository.source.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 *Created by duynn100198 on 10/04/21.
 */
@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "messages") val messages: String,
    @Json(name = "status_code") val statusCode: Int,
    @Json(name = "success") val success: Boolean
)

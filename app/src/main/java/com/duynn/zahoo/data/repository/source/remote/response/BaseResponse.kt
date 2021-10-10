package com.duynn.zahoo.data.repository.source.remote.response

import com.duynn.zahoo.data.error.AppError
import com.squareup.moshi.Json

/**
 *Created by duynn100198 on 10/04/21.
 */
data class BaseResponse<Data : Any>(
    @Json(name = "data") private val data: Data,
    @Json(name = "messages") val messages: String,
    @Json(name = "status_code") val statusCode: Int,
    @Json(name = "success") val success: Boolean
) {
    fun unwrap(): Data {
        return if (success) data
        else throw AppError.Remote.ServerError(
            errorMessage = messages,
            statusCode = statusCode
        )
    }
}

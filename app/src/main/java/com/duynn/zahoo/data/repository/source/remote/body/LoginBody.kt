package com.duynn.zahoo.data.repository.source.remote.body

import com.squareup.moshi.Json

/**
 *Created by duynn100198 on 10/04/21.
 */
data class LoginBody(
    @Json(name = "email")
    val email: String?,
    @Json(name = "password")
    val password: String?
)

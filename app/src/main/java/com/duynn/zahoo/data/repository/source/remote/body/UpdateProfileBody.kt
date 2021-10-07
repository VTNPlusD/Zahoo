package com.duynn.zahoo.data.repository.source.remote.body

import com.squareup.moshi.Json

/**
 *Created by duynn100198 on 10/04/21.
 */
data class UpdateProfileBody(
    @Json(name = "user_name")
    val userName: String,
    @Json(name = "phone")
    val phone: String,
    @Json(name = "image_path")
    val imagePath: String?
)

package com.duynn.zahoo.data.repository.source.remote.response

import com.squareup.moshi.Json

/**
 *Created by duynn100198 on 10/04/21.
 */
data class ImageResponse(
    @Json(name = "image_path")
    val imagePath: String
)

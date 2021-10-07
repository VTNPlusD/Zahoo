package com.duynn.zahoo.data.model

import com.squareup.moshi.Json

/**
 * Created by duynn100198 on 10/6/21.
 */
data class CountryData(
    val name: String,
    @Json(name = "dial_code") val dialCode: String,
    val code: String
) : BaseData()

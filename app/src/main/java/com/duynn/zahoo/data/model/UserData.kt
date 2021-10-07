package com.duynn.zahoo.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

/**
 *Created by duynn100198 on 10/04/21.
 */
@JsonClass(generateAdapter = true)
data class UserData(
    @Json(name = "user_name")
    val userName: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "role_id")
    val roleId: Int?,
    @Json(name = "image_path")
    val imagePath: String?,
    @Json(name = "_id")
    val id: String?,
    @Json(name = "phone")
    val phone: String?,
    @Json(name = "date")
    val date: Date?
) : BaseData()

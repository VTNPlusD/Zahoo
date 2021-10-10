package com.duynn.zahoo.data.model

import com.squareup.moshi.Json

/**
 *Created by duynn100198 on 10/04/21.
 */
data class UserData(
    @Json(name = "id")
    val id: String? = "",
    @Json(name = "name")
    val name: String? = "",
    @Json(name = "status")
    val status: String? = "",
    @Json(name = "image")
    val image: String? = "",
    @Json(name = "nameInPhone")
    var nameInPhone: String = "",
    @Json(name = "selected")
    val selected: Boolean = false,
    @Json(name = "online")
    val online: Boolean = false,
    @Json(name = "typing")
    val typing: Boolean = false,
    @Json(name = "inviteAble")
    val inviteAble: Boolean = false
) : BaseData()

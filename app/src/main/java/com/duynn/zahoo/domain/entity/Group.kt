package com.duynn.zahoo.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by duynn100198 on 10/12/21.
 */
@Parcelize
data class Group(
    val id: String,
    val name: String,
    val status: String,
    val image: String,
    val userIds: List<String>
) : BaseEntity(), Parcelable

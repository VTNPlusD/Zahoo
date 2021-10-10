package com.duynn.zahoo.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *Created by duynn100198 on 10/04/21.
 */
@Parcelize
data class User(
    val id: String?,
    val name: String?,
    val status: String?,
    val image: String?,
    val nameInPhone: String,
    val online: Boolean,
    val typing: Boolean,
    val selected: Boolean,
    val inviteAble: Boolean
) : Parcelable, BaseEntity()

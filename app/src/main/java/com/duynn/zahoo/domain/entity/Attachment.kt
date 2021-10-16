package com.duynn.zahoo.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by duynn100198 on 10/12/21.
 */
@Parcelize
data class Attachment(
    val name: String,
    val data: String,
    val url: String,
    val byesCount: Long
) : BaseEntity(), Parcelable

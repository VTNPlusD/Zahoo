package com.duynn.zahoo.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by duynn100198 on 10/12/21.
 */
@Parcelize
data class Contact(
    val name: String,
    val phoneNUmber: String
) : BaseEntity(), Parcelable

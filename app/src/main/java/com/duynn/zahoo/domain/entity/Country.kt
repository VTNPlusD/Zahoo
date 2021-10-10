package com.duynn.zahoo.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by duynn100198 on 10/6/21.
 */
@Parcelize
data class Country(
    val name: String,
    val dialCode: String,
    val code: String
) : Parcelable, BaseEntity() {

    override fun toString() = "($dialCode) $name"
}

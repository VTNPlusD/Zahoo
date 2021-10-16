package com.duynn.zahoo.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by duynn100198 on 10/12/21.
 */
@Parcelize
data class ContactData(
    val name: String,
    val phoneNUmber: String
) : BaseData(), Parcelable

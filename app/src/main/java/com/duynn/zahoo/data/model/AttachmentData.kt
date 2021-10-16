package com.duynn.zahoo.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by duynn100198 on 10/12/21.
 */
@Parcelize
data class AttachmentData(
    var name: String = "",
    var data: String = "",
    var url: String = "",
    var byesCount: Long = 0L
) : BaseData(), Parcelable

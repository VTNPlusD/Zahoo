package com.duynn.zahoo.presentation.mapper

import com.duynn.zahoo.data.model.AttachmentData
import com.duynn.zahoo.domain.entity.Attachment

/**
 * Created by duynn100198 on 10/6/21.
 */
class AttachmentMapper : BaseMapper<AttachmentData, Attachment>() {
    override fun map(data: AttachmentData): Attachment {
        return Attachment(
            name = data.name,
            data = data.data,
            url = data.url,
            byesCount = data.byesCount
        )
    }
}

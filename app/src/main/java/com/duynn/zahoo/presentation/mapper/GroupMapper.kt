package com.duynn.zahoo.presentation.mapper

import com.duynn.zahoo.data.model.GroupData
import com.duynn.zahoo.domain.entity.Group

/**
 * Created by duynn100198 on 10/6/21.
 */
class GroupMapper : BaseMapper<GroupData, Group>() {
    override fun map(data: GroupData): Group {
        return Group(
            id = data.id,
            name = data.name,
            status = data.status,
            image = data.image,
            userIds = data.userIds
        )
    }
}

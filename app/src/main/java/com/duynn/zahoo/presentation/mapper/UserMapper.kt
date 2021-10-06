package com.duynn.zahoo.presentation.mapper

import com.duynn.zahoo.data.model.UserData
import com.duynn.zahoo.domain.entity.User

/**
 *Created by duynn100198 on 10/04/21.
 */
class UserMapper : BaseMapper<UserData, User>() {
    override fun map(data: UserData): User {
        return data.run {
            User(
                userName = userName,
                email = email,
                roleId = roleId,
                imagePath = imagePath,
                id = id,
                phone = phone,
                date = date
            )
        }
    }
}

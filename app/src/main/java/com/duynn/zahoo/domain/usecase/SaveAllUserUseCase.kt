package com.duynn.zahoo.domain.usecase

import com.duynn.zahoo.data.model.UserData
import com.duynn.zahoo.domain.repository.UserRepository

/**
 * Created by duynn100198 on 10/12/21.
 */
class SaveAllUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(users: List<UserData>) = userRepository.saveAllUser(users)
}

package com.duynn.zahoo.domain.usecase

import com.duynn.zahoo.domain.repository.UserRepository

/**
 *Created by duynn100198 on 10/04/21.
 */
class UserLoginUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        email: String,
        password: String
    ) = userRepository.login(email, password)
}

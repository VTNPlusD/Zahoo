package com.duynn.zahoo.domain.usecase

import com.duynn.zahoo.domain.repository.UserRepository

/**
 * Created by duynn100198 on 10/9/21.
 */
class ClearAuthUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.clearAuth()
}

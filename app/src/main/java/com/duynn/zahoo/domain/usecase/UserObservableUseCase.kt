package com.duynn.zahoo.domain.usecase

import com.duynn.zahoo.domain.repository.UserRepository

/**
 *Created by duynn100198 on 10/04/21.
 */
class UserObservableUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.userObservable()
}

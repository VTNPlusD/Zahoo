package com.duynn.zahoo.domain.usecase

import com.duynn.zahoo.domain.repository.UserRepository

/**
 * Created by duynn100198 on 10/6/21.
 */
class UserGetCountriesUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.getCountries()
}

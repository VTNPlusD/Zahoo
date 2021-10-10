package com.duynn.zahoo.domain.usecase

import android.app.Activity
import com.duynn.zahoo.domain.repository.UserRepository

/**
 * Created by duynn100198 on 10/10/21.
 */
class VerifyPhoneNumberUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(activity: Activity) = userRepository.verifyPhoneNumber(activity)
}

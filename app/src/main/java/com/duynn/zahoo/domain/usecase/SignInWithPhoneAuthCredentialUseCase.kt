package com.duynn.zahoo.domain.usecase

import com.duynn.zahoo.domain.repository.UserRepository
import com.google.firebase.auth.PhoneAuthCredential

/**
 * Created by duynn100198 on 10/10/21.
 */
class SignInWithPhoneAuthCredentialUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(credential: PhoneAuthCredential) =
        userRepository.signInWithPhoneAuthCredential(credential)
}

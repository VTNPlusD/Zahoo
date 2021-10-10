package com.duynn.zahoo.di

import com.duynn.zahoo.domain.usecase.CheckAuthUseCase
import com.duynn.zahoo.domain.usecase.ClearAuthUseCase
import com.duynn.zahoo.domain.usecase.GetTokenUseCase
import com.duynn.zahoo.domain.usecase.SetTokenUseCase
import com.duynn.zahoo.domain.usecase.SignInWithPhoneAuthCredentialUseCase
import com.duynn.zahoo.domain.usecase.TokenObservableUseCase
import com.duynn.zahoo.domain.usecase.UserGetCountriesUseCase
import com.duynn.zahoo.domain.usecase.UserObservableUseCase
import com.duynn.zahoo.domain.usecase.VerifyPhoneNumberUseCase
import org.koin.dsl.module

/**
 *Created by duynn100198 on 10/04/21.
 */
val useCaseModule = module {
    factory { CheckAuthUseCase(userRepository = get()) }
    factory { UserObservableUseCase(userRepository = get()) }
    factory { UserGetCountriesUseCase(userRepository = get()) }
    factory { ClearAuthUseCase(userRepository = get()) }
    factory { GetTokenUseCase(userRepository = get()) }
    factory { SetTokenUseCase(userRepository = get()) }
    factory { TokenObservableUseCase(userRepository = get()) }
    factory { VerifyPhoneNumberUseCase(userRepository = get()) }
    factory { SignInWithPhoneAuthCredentialUseCase(userRepository = get()) }
}

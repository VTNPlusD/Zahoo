package com.duynn.zahoo.di

import com.duynn.zahoo.domain.usecase.CheckAuthUseCase
import com.duynn.zahoo.domain.usecase.UserGetCountriesUseCase
import com.duynn.zahoo.domain.usecase.UserLoginUseCase
import com.duynn.zahoo.domain.usecase.UserLogoutUseCase
import com.duynn.zahoo.domain.usecase.UserObservableUseCase
import org.koin.dsl.module

/**
 *Created by duynn100198 on 10/04/21.
 */
val useCaseModule = module {
    factory { UserLoginUseCase(userRepository = get()) }
    factory { CheckAuthUseCase(userRepository = get()) }
    factory { UserObservableUseCase(userRepository = get()) }
    factory { UserLogoutUseCase(userRepository = get()) }
    factory { UserGetCountriesUseCase(userRepository = get()) }
}

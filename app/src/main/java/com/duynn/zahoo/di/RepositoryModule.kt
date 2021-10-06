package com.duynn.zahoo.di

import com.duynn.zahoo.data.repository.UserRepositoryImpl
import com.duynn.zahoo.domain.repository.UserRepository
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

/**
 *Created by duynn100198 on 10/04/21.
 */
@OptIn(KoinApiExtension::class)
val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get(), get(), get()) }
}

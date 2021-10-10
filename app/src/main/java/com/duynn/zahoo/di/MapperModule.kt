package com.duynn.zahoo.di

import com.duynn.zahoo.presentation.mapper.CountryMapper
import com.duynn.zahoo.presentation.mapper.PhoneAuthMapper
import com.duynn.zahoo.presentation.mapper.UserMapper
import org.koin.dsl.module

/**
 *Created by duynn100198 on 10/04/21.
 */
val mapperModule = module {
    factory { UserMapper() }
    factory { CountryMapper() }
    factory { PhoneAuthMapper() }
}

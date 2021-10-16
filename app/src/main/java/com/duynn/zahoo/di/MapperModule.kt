package com.duynn.zahoo.di

import com.duynn.zahoo.presentation.mapper.AttachmentMapper
import com.duynn.zahoo.presentation.mapper.CountryMapper
import com.duynn.zahoo.presentation.mapper.GroupMapper
import com.duynn.zahoo.presentation.mapper.MessageMapper
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
    factory { MessageMapper(attachmentMapper = get()) }
    factory { AttachmentMapper() }
    factory { GroupMapper() }
}

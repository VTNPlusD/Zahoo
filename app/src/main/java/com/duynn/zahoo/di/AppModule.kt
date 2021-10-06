package com.duynn.zahoo.di

import com.duynn.zahoo.BuildConfig
import com.duynn.zahoo.utils.constants.Constants
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 *Created by duynn100198 on 10/04/21.
 */
val appModule = module {
    single(named(Constants.KEY_BASE_URL)) {
        BuildConfig.BASE_URL
    }
}

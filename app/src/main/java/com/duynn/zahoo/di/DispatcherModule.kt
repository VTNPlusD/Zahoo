package com.duynn.zahoo.di

import com.duynn.zahoo.data.scheduler.DefaultDispatcher
import com.duynn.zahoo.data.scheduler.IODispatcher
import com.duynn.zahoo.data.scheduler.MainDispatcher
import com.duynn.zahoo.data.scheduler.UnconfinedDispatcher
import com.duynn.zahoo.domain.scheduler.AppDispatchers.DEFAULT
import com.duynn.zahoo.domain.scheduler.AppDispatchers.IO
import com.duynn.zahoo.domain.scheduler.AppDispatchers.MAIN
import com.duynn.zahoo.domain.scheduler.AppDispatchers.UNCONFINED
import com.duynn.zahoo.domain.scheduler.DispatchersProvider
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 *Created by duynn100198 on 10/04/21.
 */
val dispatcherModule = module {
    single<DispatchersProvider>(named(IO)) { IODispatcher() }
    single<DispatchersProvider>(named(DEFAULT)) { DefaultDispatcher() }
    single<DispatchersProvider>(named(MAIN)) { MainDispatcher() }
    single<DispatchersProvider>(named(UNCONFINED)) { UnconfinedDispatcher() }
}

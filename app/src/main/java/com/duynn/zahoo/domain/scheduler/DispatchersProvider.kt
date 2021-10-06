package com.duynn.zahoo.domain.scheduler

import kotlin.coroutines.CoroutineContext

/**
 *Created by duynn100198 on 10/04/21.
 */
interface DispatchersProvider {
    fun dispatcher(): CoroutineContext
}

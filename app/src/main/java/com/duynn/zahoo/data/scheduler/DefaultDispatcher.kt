package com.duynn.zahoo.data.scheduler

import com.duynn.zahoo.domain.scheduler.DispatchersProvider
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 *Created by duynn100198 on 10/04/21.
 */
class DefaultDispatcher : DispatchersProvider {
    override fun dispatcher(): CoroutineContext {
        return Dispatchers.Default
    }
}
